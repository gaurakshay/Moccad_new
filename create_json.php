<?php
header('Content-type: application/json');

$servername     =   'localhost' ;
$username       =   '???'      ;
$password       =   '???'     ;
$dbname         =   '???'    ;
$query			=	$_GET['query'];

echo("QUERY: ");
echo($query);

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn)
{
    die( 'Could not connect to service: ' . mysqli_error() );
}

$result     = mysqli_query($conn, $query);

$output = array();

while ( $row = mysqli_fetch_array($result) ) {
    array_push($output,array("Field"=>$row[0]));
}

mysqli_close($conn);
echo ( json_encode(array("server_response"=>$output) ) );

?>

