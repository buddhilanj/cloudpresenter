<?php

session_start();
include 'dbconn.php';
$arr = array();
if (isset($_GET['user'])) {
    $username = $_GET['user'];
    if (isset($_GET['currentip'])) {
        $currentip = $_GET['currentip'];
    } else {
        $currentip = "";
    }
    if (!mysql_query("UPDATE users SET currentip='$currentip' WHERE username = '$username'")) {
        $arr["updatefriends"] = "error";
        $arr["Error"] = mysql_error();
    }

    $sql = "SELECT
                    IF(u1.username='$username',u2.username,u1.username) AS username ,
                    IF(u1.username='$username',u2.currentip,u1.currentip) AS currentip,
                    uf.status AS fstatus
                FROM users_has_friends uf
                    LEFT JOIN users u1 ON u1.idusers = uf.users_idusers
                    LEFT JOIN users u2 ON u2.idusers = uf.users_idusers1
                 WHERE (u1.username = '$username' OR u2.username = '$username') AND (uf.status='approved' OR (uf.status='rejected' AND u1.username='$username'))";
    if (!$friend_result = mysql_query($sql)) {
        $arr["updatefriends"] = "error";
        $arr["Error"] = mysql_error();
    }
    $arr["updatefriends"] = "successful";
    $arr["friends"] = array();
    for ($i = 0; $friendrow = mysql_fetch_array($friend_result); $i++) {
        $arr["friends"][$i]["username"] = $friendrow['username'];
        $arr["friends"][$i]["currentip"] = $friendrow['currentip'];
        $arr["friends"][$i]["status"] = $friendrow['fstatus'];
    }
}
echo json_encode($arr);
?>
