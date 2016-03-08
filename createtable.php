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
echo ( 'Selecting DB ' .  $dbname . '...<br>' );
mysql_select_db($dbname, $conn);
echo ( 'DB selected. <br>' );
echo ( 'Generating the SQL...<br>' );
$query = 'CREATE TABLE patientlog (' .
            'log_id     INT( 8 ) NOT NULL AUTO_INCREMENT, ' .
            'pat_id     INT( 5 ) NOT NULL, ' .
            'doc_id     INT( 5 ) NOT NULL, ' .
            'date       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, ' .
            'diagnosis  VARCHAR( 100 ) NOT NULL, ' .
            'PRIMARY KEY( log_id )' .
            ')';
echo ( 'SQL generated<br>' );
echo ( $query . '<br>' );

$execStat = mysql_query( $query, $conn );

echo ( 'Query executed.<br>' );

if (! execStat) {
    die( 'Error while creating table: ' . mysql_error() );
}
echo ( 'Table creation successful.<br>' );

mysql_close($conn);

?>
