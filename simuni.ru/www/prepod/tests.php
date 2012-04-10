<?session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
mysql_connect("localhost", "root", "Phoenix");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID]!=2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<html>
<head>
    <title>Тесты - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?
if ($_POST['val'] != null) {
    $smart = 0;
    if (strcmp($_POST['smart'], "on") == 0) {
        $smart = 1;
    }
    if ($_POST['testid'] == null) {
        //добавление нового теста
        if (mysql_query("INSERT INTO Test (Expression,Result,TaskID,Smart,TestForTaskID) VALUES (\"" . $_POST['expr'] . "\",\"" .
            $_POST['val'] . "\",\"" . $_POST['taskid'] . "\",\""
            . $smart . "\",\"" . $_POST['testfortask'] . "\")")
        ) {
            echo "<div>Тест успешно добавлен.</div><br>";
        } else {
            echo "<div>Тест не был добавлен.</div><br>";
        }
    } else {
        //редактирование теста
        //echo "UPDATE Test SET Expression='" . $_POST['expr'] . "',Result='" . $_POST['val'] . "',TaskID='" . $_POST['taskid'] . "',
        //Smart='" . $smart . "' WHERE TestID=" . $_POST['testid'];
        if (mysql_query("UPDATE Test SET Expression='" . $_POST['expr'] . "',Result='" . $_POST['val'] . "',TaskID='" . $_POST['taskid'] . "',
        Smart='" . $smart . "' WHERE TestID=" . $_POST['testid'])
        ) {
            echo "Тест успешно отредактирован.<br>";
        } else {
            echo "Ошибка.<br>";
        }

    }
} else if ($_POST['testid'] != null) {
    //удаление теста
    if (mysql_query("DELETE FROM Test WHERE TestID=" . $_POST['testid'])) {
        echo "Тест успешно удален.<br>";
    } else {
        echo "Не удалось удалить тест.<br>";
    }
}
?>
<form action="tests.php" method="POST">
    <p><select size="1" name="taskid">
        <option value="-1">Для всех задач</option>
        <?
        $tasks = mysql_query("SELECT TaskID,HometaskID,TaskForHometask FROM Task");
        $q = mysql_num_rows($tasks);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($tasks);
            if (strcmp($_POST['taskid'], $row['TaskID']) == 0) {
                echo "<option selected value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
            } else {
                echo "<option value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
            }
        }
        ?>
    </select>
        <input type="submit" value="фильтровать">
</form>
<table border="1">
    <caption>
        <h2>Список тестов</h2>
    </caption>
    <tr>
        <td>
            <b>Номер д.з.</b>
        </td>
        <td>
            <b>Номер задачи в д.з.</b>
        </td>
        <td>
            <b>Выражение</b>
        </td>
        <td>
            <b>Ожидаемое значение</b>
        </td>
        <td>
            <b>Хитрый</b>
        </td>
        <td>
            <b>Редактирование</b>
        </td>
        <td>
            <b>Удаление</b>
        </td>
    </tr>
    <?
    $query = "SELECT * FROM Test JOIN Task USING (TaskID)";
    if (isset($_POST['taskid']) && $_POST['taskid']!=-1)
        $query = $query . " WHERE TaskID=" . $_POST['taskid'];
    $query = $query." ORDER BY TaskID";
    $tests = mysql_query($query);
    $q = mysql_num_rows($tests);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tests);
        echo "<tr><td>" . $row[HometaskID] . "</td>
        <td>" . $row[TaskForHometask] . "</td><td>" . $row[Expression] . "</td><td>" . $row[Result] . "</td><td>" . $row[Smart] . "</td>
        <td>" .
            "<form action=\"edit_test.php\" method=\"POST\"><input type=\"hidden\" name=\"testid\" value=\"$row[TestID]\">
        <input type=\"submit\" value=\"редактировать\"></form></td><td>" .
            "<form action=\"tests.php\" method=\"POST\"><input type=\"hidden\" name=\"testid\" value=\"$row[TestID]\">
        <input type=\"submit\" value=\"удалить\"></form></td></tr>";
    }
    ?>
</table>
<br>
<br>

<form action="add_test.php" method="post">
    Перейти к добавлению нового теста для задачи ({номер д.з. - номер задачи в д.з.}):
    <?
    $tasks = mysql_query("SELECT * FROM Task");
    $q = mysql_num_rows($tasks);
    echo "<p><select size=\"1\" name=\"taskid\">";
    echo "<option disabled>Выберите задачу</option>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        if (strcmp($_POST['taskid'], $row['TaskID']) == 0) {
            echo "<option selected value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        } else {
            echo "<option value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        }

    }
    echo "</select>";
    ?>
    <br>
    <input type="submit" value="Добавить новый тест">
</form>
<br>
<a href="../index.php">На главную</a>
</body>
</html>