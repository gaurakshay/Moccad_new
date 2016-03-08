<?php
header('Content-type: application/json');

$servername     =   'localhost' ;
$username       =   'root'      ;
$password       =   'mysql'     ;
$dbname         =   'moccad'    ;

$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn)
{
    die( 'Could not connect to service: ' . mysqli_error() );
}

$query      = 'SELECT * FROM patientlog;';
$result     = mysqli_query($conn, $query);

$output = array();

while ( $row = mysqli_fetch_array($result) ) {
//    echo $row[1];
//    $output[] = $row;
//    array_push($output,array("log_id"=>$row["log_id"],
//        "pat_id"=>$row["pat_id"],
//        "doc_id"=>$row["doc_id"],
//        "log_date"=>$row["log_date"],
//        "diagnosis"=>$row["diagnosis"]));
    array_push($output,array("log_id"=>$row[0],"pat_id"=>$row[1],"doc_id"=>$row[2],"log_date"=>$row[3],"diagnosis"=>$row[4]));
}

mysqli_close($conn);
echo ( json_encode(array("server_response"=>$output) ) );

?>

