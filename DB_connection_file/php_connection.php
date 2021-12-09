<?php
$con=mysqli_connect("192.168.219.104","root","pi1234","Mtemp");


mysqli_set_charset($con,"utf8");
$res = mysqli_query($con, "select * from users");
$result = array();

while($row = mysqli_fetch_array($res)){
    array_push($result,array('username'=>$row[0], 'pw'=>$row[1]));
}

echo json_encode(array("result"=>$result));
mysqli_close($con);
?>