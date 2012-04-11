<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 09.03.12
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
?>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Редактирование задачи - курс ФП</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID]!=2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<b>Измените нужные поля:</b>

<form action="hometasks.php" method="post">

    Номер д.з.:<br>
    <?php
    $task = mysql_query("SELECT * from Hometask WHERE HometaskID=" . $_POST['hometaskid']);
    $currrow = mysql_fetch_array($task);
    echo $currrow['HometaskID'];
    ?>
    <br>
    Тема:<br>
    <input name="topic" value="<?php echo $currrow['Topic']?>"><br>
    Крайний срок сдачи:<br>
    <input name="dead" value="<?php echo $currrow['Deadline']?>"><br>
    <input type="hidden" name="hometaskid" value="<?php echo $_POST['hometaskid']?>">
    <input type="submit" value="Принять изменения"><br>
</form>
</html>

