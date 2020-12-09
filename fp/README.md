# Final Project Details
**A video presentation of this project can be viewed at [https://youtu.be/MQKK1fnTlgg](https://youtu.be/MQKK1fnTlgg)**

This project is split into 3 devices

 - The Android
 - The Data Center Server
 - The IOT Raspberry Pi (rpi)

The devices are further split into more parts depending on the work it performs.

## The Android App
This is the most lacking part of the project. Since this is the most lacking. There isnt much to say here. Please view the video for more info. 

### Weather
The weather is using the Open Weather Map API and displayed on the home menu

## The Data Center Server
The data center is hosted on Docker containers, this has two main benefits

 1. Allows me to spin up and down the containers when working on other class' projects
    - They also needed me to use an database so using a Docker container kept it split up
 2. Allows fault tolerance then using in a Docker Swarm. If a node of a data center went
    down, using a Docker container and Docker Swarm would allow the IT admins to quickly
    move the database fo another node with minimal downtime

The server is farther split into the following services (more details can be found in `server/docker-compose.yml`):

 - php-apache : The PHP/Apache server running the web api in front of the database
 - mariadb : The SQL database storing the states and user info
 - phpmyadmin : The PHPMyAdmin server that allows a frontend to the database
 - python-svr : THe python server running the socket api in front of the database

### php-apache
The php server that hosts a web api for the service. All API calls are made via POST. The php server is split into these files:
 - `database.php` : Connects to the database and creates a connection that the other files can be used. Does not run
   by itself but is included by other php files
 - `first_run.php` : Creates a user if the user does not exist. Akin to registering a new user
 - `index.php` : The web front end that allows testing of the api
 - `login.php` : api endpoint that allows users to login
 - `logout.php` : api endpoint that deletes the login session and logs out the user
 - `redirect.php` : A script that tells the client to redirect if using the web. Does not run
   by itself but is included by other php files
 - `sensors.php` : API endpoint that allows users to view and update the sensors
 - `sessions.php` : A script that creates a new session and recalls a previous session. Does not run
   by itself but is included by other php files

#### database.php
This small script sets up the connection to the database but doesn't do anything. This allows the file to be included and now the code has a global variable containing a `sqli` object that contains the connection. This file also stores constants like the database address and database name.

#### first_run.php
Registers the user and creates the default sensors. The sensors are pulled from a yaml file so it can be edit and changes as the products change and grow. The file storing the default sensors is `server/default_sensors.yaml`. The details of the user creation in the database is detailed under ###mariadb####users.

#### index.php
Contains a simple frontend that allows testing and debugging of the database and API. More details in the video

#### login.php
Logs in the user, will return an error json if the user is not in the database stating that you need to register. Other error codes include:

 - `EmptyValue` - The username or password was empty
 - `FeatureNotSupported` - The requested feature is not currently supported
 - `NoSuchUser` - No such user exists, must register
 - `IncorrectPassword` - The specified password was incorrect

If the login was successful, the session ID, uid, and email is returned in a JSON format. In future revisions of the API, this should be an opcode and should be consolidated into a single endpoint.  

#### logout.php
Logs the user out and deletes the session. n future revisions of the API, this should be an opcode and should be consolidated into a single endpoint. 

#### redirect.php
Include code that allows the browser the web front is being tested at to be redirected back to the main page after 5 seconds. This allows testing without having to press the back button in the browser so many times. 

#### sensors.php
Does something using the sensors. This is the main API and its documentation is shown in thr #Protocol section of this document.

#### sessions.php
Include code that creates a new session.

### mariadb
The SQL database is split into two tables, however a third table was planned for the video storage but never got implemented.

#### sensors
The sensors table holds all the sensors for all the users. 

``` sql
CREATE TABLE sensors (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    userid INT UNSIGNED NOT NULL,
    type ENUM(
        'system',
        'garage_door',
        'thermostat_mode',
        'thermostat_fan',
        'thermostat_temp',
        'thermostat_set_temp',
        'lights_level',
        'lock_state',
        'door_window_sensor',
        'motion_sensor' ) NOT NULL,
    name VARCHAR(255) NOT NULL,
    number INT(10) UNSIGNED NOT NULL,
    state INT NOT NULL
)
```

 - `userid` of the stores the User ID as denoted by the users table. 
 - `id` of the sensor is the unique id of the sensor. In future this should be auto-generated to prevent hackers from knowing other sensors ids and other attacks. 
 - `name` of the sensor is limited to 255 characters. 
 - `number` column is the floor AND the index of the sensor. The first 3 bits of the number is the floor number and the last 7 bits are the actual index of the sensor. In hindsight, this was a very stupid move and I just haven't had time to change the code to reflect the addition of another column. More details after this list.

```
         3     7
number: 001 1101011
    first 3 bits = floor
    next 7 digits = number on that floor 
    Matlab style counting (Counting starts at 1)
    129 is first number for an upstairs sensor
```

 - `state` stores the current state of that sensor. What the state means is determined by the note made below. `var` means variable and can vary from a range such as the `thermostat_temp` can be a `65`

```
             system  0 disarmed
                     1 stay
                     2 away
        garage_door  0 closed
                     1 open
    thermostat_mode -1 cool
                     0 off
                     1 heat
     thermostat_fan  0 off
                     1 auto
                     2 on
    thermostat_temp    var
thermostat_set_temp    var
       lights_level    var
         lock_state  0 unlocked
                     1 locked
 door_window_sensor  0 off
                     1 on
      motion_sensor  0 inactive
                     1 active
```

#### users
The second table is the users table. This stores details about the user and his/her/their ID. According to https://www.php.net/manual/en/function.password-hash.php password hash is recommended to be 255 chars long. According to this https://stackoverflow.com/questions/1199190 emails are restricted to 254 chars

``` sql
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
)
```

#### recordings
A third database of all the recordings was designed, but never implemented due to time constraints. The database would have the `userid` of the user that the video belonged to, timestamp of when the recording stated in UTC, and the location of the video. This would remove the need to read a slower filesystem (vs. database) when enumerating the list of videos. 


### phpmyadmin
Not much can be said here. Uses the default image with no modifications

### python-svr
This is a custom created docker container that hosts the python socket server. The socket is a multi-threaded socket server that handles each connection in one thread. The connection is kept alive to allow bidirectional transfer at the same time. The connections are stored in a dictionary database and allows the PHP server to signal the IOT clients that a state change has occurred live. This prevents the IOT devices from repeatedly polling the database to check if a sensor value was changed. For example, if the user armed the security system via the Android app or web front end, rather than the rpi repeatedly polling the database for this change, the change will be directly signaled to the rpi using the socket. 

There was an attempt to use SSL in a previous version of the server and is committed in the git history, however because I misinterpreted the project I had to rewrite this code and omitted the SSL so I could at least get it working and maybe later add in the SSL socket wrapping. However, I did not have enough time for this.


## The IOT Raspberry Pi
The IOT RPI is split into two parts. 

 - `stream.py` - The webcam script 
 - `sensors.py` - The sensor polling script

### stream.py
This script pulls data in from the webcam using the opencv library's VideoCapture class.
The frame pulled from the webcam is processed using the MotionDetector class to create an "average" model used
in subsequent loops to detect movements. The bounding boxes for the movement is created using this model and
the current frame. These bounding boxes are drawn in red and given to flask to serve as a website

The code was considered to be multi-threaded and pipelined, however using the timings (shown below), only a 17% increase
would have been observed and was put on a lower priority list.

```
gray: 0.00015909435184857318
bbox: 0.0017290442954492934
dete: 0.0003856199084954104
enco: 0.014365608758902125
```
There is a template file in the `rpi/templates` folder that is the basis of the flask webserver that is setup


### sensors.py
This script handles polling the pins of the rpi and calls the api to update the state in the database. The script is divided into 2 classes. The `ServerPool` class that handles the polling of the sensors and calling the `Client` class to send an api call to update the database. The `Client` is a multi-threaded class that utilizes condition variables to minimize busy waiting. The `Client` has two threads, one for receiving from the socket and one for sending data in to the socket. The socket is verified to be thread safe as long as multiple threads aren't calling recv at the same time or send at the same time. This dual-thread models allows true real-time and bidirectional data transfer. The protocol used is described in the #Protocol section of this document


# Protocols
### Sockets
The socket protocol message is simply the length of the message followed by a `'\n'` character then followed by the message. A sample message is shown below:

```
26
asbcdefghijklmnopqrstuvwxyz
```

The payload of the message is always a JSON encoded string. 

### Payload
The payload is a JSON encoded string. The string always has an `opcode` value or an `error` value. A detailed list of the values in the JSON can be seen below. 

#### opcode
If the top level dictionary contains `opcode` key then its a normal message. Values for opcode can be:

 - `dump` - Dumps all the sensors in your name from the database. Including all properties and IDs. 
 - `search` - Searches all your sensors using a certain regex.
 - `request` - Request all properties of a sensor using a sensor ID
 - `login` - Only usable via the socket API. Logins using the socket
 - `device` - Only usable via socket API. Signals the IOT devices that a state has changed. Allows live updates without polling

Each of opcodes have other properties that must be present in the JSON string. For example. the `request` opcode must have the `request_sensor_id` key that stores the sensor ID that the call is requesting. These are documented in the `server/webroot/sensors.php` and `server/server.py` files.

# Secrets
All the secrets are in the `secrets/` folder. 
 - `README.md` - A disclaimer about the secrets
 - `app_test_user_password.txt` - The app's test user account password
 - `db_phpadmin_password.txt` - The admin account for the database (user: `phpadmin`)
 - `db_root_password.txt` - The root password for the database
 - `python-server.crt` - The python server public certificate for SSL traffic 
 - `python-server.csr` - The signing certificate for the python server certificate
 - `python-server.key` - The private key for the python server
 - `client.crt` - The client public certificate for SSL traffic
 - `client.csr` - The signing certificate for the client certificate
 - `client.key` - The private key for the client
 - `root-ca.crt` - The root certificate authority public certificate for SSL traffic 
 - `root-ca.key` - The signing certificate for the root certificate authority certificate
 - `root-ca.srl` - The private key for the root certificate authority
