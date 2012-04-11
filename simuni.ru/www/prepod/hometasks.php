<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <title>Все д.з. - курс ФП</title>
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
if ($_POST['topic'] != null) {
    //обработка запроса на редактирование задачи
    if ($_POST['hometaskid'] != null) {
        if (mysql_query("UPDATE Hometask SET Topic='".$_POST['topic']."',Deadline='".$_POST['dead']."' WHERE HometaskID=".$_POST['hometaskid'])) {
            echo "Д.з. успешно отредактировано.<br>";
        } else {
            echo "Ошибка.<br>";
        }

    } else {
        if (mysql_query("INSERT INTO Hometask (HometaskID,Topic,Deadline) VALUES (".($_POST['maxnum']+1).",'" . $_POST['topic'] . "','".$_POST['dead']."')")) {
            echo "Д.з. успешно добавлено.<br>";
        } else {
            echo "Д.з. не было добавлено.<br>";
        }
    }
}
//обработка запроса на удаление задачи
else if ($_POST['hometaskid'] != null) {
    if (mysql_query("DELETE FROM Hometask WHERE HometaskID=" . $_POST['hometaskid'])) {
        echo "Д.з. успешно удалено.<br>";
    } else {
        echo "Не удалось удалить д.з..<br>";
    }

}
?>
<table border="1">
    <caption>
        <h2>Список всех домашних заданий</h2>
    </caption>
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
        <td>".$row[Deadline]."</td>
        <td>" .
            "<form action=\"edit_hometask.php\" method=\"POST\"><input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" value=\"редактировать\"></form></td><td>" .
            "<form action=\"hometasks.php\" method=\"POST\"><input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" value=\"удалить\"></form></td></tr>";
    }
    ?>
</table>

<a href="add_hometask.php">Добавить ещё д.з.</a>
<br>
<a href="../index.php">На главную</a>
</body>
</form>
</html>