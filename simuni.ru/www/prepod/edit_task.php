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
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Редактирование задачи - курс ФП</title>
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
        <td align="right"><a href="tasks.php">К задачам</a>
        <td>
        <td align="right"><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>

<h2 align="center">Страница редактирования задачи</h2>
<br>

<form action="tasks.php" method="post">

    Номер д.з.:<br>
    <?php
    $task = mysql_query("SELECT * from Task WHERE TaskID=" . $_POST['taskid']);
    $currrow = mysql_fetch_array($task);
    $hometasks = mysql_query("SELECT HometaskID,Topic FROM Hometask");
    $q = mysql_num_rows($hometasks);
    echo "<p><select size=\"1\" name=\"hometaskid\">";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($hometasks);
        if (strcmp($currrow['HometaskID'], $row['HometaskID']) == 0) {
            echo "<option selected value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - " . $row['Topic'] . "</option>";
        } else {
            echo "<option value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - " . $row['Topic'] . "</option>";
        }
    }
    echo "</select>";
    ?>
    <br>
    Номер задачи в д.з.:<br>
    <input name="taskforhometask" value="<?php echo $currrow['TaskForHometask']?>"><br>
    Условие:<br>
    <textarea cols="50" rows="10" name="condition"><?php echo htmlspecialchars($currrow['Condition'])?></textarea><br>
    <input type="hidden" name="taskid" value="<?php echo $_POST['taskid']?>">
    Цена:<br>
    <input name="price" value="<?php echo $currrow['Price']?>"><br/>
    <input type="submit" value="Принять изменения"><br>
</form>
</html>

