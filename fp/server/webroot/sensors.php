<?php
    require_once 'session.php';
    require_once 'database.php';
?>
<?php
function get_sensor_dump(&$conn) {
    $jsonReply = new stdClass();
    $sql_uid = $_SESSION['sql_uid'];

    $stmt = $conn->prepare("SELECT * FROM sensors WHERE userid=?");
    if (!$stmt) {
        die('statement creation failed failed: ' . $stmt->error);
    }

    $stmt->bind_param('s', $sql_uid);
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

$jsonReply = new stdClass();

$post = null;
if (isset($_POST["json"])) {
    $post = json_decode($_POST["json"]);
} else {
    $post = $_POST;
}

$opcode = $post["opcode"];

switch ($opcode) {
    case "dump":
        echo get_sensor_dump($conn);
    break;

    case "request_id":

    break;

    case "request":

    break;

    case "update":

    break;

    default:
        $jsonReply->error = array(
            'type' => "IncorrectOpcode",
            'details' => "Opcode $opcode is not supported"
        );
        
        echo json_encode($jsonReply);

        exit();
}

?> 