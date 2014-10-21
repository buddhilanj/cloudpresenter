

<?php
$host = "localhost";
$username = "root";
$password = "cluztertechnologies";
$databasename = "cloud";
if (!($connection = mysql_connect($host, $username, $password)))
    die('{"Error":"' . mysql_error() . '"}');
if (!mysql_select_db($databasename, $connection))
    die('{"Error":"Could not open ' . $databasename . ' database"}');
?>
