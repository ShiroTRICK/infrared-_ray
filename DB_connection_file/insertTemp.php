<?php
$conn = mysqli_connect('192.168.219.104','root','pi1234', "Mtemp");
$sql  = "
    insert INTO usertemp (phone) VALUES ('{$_POST['phone']}') ";
$result = mysqli_query($conn, $sql);
if($result === false){
    echo mysqli_error($conn);
}
else{
echo "1";
}
?>

<form method="POST" action="insertTemp.php">
        전번: <input type="text" name="phone"/><br/>
        <input type="submit"/><br/>
    </form>
