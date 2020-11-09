<?php
    require_once 'session.php';
?>

<?php
    unset($_SESSION['username']);
    unset($_SESSION['sql_uid']);

    session_unset();
    session_destroy();
?>