<?php
$conn = mysqli_connect('192.168.219.103','root','pi1234', "tests");
$sql  = "
    insert INTO users (username,pw) VALUES ('{$_POST['username']}', '{$_POST['pw']}') ";
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
        비번: <input type="text" name="pw"/><br/>
        <input type="submit"/><br/>
    </form>
