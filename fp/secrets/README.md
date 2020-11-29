I fully understand that I am committing passwords into the git repo and will not use these passwords in production code

```shell
openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -keyout server.key -out server.crt
```

CSR Server Info:
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:North Carolina
Locality Name (eg, city) []:Charlotte
Organization Name (eg, company) [Internet Widgits Pty Ltd]:ECGR5090
Organizational Unit Name (eg, section) []:49er Sense
Common Name (e.g. server FQDN or YOUR name) []:Aryan Gupta
Email Address []:webmaster@49ersense.com


CSR Client Info:
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:North Carolina
Locality Name (eg, city) []:Charlotte
Organization Name (eg, company) [Internet Widgits Pty Ltd]:ECGR5090
Organizational Unit Name (eg, section) []:49er Sense Clients
Common Name (e.g. server FQDN or YOUR name) []:test client
Email Address []:test@49ersense.com