<?php

$servername = 'localhost';
$username   = 'root';
$password   = 'mysql';
$dbname     = 'moccad';

//create connection
$conn = mysql_connect($servername, $username, $password);

if ( ! $conn ) {
    die( "Could not connect: " . mysql_error() );
}

echo ( 'Connection successful.<br>' ); 
echo ( 'Creating database "' . $dbname . '"...<br>' );

$query = "CREATE DATABASE " . $dbname;

echo $query . '<br>';

if (mysql_query($query, $conn)) {
    echo ( 'Database successfully created. <br>' );
}
else {
    echo ( 'Database couldn\'t be created. <br>' );
    echo ( 'Error details:<br>' );
    echo ( mysql_error() );
}

mysql_close($conn);

?>
