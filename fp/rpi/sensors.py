#!/usr/bin/env python3

import socket
import queue

HOST = '127.0.0.1'  # The server's hostname or IP address
PORT = 6000        # The port used by the server
msg = 'Hello World'

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    data = str(len(msg)) + '\n' + msg
    s.sendall(data.encode())





class Client:
    def __init__():
        self.mQ = queue.Queue()
        self.mThread = threading.Thread(target=__thread_target)
        self.mThread.start()

    def __thread_target():
        while True:
            dmQ.get()
