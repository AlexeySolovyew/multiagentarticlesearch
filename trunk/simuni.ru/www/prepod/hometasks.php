<?php session_start(); include "../db.php";
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Все домашние задания - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
connect_db();

$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
if ($_POST['topic'] != null) {
    //обработка запроса на редактирование задачи
    if ($_POST['hometaskid'] != null) {
        if (mysql_query("UPDATE Hometask SET Topic='" . $_POST['topic'] . "',Deadline='" . $_POST['dead'] . "' WHERE HometaskID=" . $_POST['hometaskid'])) {
            echo "<font color=\"green\">Домашнее задание успешно отредактировано.</font><br>";
        } else {
            echo "<font color=\"red\">Ошибка.</font><br>";
        }

    } else {
        if (mysql_query("INSERT INTO Hometask (HometaskID,Topic,Deadline) VALUES (" . ($_POST['maxnum'] + 1) . ",'" . $_POST['topic'] . "','" . $_POST['dead'] . "')")) {
            echo "<font color=\"green\">Домашнее задание успешно добавлено.</font><br>";
        } else {
            echo "<font color=\"red\">Домашнее задание не было добавлено.</font><br>";
        }
    }
} //обработка запроса на удаление задачи
else if ($_POST['hometaskid'] != null) {
    if (mysql_query("DELETE FROM Hometask WHERE HometaskID=" . $_POST['hometaskid'])) {
        echo "<font color=\"green\">Домашнее задание успешно удалено.</font><br>";
    } else {
        echo "<font color=\"red\">Не удалось удалить домашнее задание.</font><br>";
    }

}
?>
<table width="100%">
    <tr>
        <td><a href="add_hometask.php">Добавить домашнее задание</a>
        <td><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>
<br/>

<h2 align="center">Таблица с домашними заданиями</h2>
<table border="1">
    <tr>
        <td>
            <b>Номер д.з.</b>
        </td>
        <td>
            <b>Тема д.з.</b>
        </td>
        <td>
            <b>Набор задач</b>
        </td>
        <td>
            <b>Крайний срок сдачи</b>
        </td>
        <td>
            <b>Редактирование</b>
        </td>
        <td>
            <b>Удаление</b>
        </td>
    </tr>
    <?php
    $query = "SELECT * FROM Hometask ORDER BY HometaskID";
    $alltasks = mysql_query($query);
    $q = mysql_num_rows($alltasks);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($alltasks);
        echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[Topic] . "</td>
        <td>" .
            "<form action=\"tasks.php\" method=\"POST\"><input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" value=\"перейти к списку задач\"></form></td>
        <td>" . $row[Deadline] . "</td>
        <td>" .
            "<form action=\"edit_hometask.php\" method=\"POST\"><input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" style=\"background: url(../img/edit.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" .
            "<form action=\"hometasks.php\" method=\"POST\" onSubmit=\"return confirm('Вы уверены?');\"><input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" style=\"background: url(../img/delete.jpg); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td></tr>";
    }
    ?>
</table>
</body>
</form>
</html>