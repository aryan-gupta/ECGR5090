FROM python:latest
MAINTAINER Aryan Gupta <agupta40@uncc.edu>

RUN pip install requests mysql.connector

WORKDIR /run

EXPOSE 9080/tcp

ENTRYPOINT sleep 3 && python /run/server.py