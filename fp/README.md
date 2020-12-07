

Here is SQL schema for sensors.
    // 255 is an arbitrary max len number that should suite
    // most sensors
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

Here is the schema for users. Not sure if I want to add a GPS location column.
    // According to https://www.php.net/manual/en/function.password-hash.php
    // password hash is recommended to be 255 chars long

    // According to this https://stackoverflow.com/questions/1199190
    // emails are restricted to 254 chars
``` sql
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
)
```

```
type:
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


                3     7
number: 001 1101011
    first 3 bits = floor
    next 7 digits = number on that floor 
    Matlab style counting (Counting starts at 1)
    129 is first number for an upstairs sensor
```


