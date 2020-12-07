#!/usr/bin/python3

import requests
import json
import threading
import signal
import sys
import time
import socket

login_url = "http://192.168.1.19:8080/login.php"
sensor_url = "http://192.168.1.19:8080/sensors.php"
app_svr = ("192.168.1.19", 9080)
username = 'test'
password_file = "../secrets/app_test_user_password.txt"

class SensorPoll:
    def __init__(self, client):
        self.client = client

    def run(self, state):
        data = {}
        data["opcode"] = "update"
        data["update_sensor_id"] = 4
        data["state"] = state
        # print(json.dumps(data))
        client.send( data )

class Client:
    def __init__(self, method='socket', sleep=1):
        self.session_id = None
        self.token = None
        self.email = None
        self.uid = None
        
        self.cv = threading.Condition()
        self.next = None

        self.sleep = sleep

        if method == 'post':
            self.session = requests.Session()
            self.use_socket = False
        elif method == 'socket':
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.connect(app_svr)
            self.use_socket = True

        self.login()

        self.exitThread = threading.Event()
        self.thread = threading.Thread(target=self.threadTarget)
        self.thread.start()
    
    def __read_password():
        with open(password_file, "r") as f:
            return f.read()

    def join(self):
        self.thread.join()

    def notify(self):
        with self.cv:
            self.cv.notify()

    def stop(self):
        self.exitThread.set()

    def stopJoin(self):
        self.stop()
        self.notify()
        self.join()

    def login(self):
        print("Logging in")
        password = Client.__read_password()
        data = { 'username': username, 'password': password }
        
        if self.use_socket:
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

        else:
            reply = self.session.post(login_url, data=data)
            replyjson = json.loads(reply.text)

        print(replyjson)

        if 'error' in replyjson and replyjson['error'] is not None:
            raise RuntimeError("Login Failed")

        print(replyjson)

        self.session_id = replyjson["session"]["session_id"]
        self.uid = replyjson["session"]["uid"]
        self.email = replyjson["session"]["email"]
        self.token = replyjson["token"]
        
        print("Login Success. uid: xxx%s" % self.session_id[-6:])

    def send(self, json):
        while True:
            with self.cv:
                if self.next is None:
                    self.next = json
                    self.cv.notify()
                    break

    def post_method(self, data):
        reply = self.session.post(sensor_url, data=data)
        replyjson = json.loads(reply.text)
        return replyjson
    
    def socket_method(self, data):
        # Send message
        data['token'] = self.token
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
        return replyjson

    def threadTarget(self):
        print("Client thread started")

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

            replyjson = {}
            if self.use_socket:
                replyjson = self.socket_method(nextSend)
            else:
                replyjson = self.post_method(nextSend)

            print("Recived reply: ", json.dumps(replyjson))

            if 'error' in replyjson and replyjson['error'] is not None:
                print(reply.text)


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

