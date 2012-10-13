<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Добавление теста - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<table width="100%">
    <tr>
        <td align="right"><a href="tests.php">К тестам</a>
        <td>
        <td align="right"><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>

<h2 align="center">Страница добавления теста </h2>
<br/>
<?php
$res = mysql_query("SELECT * FROM Task WHERE TaskID=" . $_POST['taskid']);
$row = mysql_fetch_array($res);
if ($_POST['taskid'] != null) {
    echo "<p>Добавление нового теста к <b>" . $row[TaskForHometask] . "</b> задаче из <b>" . $row[HometaskID] . "</b> домашнего задания.";
} else {
    echo "Не указан номер задачи.";
}
?>
<form action="tests.php" method="post">

    <?php
    $cond = mysql_query("SELECT `Condition` FROM Task WHERE TaskID=" . $_POST['taskid']);
    //echo "SELECT Condition FROM Task WHERE TaskID=".$_POST['taskid'];
    $rowCond = mysql_fetch_array($cond);
    echo "<u>Условие:</u> <br/>" . htmlspecialchars(nl2br(htmlspecialchars($rowCond['Condition'])));
    echo "<br/><br/>";

    $maxnum = mysql_query("SELECT MAX(TestForTaskID) AS TestNum FROM Test WHERE TaskID=" . $_POST['taskid']);
    $row = mysql_fetch_array($maxnum);
    $testfortask = $row['TestNum'] + 1;
    echo "<p>Это будет уже <b>" . $testfortask . "</b> тест для данной задачи.<br>";
    echo "<input type=\"hidden\" name=\"testfortask\" value=\"" . $testfortask . "\">";
    echo "<input type=\"hidden\" name=\"taskid\" value=\"" . $_POST['taskid'] . "\">";

    ?>
    Выражение:<br/>
    <input name="expr" value=""><br>
    Ожидаемое значение:<br/>
    <input name="val" value=""><br>
    Хитрый тест:<br/>
    <input name="smart" type="checkbox"><br>
    Подсказка к хитрому тесту:<br/>
    <textarea name="help"></textarea><br>
    <input type="submit" value="Добавить тест"><br>
</form>
</html>