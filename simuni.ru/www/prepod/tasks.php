<?php session_start(); include "../db.php";
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Все задачи - курс ФП</title>
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

if ($_POST['taskforhometask'] != null) {
    //обработка запроса на редактирование задачи
    if ($_POST['taskid'] != null) {
        //echo "UPDATE Task SET TaskForHometask=".$_POST['taskforhometask'].",HometaskID=".$_POST['hometaskid'].",`Condition`=".$_POST['condition']."
        //WHERE TaskID=".$_POST['taskid'];
        if (mysql_query("UPDATE Task SET TaskForHometask=" . $_POST['taskforhometask'] . ",HometaskID=" . $_POST['hometaskid'] . "
        ,`Condition`='" . $_POST['condition'] . "
        ',Price=" . $_POST['price'] . " WHERE TaskID=" . $_POST['taskid'])
        ) {
            echo "<font color=\"green\">Задача успешно отредактирована.</font><br>";
        } else {
            echo "<font color=\"red\">Ошибка.</font><br>";
        }

    }
} //обработка запроса на удаление задачи
else if ($_POST['taskid'] != null) {
    if (mysql_query("DELETE FROM Task WHERE TaskID=" . $_POST['taskid'])) {
        echo "<font color=\"green\">Задача успешно удалена.</font><br>";
    } else {
        echo "<font color=\"red\">Не удалось удалить задачу.</font><br>";
    }

}
?>
<table width="100%">
    <tr>
        <td>
            <form action="add_task.php" method="post">

                <?php
                $tasks = mysql_query("SELECT * FROM Hometask");
                $q = mysql_num_rows($tasks);
                echo "<p><select size=\"1\" name=\"hometaskid\">";
                echo "<option disabled>Выберите задание</option>";
                for ($i = 0; $i < $q; $i++) {
                    $row = mysql_fetch_array($tasks);
                    if (strcmp($_POST['hometaskid'], $row['HometaskID']) == 0) {
                        echo "<option selected value=" . $row['HometaskID'] . ">" . $row['Topic'] . "</option>";
                    } else {
                        echo "<option value=" . $row['HometaskID'] . ">" . $row['Topic'] . "</option>";
                    }

                }
                echo "</select>";
                ?>
                <br>
                <input type="submit" value="Добавить задачу">
            </form>
        <td>
        <td><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>
<br/>

<h2 align="center">Таблица с задачами</h2>

<form action="tasks.php" method="POST">
    <p><select size="1" name="hometaskid">
        <option value="-1">Для всех домашних заданий</option>
        <?php
        $hts = mysql_query("SELECT HometaskID,Topic FROM Hometask");
        $q = mysql_num_rows($hts);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($hts);
            if (strcmp($_POST['hometaskid'], $row['HometaskID']) == 0) {
                echo "<option selected value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - " . $row['Topic'] . "</option>";
            } else {
                echo "<option value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - " . $row['Topic'] . "</option>";
            }
        }
        ?>
    </select>
        <input type="submit" value="фильтровать">
</form>
<table border="1">
    <tr>
        <td>
            <b>Номер д.з.</b>
        </td>
        <td>
            <b>Номер задачи в д.з.</b>
        </td>
        <td>
            <b>Условие задачи</b>
        </td>
        <td>
            <b>Тесты</b>
        </td>
        <td>
            <b>Цена</b>
        </td>
        <td>
            <b>Редактирование</b>
        </td>
        <td>
            <b>Удаление</b>
        </td>
    </tr>
    <?php
    $query = "SELECT * FROM Task";
    if (isset($_POST['hometaskid']) && $_POST['hometaskid'] != -1)
        $query = $query . " WHERE HometaskID=" . $_POST['hometaskid'];
    $query = $query . " ORDER BY HometaskID,TaskForHometask";
    $alltasks = mysql_query($query);
    $q = mysql_num_rows($alltasks);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($alltasks);
        echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td><td>" . nl2br(htmlspecialchars($row[Condition])) . "</td>
        <td>" .
            "<form action=\"tests.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" style=\"background: url(../img/view.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" . $row[Price] . "</td>
        <td>" .
            "<form action=\"edit_task.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" style=\"background: url(../img/edit.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" .
            "<form action=\"tasks.php\" method=\"POST\" onSubmit=\"return confirm('Вы уверены?');\">
            <input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
            <input type=\"hidden\" name=\"hometaskid\" value=\"$row[HometaskID]\">
        <input type=\"submit\" style=\"background: url(../img/delete.jpg); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td></tr>";
    }
    ?>
</table>

</body>
</form>
</html>