<?php
include 'dbconn.php';
$user = $_POST['user'];
$friend = $_POST['friend'];
$status = $_POST['status'];
$sql = "";
$error = "";
$arr = array();
switch ($status) {
    case "requests":
        $sql = "INSERT INTO users_has_friends
                            (users_idusers, users_idusers1, status)
                            VALUES
                            (
                                    (	SELECT idusers
                                            FROM users
                                            WHERE username='$user'
                                    ),
                                    (	SELECT idusers
                                            FROM users
                                            WHERE username='$friend'
                                    ),
                                    '$status'
                            )";
        break;
    case "approved":
        $sql = "UPDATE users_has_friends
                            SET status = '$status'
                            WHERE
                                users_idusers = (	SELECT idusers
                                        FROM users
                                        WHERE username='$friend'
                                )
                                AND
                                users_idusers1 = (	SELECT idusers
                                        FROM users
                                        WHERE username='$user'
                                )";
        break;
    case "rejected":
        $sql = "UPDATE users_has_friends
                            SET status = '$status'
                            WHERE
                                users_idusers = (
                                        SELECT idusers
                                        FROM users
                                        WHERE username='$friend'
                                )
                                AND
                                users_idusers1 = (
                                        SELECT idusers
                                        FROM users
                                        WHERE username='$user'
                                )";
        break;
    case "delete":
        $sql = "DELETE FROM users_has_friends
                            WHERE
                                users_idusers = (
                                        SELECT idusers
                                        FROM users
                                        WHERE username='$user'
                                )
                                AND
                                users_idusers1 = (
                                        SELECT idusers
                                        FROM users
                                        WHERE username='$friend'
                                )";
        break;
    default:
        $arr["friendreq"] = "error";
        $arr["error"] = "status :'$status' not supported";
}
if (!empty($sql) && !mysql_query($sql)) {
    $arr["friendreq"] = "error";
    $arr["error"] = mysql_error();
} else if (!empty($sql)) {
    $arr["friendreq"] = "successful";
}
echo json_encode($arr);
?>
