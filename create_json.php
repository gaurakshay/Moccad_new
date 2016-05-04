<?php
include 'chooseQEP.php';
header('Content-type: application/json');

$servername     =	'localhost';
$username       =	'???';
$password       =	'???';
$dbname         =	'???';
$query		=	$_GET['query'];
$query		=	urldecode($query);
$time		=	$_GET['time'];
$money		=	$_GET['money'];
$power		=	$_GET['power'];
$logFile 	=	fopen("MOCCAD_LOG.txt", "a"); //Log file

$d = mktime();
fwrite($logFile, date("Y-m-d h:i:sa", $d)."\n");
fwrite($logFile, "QUERY: ".$query."\n");
fwrite($logFile, "TIME WEIGHT: ".$time."\n");
fwrite($logFile, "MONEY WEIGHT: ".$money."\n");
fwrite($logFile, "POWER WEIGHT: ".$power."\n");

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn)
{
    die( 'Could not connect to service: ' . mysqli_error() );
}

$result     = mysqli_query($conn, $query);
if(!$result)
{
	echo(mysqli_error($conn));
	echo($query);
	exit();
}

$output = array();

while ( $row = mysqli_fetch_array($result) ) {
    array_push($output,array("Field"=>$row[0]));
}

mysqli_close($conn);
$total = $time + $power + $money;
$time = $time / $total;
$money = $money / $total;
$power = $power / $total;
$sum = $time + $power + $money;

$estimationStack = generateRandomEstimations(10, $time, $money, $power);
$chosenEstimation = chooseQEP($estimationStack, $time, $money, $power);

fwrite($logFile, "Chosen Estimation: ".$chosenEstimation->{'estimationToString'}()."\n");

echo ( json_encode(array("server_response"=>$output) ) );

?>