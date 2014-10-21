<?php

session_start();
include 'dbconn.php';
//print_r($_SESSION);
if(isset($_SESSION['user'])){
$username = $_SESSION['user'];
} else if(isset($_POST['user'])){
  $username  = $_POST['user'];
} else{
    $username ="";
}
if (empty($username) || !mysql_query("UPDATE users SET currentip='' WHERE username = '$username'")) {
    if(empty($username)){
        die('{"logout":"error","Error":"No User Defined"}');
    }
    die('{"logout":"error","Error":"' . mysql_error() . '"}');
}
session_destroy();
echo '{"logout":"successful"}';
?>
