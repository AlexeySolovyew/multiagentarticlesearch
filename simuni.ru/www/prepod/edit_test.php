<?php session_start(); include "../db.php";
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
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Редактирование теста - курс ФП</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
connect_db();

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

<h2 align="center">Страница редактирования теста</h2>
<br/>

<form action="tests.php" method="post">

    Номер задачи:<br>

    <p><select size="1" name="taskid">
        <?php
        $test = mysql_query("SELECT * from Test WHERE TestID=" . $_POST['testid']);
        $currrow = mysql_fetch_array($test);
        $tasks = mysql_query("SELECT TaskID,HometaskID,TaskForHometask FROM Task");
        $q = mysql_num_rows($tasks);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($tasks);
            if (strcmp($currrow['TaskID'], $row['TaskID']) == 0) {
                echo "<option selected value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
            } else {
                echo "<option value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
            }
        }?>
    </select>
        <br>
        Номер теста для задачи:<br>
        <input name="testfortask" value="<?php echo $currrow['TestForTaskID']?>"><br>
        Выражение:<br>
        <input name="expr" size="150" value="<?php echo htmlspecialchars($currrow['Expression'])?>"><br>
        Ожидаемое значение:<br>
        <input name="val" value="<?php echo $currrow['Result']?>"><br>
        Хитрый?:<br>
        <input name="smart" type="checkbox" <?php if ($currrow['Smart'] == 1) echo "checked"?>><br>
        Подсказка к хитрому:<br>
        <textarea name="help"><?php echo $currrow['SmartHelp']?></textarea><br>
        <input type="hidden" name="testid" value="<?php echo $_POST['testid']?>">

        <input type="submit" value="Принять изменения"><br>
</form>
</html>

