<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 09.03.12
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
?>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Добавление д.з. - курс ФП</title>
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
<b>Введите данные о новом задании:</b>
<form action="hometasks.php" method="post">

    Введите тему нового домашнего задания:
    <input name="topic"><br>
    <?php
    $maxnum = mysql_query("SELECT MAX(HometaskID) AS Num FROM Hometask");
    $row = mysql_fetch_array($maxnum);
    ?>
    <input type="hidden" name="maxnum" value="<?php echo $row['Num']?>">
    Дедлайн:
    <input name="dead"> (ГГГГ-ММ-ДД)<br>
    <input type="submit" value="Добавить д.з."><br>
</form>
</html>