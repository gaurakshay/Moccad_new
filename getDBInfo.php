<?php
header('Content-type: application/json');
$servername     =   'localhost' ;
$username       =   'root'      ;
$password       =   'mysql'     ;
$dbname         =   'information_schema'    ;
$searchDB		=	'moccad';
$conn = mysqli_connect($servername, $username, $password, $dbname);
if (!$conn)
{
    die( 'Could not connect to service: ' . mysqli_error() );
}
//$query      = 'SELECT TABLE_NAME, COLUMN_NAME FROM `COLUMNS` WHERE TABLE_SCHEMA = "'.$searchDB.'"';
$query      = 'SELECT distinct TABLE_NAME FROM `COLUMNS` WHERE TABLE_SCHEMA = "'.$searchDB.'"';
$result     = mysqli_query($conn, $query);
if(!$result)
{
	echo(mysqli_error($conn));
	exit();
}
$output = array();
while ( $row = mysqli_fetch_array($result) ) {
    //array_push($output,array("TABLE_NAME"=>$row[0],"COLUMN_NAME"=>$row[1]));
    array_push($output,array("TABLE_NAME"=>$row[0]));
}
mysqli_close($conn);
echo ( json_encode(array("server_response"=>$output) ) );
?>
