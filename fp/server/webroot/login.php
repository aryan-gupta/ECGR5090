<?php
    require_once 'session.php';
    require_once 'database.php';
?>

<?php

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // https://stackoverflow.com/questions/1434368
    $jsonReply = new stdClass();

    if (empty($_POST["username"])) {
        $jsonReply->error = array(
            'type' => "EmptyValue",
            'details' => "Name value was empty"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    if (empty($_POST["password"])) {
        $jsonReply->error = array(
            'type' => "EmptyValue",
            'details' => "Password value was empty"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    $username = $_POST["username"];
    $password = $_POST["password"];

    if ($stmt = $conn->prepare("SELECT id,email,password FROM users WHERE username=? LIMIT 1")) {
        
        $stmt->bind_param('s', $username);
        if (!$stmt->execute()) {
            die('execute() failed: ' . $stmt->error);
        }

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

            echo json_encode($jsonReply);

            exit();
        } else {
            $jsonReply->error = array(
                'type' => "IncorrectPassword",
                'details' => "That is the incorrect password for the user $username"
            );
            
            echo json_encode($jsonReply);
    
            exit();
        }
    }

}

?>