<?php
    require_once 'session.php';
?>

<html>
<head>
    <style>
        /* https://css-tricks.com/exposing-form-fields-radio-button-css/ */
        .reveal-if-active {
            opacity: 0;
            max-height: 0;
            overflow: hidden;
        }

        input[type="radio"]:checked ~ .reveal-if-active,
        input[type="checkbox"]:checked ~ .reveal-if-active {
            opacity: 1;
            max-height: 200px;
            overflow: visible;
        }
    </style>
</head>
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
        <input type="checkbox" onclick="togglePasswordVisibility()" checked>Show Password<br/>
        <div>
            <input type="checkbox" name="register" value="yes">Register<br/>
            <div class="reveal-if-active">
                <input type="text" name="email" placeholder="Email"><br/>
            </div>
        </div>
        <input type="submit">
    </form>';
} else {
    $username = $_SESSION['username'];
    echo "Welcome $username";
    echo '<form action="sensors.php" method="post">';
    echo 'Opcode:';

    echo '
        <div>
            <input type="radio" name="opcode" value="dump" id="dump-opcode-input">Dump
        </div>';
    
    echo '
        <div>
            <input type="radio" name="opcode" value="search" id="request-id-opcode-input">Search
            <div class="reveal-if-active">
                <input type="text" name="type" placeholder="Type">
                <input type="text" name="name" placeholder="Name">
                <input type="text" name="floor" placeholder="Floor">
                <input type="text" name="number" placeholder="Number">
                <br/>
            </div>
        </div>';
    
    // https://stackoverflow.com/questions/7880619/
    echo '
        <div>
            <input type="radio" name="opcode" value="request" id="request-opcode-input">Request
            <div class="reveal-if-active">
                <input type="text" name="request_sensor_id" placeholder="Sensor ID">
                <br/>
            </div>
        </div>';

    echo '
        <div>
            <input type="radio" name="opcode" value="update" id="update-opcode-input">Update
            <div class="reveal-if-active">
                <input type="text" name="update_sensor_id" placeholder="Sensor ID">
                <input type="text" name="state" placeholder="State">
                <br/>
            </div>
        </div>';
    
    echo '
    <input type="submit">
    </form>
    <a href="logout.php">Logout</a>';
}
?>

</body>
</html>