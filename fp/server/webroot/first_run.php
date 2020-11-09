<?php
function first_run_setup(&$conn, $db_name) {
    // if ($conn->query($sql) !== TRUE) {
    //     die("Error creating database: " . $conn->error);
    // }

    // create database
    $sql = "CREATE DATABASE $db_name";
    if ($conn->query($sql) !== TRUE) {
        die("Error creating database: " . $conn->error);
    }

    // reselect database and fail if can't
    $db_selected = $conn->select_db($db_name);
    if (!$db_selected) {
        die("Error selecting database: " . $conn->error);
    }

    // create Sensor database table
    $sql = ("CREATE TABLE sensors (" .
        "id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," .
        "userid INT UNSIGNED NOT NULL," .
        "type ENUM(" .
            "'system'," .
            "'garage_door'," .
            "'thermostat_mode'," .
            "'thermostat_fan'," .
            "'thermostat_temp'," .
            "'thermostat_set_temp'," .
            "'lights_level'," .
            "'lock_state'," .
            "'door_window_sensor'," .
            "'motion_sensor'" .
        ") NOT NULL," .
        // 255 is an arbitrary max len number that should suite
        // most sensors
        "name VARCHAR(255) NOT NULL," .
        "number INT(10) UNSIGNED NOT NULL," .
        "state INT NOT NULL" .
    ")");
    if ($conn->query($sql) !== TRUE) {
        die("Error creating sensors table: " . $conn->error);
    }

    // create User database table
    $sql = ("CREATE TABLE users (" .
        "id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," .
        "username VARCHAR(50) NOT NULL," .
        // According to this https://stackoverflow.com/questions/1199190
        // emails are restricted to 254 chars
        "email VARCHAR(255) NOT NULL," .
        // According to https://www.php.net/manual/en/function.password-hash.php
        // password hash is recommended to be 255 chars long
        "password VARCHAR(255) NOT NULL" .
    ")");
    if ($conn->query($sql) !== TRUE) {
        die("Error creating users table: " . $conn->error);
    }

    // create default test user
    $app_test_user_password = file_get_contents("/run/secrets/app_test_user_password");
    $hash = password_hash($app_test_user_password, PASSWORD_DEFAULT);
    $sql = "INSERT INTO users (username,email,password) VALUES ('test','test@example.com','$hash')";
    if ($conn->query($sql) !== TRUE) {
        die("Error creating test user: " . $conn->error);
    }

    // get uid from SQL
    $sql = "SELECT id FROM users WHERE username='test' LIMIT 1";
    $result = $conn->query($sql);
    if ($result->num_rows == 0) {
        die("Error user not found: " . $conn->error);
    }

    $uid = $result->fetch_object()->id;

    // create default sensors
    // We cant create a large SQL statement here because of packet
    // limitations
    $sql = ("INSERT INTO sensors " .
        "(userid,type,name,number,state) VALUES " .
        "(?,?,?,?,?)"
    );
    $type = ""; $name = ""; $number = 0; $state = 0;
    $stmt = $conn->prepare($sql);
    if (!$stmt->bind_param('issii', $uid, $type, $name, $number, $state)) {
        echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
    }

    $default_sensor_list = yaml_parse_file("/run/secrets/default_sensors");
    //var_dump($default_sensor_list);

    foreach ($default_sensor_list as $sen) {
        $type = $sen["type"];
        $name = $sen["name"];
        $number = $sen["number"];
        $state = $sen["state"];

        if (!$stmt->execute()) {
            die('execute() failed: ' . $stmt->error);
        }
    }
}
?>