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
$timeEst	=	$_GET['timeEst'];
$powerEst	=	$_GET['powerEst'];
$logFile 	=	fopen("MOCCAD_LOG.txt", "a"); //Log file

$total = $time + $power + $money;
$time = $time / $total;
$money = $money / $total;
$power = $power / $total;
$sum = $time + $power + $money;

$d = mktime();
fwrite($logFile, date("Y-m-d h:i:sa", $d)."\n");
fwrite($logFile, "QUERY: ".$query."\n");
fwrite($logFile, "TIME WEIGHT: ".$time."\n");
fwrite($logFile, "MONEY WEIGHT: ".$money."\n");
fwrite($logFile, "POWER WEIGHT: ".$power."\n");

fwrite($logFile, "MOBILE ESTIMATION PARAMETERS: \n");
$mobileEstimation = new randomEstimation;
$mobileEstimation ->objMoney = 0;
$mobileEstimation ->objTime = $timeEst;
$mobileEstimation ->objPower = $powerEst;
$mobileEstimation ->score = ($time * ($mobileEstimation->objTime / 10)) + ($money * ($mobileEstimation->objMoney / .10))
				+ ($power * ($mobileEstimation->objPower / .5));
fwrite($logFile, "\t".$mobileEstimation->{'estimationToString'}()."\n");

$estimationStack = generateRandomEstimations(20, $time, $money, $power);
array_push($estimationStack, $mobileEstimation);
$chosenEstimation = chooseQEP($estimationStack, $time, $money, $power);

fwrite($logFile, "Chosen Estimation: ".$chosenEstimation->{'estimationToString'}()."\n");

if($chosenEstimation->objTime == $mobileEstimation->objTime &&
   $chosenEstimation->objMoney == $mobileEstimation->objMoney &&
   $chosenEstimation->objPower == $mobileEstimation->objPower &&
   $chosenEstimation->score == $mobileEstimation->score)
{
	fwrite($logFile, "Running query on mobile device.\n");
	echo ("ROMD");
	//ROMD (Run on mobile device) is a flag for the mobile device to process the query.
}
else
{
	fwrite($logFile, "Running query on cloud.\n");
	$conn = mysqli_connect($servername, $username, $password, $dbname);

	if (!$conn)
	{
    		die( 'Could not connect to service: ' . mysqli_error() );
	}

	$result = mysqli_query($conn, $query);
	if(!$result)
	{
		echo(mysqli_error($conn));
		echo($query);
		exit();
	}

	$output = array();
	
	$i = 0;
	while ( $row = mysqli_fetch_array($result) ) {
    		array_push($output,array("Field".$i=>$row[0]));
		$i++;
	}

	mysqli_close($conn);

	echo ( json_encode(array("server_response"=>$output) ) );
}

?>