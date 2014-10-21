<?php
include 'dbconn.php';
$username = $_POST['user'];
$password = $_POST['pass'];

$sql = "INSERT INTO users (username, password) VALUES ('$username','$password')";
$arr = array();
if(!mysql_query($sql)){
    $arr["signup"] = "error";
    $arr["error"]=mysql_error();
}
else{
$arr["signup"]="successful";
}
echo json_encode($arr);
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

?>
