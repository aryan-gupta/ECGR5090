<?php
    require_once 'session.php';
?>

<html>
<body>
<?php
if (!isset($_SESSION["username"])) {
    echo '
    <script>
      function togglePasswordVisibility() {
        var x = document.getElementById("password");
        if (x.type === "password") {
          x.type = "text";
        } else {
          x.type = "password";
        }
      }
    </script>
    <form action="login.php" method="post">
        <input type="text" name="username" placeholder="Username"><br/>
        <input type="text" name="password" placeholder="Password" id="password"><br/>
        <input type="checkbox" onclick="togglePasswordVisibility()" checked>Show Password <br/>
        <input type="submit">
    </form>';
} else {
    $username = $_SESSION['username'];
    echo "Welcome $username";
    echo '<form action="sensors.php" method="post">';
    echo 'Opcode:';
    echo '<input type="radio" name="opcode" value="dump">Dump';
    echo '<input type="radio" name="opcode" value="request_id">Request ID';
    echo '<input type="radio" name="opcode" value="request">Request';
    echo '<input type="radio" name="opcode" value="update">Update';
    echo '<br/>';
    echo '<input type="submit">';
    echo '</form>';
    echo '<a href="logout.php">Logout</a>';
}
?>

</body>
</html>