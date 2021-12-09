<?php
<<<<<<< Updated upstream
$con=mysqli_connect("192.168.219.103","root","pi1234","tests");

$sql = "
    select * from users where username =  ('{$_POST['username']}') ";
=======
$con=mysqli_connect("192.168.219.104","root","pi1234","Mtemp");

$sql = "
    select * from users where phone =  ('{$_POST['phone']}') ";
>>>>>>> Stashed changes
mysqli_set_charset($con,"utf8");
$res = mysqli_query($con, $sql);
$result = array();

while($row = mysqli_fetch_array($res)){
<<<<<<< Updated upstream
    array_push($result,array('username'=>$row[0], 'pw'=>$row[1]));
=======
    array_push($result,array('username'=>$row[0], 'phone'=>$row[1], 'address' => $row[2], 'vaccin' => $row[3]));
>>>>>>> Stashed changes
}

echo json_encode(array("result"=>$result));
mysqli_close($con);
?>

<form method="POST" action="select.php">
<<<<<<< Updated upstream
        이름: <input type="text" name="username"/><br/>
=======
        폰번: <input type="text" name="phone"/><br/>
>>>>>>> Stashed changes
<input type="submit"/><br/>
    </form>
