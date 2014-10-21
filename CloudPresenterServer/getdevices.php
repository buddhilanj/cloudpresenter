<?php
include'dbconn.php';
$user = $_GET["user"];
$arr = array();
$sqlfriends = "SELECT IF(u1.username='$user',u2.username,u1.username) AS username
                                   FROM users_has_friends uf
                                            LEFT JOIN users u1 ON u1.idusers = uf.users_idusers
                                            LEFT JOIN users u2 ON u2.idusers = uf.users_idusers1
                                    WHERE (u1.username = '$user' OR u2.username = '$user') AND uf.status='approved'";
$sqlrequests  = "SELECT u1.username AS username
                                    FROM users_has_friends uf
                                            LEFT JOIN users u1 ON u1.idusers = uf.users_idusers
                                            LEFT JOIN users u2 ON u2.idusers = uf.users_idusers1
                                    WHERE u2.username = '$user' AND uf.status='requests'";
$sqlusers = "SELECT * FROM users WHERE username NOT IN ($sqlfriends) AND username != '$user'";

if (!$user_results = mysql_query("$sqlusers")) {
    $arr["getdevices"] = "error";
    $arr["error"] = mysql_error();
} elseif(!isset($arr["error"]))  {
    $arr["getdevices"] = "successful";
    $arr["users"] = array();
    
    for ($i = 0; $userrow = mysql_fetch_array($user_results); $i++) {
        $arr["users"][$i] =array();
        $arr["users"][$i]["username"] = $userrow["username"];
    }
}
if (!$firend_results = mysql_query($sqlfriends)) {
    $arr["getdevices"] = "error";
    $arr["error"] = mysql_error();
} elseif(!isset($arr["error"])) {
    $arr["getdevices"] = "successful";
    $arr["friends"] = array();
    for ($i = 0; $friendrow = mysql_fetch_array($firend_results); $i++) {
        $arr["friends"][$i] =array();
        $arr["friends"][$i]["username"] = $friendrow["username"];
    }
}
if (!$request_results = mysql_query($sqlrequests)) {
    $arr["getdevices"] = "error";
    $arr["error"] = mysql_error();
} elseif(!isset($arr["error"])) {
    $arr["getdevices"] = "successful";
    $arr["requests"] = array();
    for ($i = 0; $requestrow = mysql_fetch_array($request_results); $i++) {
        $arr["requests"][$i] =array();
        $arr["requests"][$i]["username"] = $requestrow["username"];
    }
}
echo json_encode($arr);
?>
