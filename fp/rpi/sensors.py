#!/usr/bin/python3

# This script must be run on the client rpi's so that
# it can poll the data from the sensors and update the
# database

import requests
import json
import threading
import signal
import sys
import time
import socket
import os

# this allows me to run this script 
# from my amd64 desktop for testing
if os.uname()[4][:3] == 'arm':
    import RPi.GPIO as GPIO

# some constant variables for this app
login_url = "http://192.168.1.19:8080/login.php"
sensor_url = "http://192.168.1.19:8080/sensors.php"
app_svr = ("192.168.1.19", 9080)
username = 'test'
password_file = "../secrets/app_test_user_password.txt"


# This class Polls the sensors and sends the updates
# to the Client class to be uploaded to the PHP/App
# server
# if the script is running on a rpi use the actual
# pins for the sensor input, if the script is running
# from my desktop (amd64) then just simulate the button
# as a function parameter
class SensorPoll:
    def __init__(self, client):
        self.client = client
        self.state = 0

        if os.uname()[4][:3] == 'arm':
            GPIO.setmode(GPIO.BCM)
            GPIO.setup(4, GPIO.IN)
            GPIO.setup(4, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

    # polls the input every second and requests an update using
    # the client if the state changed from the last run
    def run(self, state=0):
        if os.uname()[4][:3] == 'arm':
            while True:
                time.sleep(1)
                state = GPIO.input(4)
                if state == self.state:
                    continue
                self.state = state
                print("Button State Changed")
                data = {}
                data["opcode"] = "update"
                data["update_sensor_id"] = 4
                data["state"] = self.state
                client.send( data )
        else:
            data = {}
            data["opcode"] = "update"
            data["update_sensor_id"] = 4
            data["state"] = state
            # print(json.dumps(data))
            client.send( data )
            

# the client is responsible for sending and receiving 
# data from the app server. Runs on 2 threads, one for
# receive and one for sending data
class Client:
    def __init__(self, sleep=1):
        # basic information about the user that is logged in
        self.session_id = None
        self.token = None
        self.email = None
        self.uid = None
        
        # a condition var to notify the sending thread that there
        # is data to send to the app server
        self.cv = threading.Condition()
        self.next = None

        # deprecated
        # @todo remove this and the parameter var
        self.sleep = sleep

        # the main socket that is connected to the app server
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect(app_svr)

        # once se have all of our basic variables init'ed
        # go ahead and do a login so we have a token for communication
        self.login()

        # create a event to notify the threads to stop when we want to
        # stop the server gracefully
        self.exitThread = threading.Event()

        # our sending thread
        self.threadSend = threading.Thread(target=self.threadTargetSend)
        self.threadSend.start()
        
        # out receiving thread
        self.threadRecv = threading.Thread(target=self.threadTargetRecv)
        self.threadRecv.start()
    
    # reads the password from the password file
    def __read_password():
        with open(password_file, "r") as f:
            return f.read()

    # waits for the send/recv threads to end
    def join(self):
        self.thread.join()

    # notifies the sending thread that there is data to send
    # is thread safe
    def notify(self):
        with self.cv:
            self.cv.notify()

    # signals the threads to gracefully exit
    def stop(self):
        self.exitThread.set()

    # stops the client gracefully. May be blocking while
    # the threads stop
    def stopJoin(self):
        self.stop()
        self.notify()
        self.join()

    # does the login flow and gets basic details about the user
    # and an auth token for future communication
    def login(self):
        print("Logging in")
        password = Client.__read_password()
        data = { 'username': username, 'password': password }

        # Send message
        data['opcode'] = 'login'
        jsonmsg = json.dumps(data)
        msg = str(len(jsonmsg)) + '\n' + jsonmsg
        self.socket.sendall(msg.encode())

        # First receive the length
        reply = ''
        msg_len = None
        while not msg_len:
            reply += self.socket.recv(1024).decode()

            reply_split = reply.split('\n', 1)
            if len(reply_split) != 2:
                continue
            else:
                len_str = reply_split[0]
                msg_len = int(len_str)
                reply = reply_split[1]
        

        # Then receive the json
        len_recv = len(reply)
        while len_recv < msg_len:
            read_rem = msg_len - len_recv
            reply += self.socket.recv(read_rem).decode()
            len_recv = len(reply)

        replyjson = json.loads(reply)

        print(replyjson)

        if 'error' in replyjson and replyjson['error'] is not None:
            raise RuntimeError("Login Failed")

        print(replyjson)

        self.session_id = replyjson["session"]["session_id"]
        self.uid = replyjson["session"]["uid"]
        self.email = replyjson["session"]["email"]
        self.token = replyjson["token"]
        
        print("Login Success. uid: xxx%s" % self.session_id[-6:])

    # gives data to the send thread to send to the app server
    # is thread safe
    def send(self, json):
        while True:
            with self.cv:
                if self.next is None:
                    self.next = json
                    self.cv.notify()
                    break

    # sends data via the internal socket
    def socket_send(self, data):
        # Send message
        data['token'] = self.token
        jsonmsg = json.dumps(data)
        msg = str(len(jsonmsg)) + '\n' + jsonmsg
        self.socket.sendall(msg.encode())

    # recives and parses the data from the internal socket
    def socket_receive(self):
        # First receive the length
        reply = ''
        msg_len = None
        while not msg_len:
            reply += self.socket.recv(1024).decode()

            reply_split = reply.split('\n', 1)
            if len(reply_split) != 2:
                continue
            else:
                len_str = reply_split[0]
                msg_len = int(len_str)
                reply = reply_split[1]

        # Then receive the json
        len_recv = len(reply)
        while len_recv < msg_len:
            read_rem = msg_len - len_recv
            reply += self.socket.recv(read_rem).decode()
            len_recv = len(reply)

        replyjson = json.loads(reply)
        return replyjson

    # the thread target for the sending thread
    def threadTargetSend(self):
        print("Client Send thread started")

        while not self.exitThread.is_set():
            nextSend = None

            while not nextSend:
                with self.cv:
                    print("Client thread waiting for data")
                    self.cv.wait()
                    nextSend = self.next
                    self.next = None

                if self.exitThread.is_set():
                    print("Client thread exiting")
                    return

            print("Thread recived data to send. %s" % json.dumps(nextSend))
            self.socket_send(nextSend)

    # the thread target for the recv thread
    def threadTargetRecv(self):
        print("Client Recv thread started")

        while not self.exitThread.is_set():
            replyjson = self.socket_receive()
            print("Recived reply: ", json.dumps(replyjson))

            if 'error' in replyjson and replyjson['error'] is not None:
                print(reply.text)

# signal handler for quiting the server
def signal_handler(sig, frame):
    client.stopJoin()
    sys.exit(0)

if __name__ == '__main__':
    #signal.signal(signal.SIGINT, signal_handler)

    client = Client()
    print("Client class created")

    sensors = SensorPoll(client)

    while True:
        sensors.run(-1)
        time.sleep(3)
        sensors.run(0)
        time.sleep(3)
        sensors.run(1)
        time.sleep(3)

    while True:
        time.sleep(5)

