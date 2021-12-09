<?php
$con=mysqli_connect("192.168.219.103","root","pi1234","tests");

$sql = "
    select * from users where username =  ('{$_POST['username']}') ";
mysqli_set_charset($con,"utf8");
$res = mysqli_query($con, $sql);
$result = array();

while($row = mysqli_fetch_array($res)){
    array_push($result,array('username'=>$row[0], 'pw'=>$row[1]));
}

echo json_encode(array("result"=>$result));
mysqli_close($con);
?>

<form method="POST" action="select.php">
        이름: <input type="text" name="username"/><br/>
<input type="submit"/><br/>
    </form>
