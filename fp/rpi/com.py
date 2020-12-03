#!/usr/bin/python3

import requests
import json
import threading
import signal
import sys
import time

login_url = "http://192.168.1.19:8080/login.php"
sensor_url = "http://192.168.1.19:8080/sensors.php"
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
        print(json.dumps(data))
        client.send( data )

class Client:
    def __init__(self, sleep=1):
        self.session = requests.Session()
        self.session_id = None
        self.email = None
        self.uid = None
        
        self.cv = threading.Condition()
        self.next = None

        self.sleep = sleep

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
        password = Client.__read_password()
        data = { 'username': username, 'password': password }

        reply = self.session.post(login_url, data=data)
        replyjson = json.loads(reply.text)

        if replyjson["error"] is not None:
            raise RuntimeError("Login Failed")

        self.session_id = replyjson["session"]["session_id"]
        self.uid = replyjson["session"]["uid"]
        self.email = replyjson["session"]["email"]
        
        print("Login Success. uid: xxx%s" % self.session_id[-6:])

    def send(self, json):
        while True:
            with self.cv:
                if self.next is None:
                    self.next = json
                    self.cv.notify()
                    break

    def threadTarget(self):
        while not self.exitThread.is_set():
            nextSend = None

            while not nextSend:
                with self.cv:
                    self.cv.wait()
                    nextSend = self.next
                    self.next = None

                    if self.exitThread.is_set():
                        return

            reply = self.session.post(sensor_url, data=nextSend)
            replyjson = json.loads(reply.text)

            if "error" in replyjson.keys():
                print(reply.text)
            

def signal_handler(sig, frame):
    client.stopJoin()
    sys.exit(0)

if __name__ == '__main__':
    signal.signal(signal.SIGINT, signal_handler)

    client = Client()
    sensors = SensorPoll(client)

    while True:
        sensors.run(-1)
        time.sleep(3)
        sensors.run(0)
        time.sleep(3)
        sensors.run(1)
        time.sleep(3)

