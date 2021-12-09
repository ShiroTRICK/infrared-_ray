<?php
<<<<<<< Updated upstream
$conn = mysqli_connect('192.168.219.103','root','pi1234', "tests");
$sql  = "
    insert INTO users (username,pw) VALUES ('{$_POST['username']}', '{$_POST['pw']}') ";
=======
$conn = mysqli_connect('192.168.219.104','root','pi1234', "Mtemp");
$sql  = "
    insert INTO users (username,phone,address,vaccin) VALUES ('{$_POST['username']}', '{$_POST['phone']}', '{$_POST['address']}', '{$_POST['vaccin']}') ";
>>>>>>> Stashed changes
$result = mysqli_query($conn, $sql);
if($result === false){
    echo mysqli_error($conn);
}
else{
echo "1";
}
?>

<form method="POST" action="insert.php">
        이름: <input type="text" name="username"/><br/>
<<<<<<< Updated upstream
        비번: <input type="text" name="pw"/><br/>
=======
        전번: <input type="text" name="phone"/><br/>
        주소: <input type="text" name="address"/><br/>
        백신 여부: <input type="text" name="vaccin"/><br/>
>>>>>>> Stashed changes
        <input type="submit"/><br/>
    </form>
