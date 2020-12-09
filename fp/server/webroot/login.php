<?php
    require_once 'redirect.php';

    require_once 'session.php';
    require_once 'database.php';
?>

<?php

// Double-check that we are POSTING
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // https://stackoverflow.com/questions/1434368
    $jsonReply = new stdClass();

    // if the username is empty error out
    if (empty($_POST["username"])) {
        $jsonReply->error = array(
            'type' => "EmptyValue",
            'details' => "Name value was empty"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    // if the password is empty error out
    if (empty($_POST["password"])) {
        $jsonReply->error = array(
            'type' => "EmptyValue",
            'details' => "Password value was empty"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    // if the user wants to register then error cause we have yet to
    // implement it
    if (isset($_POST["register"]) && $_POST["register"] == "yes") {
        $jsonReply->error = array(
            'type' => "FeatureNotSupported",
            'details' => "Registration for this app is currently not supported"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    // extract the username and password
    $username = $_POST["username"];
    $password = $_POST["password"];

    # get the user
    if ($stmt = $conn->prepare("SELECT id,email,password FROM users WHERE username=? LIMIT 1")) {

        $stmt->bind_param('s', $username);
        if (!$stmt->execute()) {
            die('execute() failed: ' . $stmt->error);
        }

        # check that the user actually exists
        $stmt->store_result();
        if ($stmt->num_rows != 1) {
            $jsonReply->error = array(
                'type' => "NoSuchUser",
                'details' => "There is no user by that name"
            );
            
            echo json_encode($jsonReply);
    
            exit();
        }
        $id = ""; $email = ""; $hash = "";
        $stmt->bind_result($id, $email, $hash);

        $stmt->fetch();

        # check the password. if the password is incorrect then error out
        if (password_verify($password, $hash)) {
            $_SESSION["username"] = $username;
            $_SESSION["sql_uid"] = $id;

            $sid = session_id();

            $jsonReply->session = array(
                'uid' => "$id",
                'session_id' => "$sid",
                'email' => "$email"
            );

            $jsonReply->error = null;

            # echo the success json
            echo json_encode($jsonReply);

            exit();
        } else {
            $jsonReply->error = array(
                'type' => "IncorrectPassword",
                'details' => "That is the incorrect password for the user $username"
            );
            
            # echo the error json
            echo json_encode($jsonReply);
    
            exit();
        }
    }

}

?>