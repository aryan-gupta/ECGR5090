<?php
    require_once "first_run.php";
?>

<?php

$db_servername = "mariadb";
$db_username = "phpadmin";
$db_name = "sense49";
$db_password = file_get_contents("/run/secrets/db_${db_username}_password");

// Create connection
$conn = new mysqli($db_servername,$db_username,$db_password);

require_once 'first_run.php';

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// check for database
$db_selected = $conn->select_db($db_name);
if (!$db_selected) {
    first_run_setup($conn, $db_name);
}

?>