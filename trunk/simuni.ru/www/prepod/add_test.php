<?phpsession_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <title>Добавление теста - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
<b>Выберите параметры нового теста:</b>
<?php
$res = mysql_query("SELECT * FROM Task WHERE TaskID=".$_POST['taskid']);
$row=mysql_fetch_array($res);
if ($_POST['taskid'] != null) {
    echo "<p>Добавление нового теста к <b>".$row[TaskForHometask]."</b> задаче из <b>" . $row[HometaskID] . "</b> домашнего задания";
} else {
    echo "Не указан номер задачи, ахтунг";
}
?>
<form action="tests.php" method="post">

    <?php

    $maxnum = mysql_query("SELECT MAX(TestForTaskID) AS TestNum FROM Test WHERE TaskID=" . $_POST['taskid']);
    $row = mysql_fetch_array($maxnum);
    $testfortask = $row['TestNum'] + 1;
    echo "<p>Это будет уже <b>" . $testfortask . "</b> тест для данной задачи.<br>";
    echo "<input type=\"hidden\" name=\"testfortask\" value=\"" . $testfortask . "\">";
    echo "<input type=\"hidden\" name=\"taskid\" value=\"" . $_POST['taskid'] . "\">";

    ?>
    Выражение:
    <input name="expr"><br>
    Ожидаемое значение:
    <input name="val"><br>
    Хитрый тест:
    <input name="smart" type="checkbox"><br>
    <input type="submit" value="Добавить тест"><br>
</form>
<a href="index.php">На главную</a>
</html>