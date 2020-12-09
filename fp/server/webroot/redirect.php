<?php
    // redirects so the app redirects to the main page after 5 seconds
    // is not cached
    header('Content-type: application/json');
    header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
    header("Cache-Control: no-cache");
    header("Pragma: no-cache");
    header( "refresh:5;url=/" );
?>