<?php
    require_once 'redirect.php';
    
    require_once 'session.php';
    require_once 'database.php';
?>
<?php
function get_sensor_dump(&$conn) {
    $sql_uid = $_SESSION['sql_uid'];

    $stmt = $conn->prepare("SELECT * FROM sensors WHERE userid=?");
    if (!$stmt) {
        die('statement creation failed failed: ' . $stmt->error);
    }

    $stmt->bind_param('i', $sql_uid);
    if (!$stmt->execute()) {
        die('execute() failed: ' . $stmt->error);
    }

    $result = $stmt->get_result();

    $rows = array();
    while($r = $result->fetch_assoc()) {
        $rows[] = $r;
    }

    return json_encode($rows);
}

function get_sensor_dump_by_id(&$conn, $sen_id) {
    $jsonReply = new stdClass();
    $sql_uid = $_SESSION['sql_uid'];

    $stmt = $conn->prepare("SELECT * FROM sensors WHERE userid=? AND id=? LIMIT 1");
    if (!$stmt) {
        die('statement creation failed failed: ' . $stmt->error);
    }

    $stmt->bind_param('ii', $sql_uid, $sen_id);
    if (!$stmt->execute()) {
        die('execute() failed: ' . $stmt->error);
    }

    $result = $stmt->get_result();
    if ($result->num_rows < 1) {
        $jsonReply->error = array(
            'type' => "SensorNotFound",
            'details' => "Sensor by ID $sen_id was not found"
        );
        
        echo json_encode($jsonReply);

        exit();
    }

    return json_encode($result->fetch_assoc());
}


function update_sensor_value(&$conn, $sen_id, $state) {
    // Create connection
    global $db_servername,$db_username,$db_password,$db_name,$_SESSION;

    error_log("Creating Connection");
    $localconn = new mysqli($db_servername,$db_username,$db_password);

    // Check connection
    if ($localconn->connect_error) {
        die("Connection failed: " . $localconn->connect_error);
    }

    // check for database
    error_log("Selecting database");
    $db_selected = $localconn->select_db($db_name);
    if (!$db_selected) {
        die("Failed");
    }


    $sql_uid = $_SESSION['sql_uid'];

    error_log("Preparing Statement");
    $stmt = $localconn->prepare("UPDATE sensors SET state=? WHERE userid=? AND id=?");
    if (!$stmt) {
        die('statement creation failed failed: ' . $stmt->error);
    }
    
    $stmt->bind_param('iii', $state, $sql_uid, $sen_id);
    error_log("Executing statement");
    if (!$stmt->execute()) {
        die('execute() failed: ' . $stmt->error);
    }

    $ret = get_sensor_dump_by_id($localconn, $sen_id);
    return $ret;
}

function update_devices($ret) {
    $reply = json_decode($ret, true);
    $reply['opcode'] = 'device';
    $reply = json_encode($reply);

    error_reporting(E_ALL);
    $address = "192.168.1.19";
    $service_port = 9080;

    error_log("Socket creation OK");

    /* Create a TCP/IP socket. */
    $socket = socket_create(AF_INET, SOCK_STREAM, 0) or die("Could not create socket\n");
    if ($socket === false) {
        echo "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
    } else {
        error_log("Socket creation OK asdasd");
    }

    $result = socket_connect($socket, $address, $service_port);
    if ($result === false) {
        echo "socket_connect() failed.\nReason: ($result) " . socket_strerror(socket_last_error($socket)) . "\n";
    }

    $msg = strval(strlen($reply)) . "\n" . $reply;

    socket_write($socket, $msg, strlen($msg));
    socket_close($socket);
}

function search_sensor(&$conn, $type, $name, $number) {
    $sql_uid = $_SESSION['sql_uid'];

    // possible solution for this madness using bind param, but I have no freaking
    // clue what its doing: https://stackoverflow.com/questions/17479110

    $sql = "SELECT * FROM sensors WHERE userid=$sql_uid ";
    if ($type != null) {
        $sql .= " AND type LIKE '%$type%' ";
    }

    if ($name != null) {
        $sql .= " AND name LIKE '%$name%' ";
    }

    if ($number != null) {
        $sql .= " AND number=$number ";
    }

    $result = $conn->query($sql);

    $rows = array();
    while($r = $result->fetch_assoc()) {
        $rows[] = $r;
    }

    return json_encode($rows);
}


$post = $_POST;
$opcode = $post["opcode"];

/// @todo CLEAN THE INPUTS SQL INJECTION MUCH?????
switch ($opcode) {
    case "dump":
        echo get_sensor_dump($conn);
    break;

    case "search":
        /// @todo convert floors and number to actual number
        echo search_sensor($conn, $post["type"], $post["name"], $post["number"]);
    break;

    /// @TODO because forms have multiple inputs with the same name,
    ///       this needs to be updated so it works with JSON or with POST 
    case "request":
        echo get_sensor_dump_by_id($conn, $post["request_sensor_id"]);
    break;

    case "update":
        $ret = update_sensor_value($conn, $post["update_sensor_id"], $post["state"]);
        update_devices($ret);
        echo $ret;
    break;

    default:
        $jsonReply = new stdClass();
        $jsonReply->error = array(
            'type' => "IncorrectOpcode",
            'details' => "Opcode $opcode is not supported",
            'post' => $post
        );
        
        echo json_encode($jsonReply);

        exit();
}

?> 