<?session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <title>Все задачи - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?
mysql_connect("localhost", "root", "Phoenix");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID]!=2) die("Студенту нельзя лезть к материалам преподавателя!");

if ($_POST['taskforhometask'] != null) {
    //обработка запроса на редактирование задачи
    if ($_POST['taskid'] != null) {
        //echo "UPDATE Task SET TaskForHometask=".$_POST['taskforhometask'].",HometaskID=".$_POST['hometaskid'].",`Condition`=".$_POST['condition']."
        //WHERE TaskID=".$_POST['taskid'];
        if (mysql_query("UPDATE Task SET TaskForHometask=".$_POST['taskforhometask'].",HometaskID=".$_POST['hometaskid']."
        ,`Condition`='".$_POST['condition']."
        ',Price=".$_POST['price']." WHERE TaskID=".$_POST['taskid'])) {
            echo "Задача успешно отредактирована.<br>";
        } else {
            echo "Ошибка.<br>";
        }

    } else {
        //echo "INSERT INTO Task (TaskForHometask,HometaskID,Condition) VALUES (\"" . $_POST['taskforhometask'] . "\",\"" . $_POST['hometaskid'] . "\",
        //\"" . $_POST['condition'] . "\")";
        //обработка запроса на добавление новой задачи
        if (mysql_query("INSERT INTO Task (TaskForHometask,HometaskID,`Condition`,Price) VALUES (\"" . $_POST['taskforhometask'] . "\",\"" . $_POST['hometaskid'] . "\",
    '" . $_POST['condition'] . "',".$_POST['price'].")"
        )
        ) {
            echo "Задача успешно добавлена.<br>";
        } else {
            echo "Задача не была добавлена.<br>";
        }
    }
}
//обработка запроса на удаление задачи
else if ($_POST['taskid'] != null) {
    if (mysql_query("DELETE FROM Task WHERE TaskID=" . $_POST['taskid'])) {
        echo "Задача успешно удалена.<br>";
    } else {
        echo "Не удалось удалить задачу.<br>";
    }

}
?>
<form action="tasks.php" method="POST">
    <p><select size="1" name="hometaskid">
        <option value="-1">Для всех домашних заданий</option>
        <?
        $hts = mysql_query("SELECT HometaskID,Topic FROM Hometask");
        $q = mysql_num_rows($hts);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($hts);
            if (strcmp($_POST['hometaskid'],$row['HometaskID'])==0){
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
    <caption>
        <h2>Список задач</h2>
    </caption>
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
    <?
    $query = "SELECT * FROM Task";
    if (isset($_POST['hometaskid']) && $_POST['hometaskid'] != -1)
        $query = $query . " WHERE HometaskID=" . $_POST['hometaskid'];
    $query = $query." ORDER BY HometaskID,TaskForHometask";
    $alltasks = mysql_query($query);
    $q = mysql_num_rows($alltasks);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($alltasks);
        echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td><td>" . $row[Condition] . "</td>
        <td>" .
            "<form action=\"tests.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" value=\"тесты по задаче\"></form></td><td>".$row[Price]."</td>
        <td>" .
            "<form action=\"edit_task.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" value=\"редактировать\"></form></td><td>" .
            "<form action=\"tasks.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" value=\"удалить\"></form></td></tr>";
    }
    ?>
</table>

<a href="add_task.php">Добавить задачу</a>
<br>
<a href="../index.php">На главную</a>
</body>
</form>
</html>