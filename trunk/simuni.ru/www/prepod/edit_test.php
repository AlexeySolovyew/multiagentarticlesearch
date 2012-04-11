<?phpsession_start();
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
    <title>Редактирование теста - курс ФП</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "Phoenix");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID]!=2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<b>Измените нужные поля:</b>

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
        Выражение:<br>
        <input name="expr" value="<?phpecho $currrow['Expression']?>"><br>
        Ожидаемое значение:<br>
        <input name="val" value="<?phpecho $currrow['Result']?>"><br>
        Хитрый?:<br>
        <input name="smart" type="checkbox" <?phpif ($currrow['Smart'] == 1) echo "checked"?>><br>
        <input type="hidden" name="testid" value="<?phpecho $_POST['testid']?>">

        <input type="submit" value="Принять изменения"><br>
</form>
</html>

