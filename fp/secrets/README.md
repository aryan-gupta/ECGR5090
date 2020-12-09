I fully understand that I am committing passwords into the git repo and will not use these passwords in production code

```shell
openssl req -new -newkey rsa:2048 -days 365 -nodes -x509 -keyout server.key -out server.crt
```

CSR Root CA Info:
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:North Carolina
Locality Name (eg, city) []:Charlotte
Organization Name (eg, company) [Internet Widgits Pty Ltd]:ECGR5090
Organizational Unit Name (eg, section) []:Root CA Sense49
Common Name (e.g. server FQDN or YOUR name) []:rootca.49ersense.com
Email Address []:webmaster@49ersense.com


CSR Server Info:
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:North Carolina
Locality Name (eg, city) []:Charlotte
Organization Name (eg, company) [Internet Widgits Pty Ltd]:ECGR5090
Organizational Unit Name (eg, section) []:49er Sense
Common Name (e.g. server FQDN or YOUR name) []:python-server.49ersense.com
Email Address []:webmaster@49ersense.com
Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:


CSR Client Info:
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:North Carolina
Locality Name (eg, city) []:Charlotte
Organization Name (eg, company) [Internet Widgits Pty Ltd]:ECGR5090
Organizational Unit Name (eg, section) []:Clients
Common Name (e.g. server FQDN or YOUR name) []:test client
Email Address []:test@49ersense.com
Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
An optional company name []:
