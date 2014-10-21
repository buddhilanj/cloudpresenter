<?php

session_start();
include 'dbconn.php';
if (isset($_POST['user']) && isset($_POST['pass'])) {
    $username = $_POST['user'];
    $password = $_POST['pass'];
    if (isset($_POST['currentip'])) {
        $currentip = $_POST['currentip'];
    } else {
        $currentip = "";
    }
    $authenticated = false;
    if (!$user_results = mysql_query("SELECT * FROM users WHERE username = '$username'")) {
        die('{"login":"error","Error":"' . mysql_error() . '"}');
    }
    while ($row = mysql_fetch_array($user_results)) {
        $authenticated = ($password == $row['password']);
        if (!mysql_query("UPDATE users SET currentip='$currentip' WHERE username = '$username'")) {
            die('{"login":"error","Error":"' . mysql_error() . '"}');
        }
    }
    if ($authenticated) {
        $_SESSION['user'] = $username;
        $sql = "SELECT
                    IF(u1.username='$username',u2.username,u1.username) AS username , 
                    IF(u1.username='$username',u2.currentip,u1.currentip) AS currentip,
                    uf.status AS fstatus
                FROM users_has_friends uf 
                    LEFT JOIN users u1 ON u1.idusers = uf.users_idusers 
                    LEFT JOIN users u2 ON u2.idusers = uf.users_idusers1
                 WHERE (u1.username = '$username' OR u2.username = '$username') AND (uf.status='approved' OR (uf.status='rejected' AND u1.username='$username'))";
        if (!$friend_result = mysql_query($sql)) {
            die('{"login":"error","Error":"' . mysql_error() . '"}');
        }
        echo '{"login":"suceessfull","friends":[';
        $once = TRUE;
        while ($friendrow = mysql_fetch_array($friend_result)) {
            if (!$once) {
                echo ',';
            } else {
                $once = FALSE;
            }
            echo '{"username":"' . $friendrow['username'] . '","currentip":"' . $friendrow['currentip'] . '","status":"' . $friendrow['fstatus'] . '"}';
        }
        echo ']}';
    } else {
        echo'{"login":"failed"}';
    }
}
?>
