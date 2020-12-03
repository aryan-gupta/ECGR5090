#!/usr/bin/python3

import requests
import multiprocessing
import json
import threading
import signal
import sys
import socketserver

login_url = "http://192.168.1.19:8080/login.php"
username = 'test'
password_file = "../secrets/app_test_user_password.txt"
local_server_addr = address = ('localhost', 6000)


client = None
server = None


class ServerConnectionHandler(socketserver.StreamRequestHandler):
    def handle_update(msg):
        obj = {}
        obj['opcode'] = 'update'
        obj['sensor_id'] = msg['id']
        obj['state'] = msg['state']

    def handle_message(msg):
        pass

    def handle(self):
        data = self.rfile.readline().strip()
        msg_len = int(data.decode())

        data = b''
        len_recv = 0
        while len_recv < msg_len:
            data += self.rfile.read(1024)
            len_recv = len(data)

        msg = data.decode()
        msgjson = json.loads(msg)
        
        reqtype = msgjson['type']
        reqmsg = msgjson['message']

        if reqtype == 'update':
            handle_update(reqauth, reqmsg)



# class Server:
#     def __init__(self):
#         self.connections = {}


#     def thread_target(self):
#         with Listener(local_con) as listener:
#             with listener.accept() as conn:
#                 while True:
#                     data = handle_connection(conn)
#                     reply = handle_data(data)

#         with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock
#             s.bind(local_server_addr)
#             s.listen(1)

#             conn, addr = s.accept()
#             print 'Connection address:', addr
#             while 1:
#                 data = conn.recv(BUFFER_SIZE)
#                 if not data: break
#                 print "received data:", data
#                 conn.send(data)  # echo
#             conn.close()



class Client:
    def __init__(self, sleep=1):
        self.session = requests.Session()
        self.session_id = None
        self.email = None
        self.uid = None
        
        self.mutex = threading.Lock()
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

    def stop(self):
        self.exitThread.set()

    def stopJoin(self):
        self.stop()
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
        updated = False
        while not updated:
            with self.mutex:
                if self.next is None:
                    self.next = json
                    updated = True

    def threadTarget(self):
        nextSend = None

        with self.mutex:
            if self.next is not None:
                nextSend = self.next
                self.next = None

        while not self.exitThread.is_set():
            print("Hola")
        

def signal_handler(sig, frame):
    #client.stopJoin()
    server.stopJoin()
    sys.exit(0)

if __name__ == '__main__':
    #signal.signal(signal.SIGINT, signal_handler)

    #client = Client()
    server = socketserver.ThreadingTCPServer(local_server_addr, ServerConnectionHandler)
    with server:
        server.serve_forever()
