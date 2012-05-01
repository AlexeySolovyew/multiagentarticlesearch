<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Добавление задачи - курс ФП</title>
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
<table width="100%">
<tr>
<td align="right"><a href="tasks.php">К задачам</a><td>
<td align="right"><a href="../index.php">На главную</a><td>
</tr>
</table>
<br/>
<h2 align="center">Страница добавления задачи</h2>
<br>
<br>
<form action="tasks.php" method="post">

    Номер д.з.:<br>
    <?php
    $tasks = mysql_query("SELECT HometaskID,Topic FROM Hometask");
    $q = mysql_num_rows($tasks);
    echo "<p><select size=\"1\" name=\"hometaskid\">";
    echo "<option disabled>Номер домашнего задания</option>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        echo "<option value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - ".$row['Topic']."</option>";
    }
    echo "</select>";
    ?>
    <br>
    Номер задачи в д.з.:<br>
    <input name="taskforhometask"><br>
    Условие:<br>
    <textarea cols="50" rows="10" name="condition"></textarea><br>
    Цена задачи:<br>
    <input name="price"><br>
    <input type="submit" value="Добавить задачу"><br>
</form>
</html>