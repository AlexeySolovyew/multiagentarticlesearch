<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<html>
<head>
    <title>Тесты - курс ФП</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
if ($_POST['val'] != null) {
    $smart = 0;
    if (strcmp($_POST['smart'], "on") == 0) {
        $smart = 1;
    }
    if ($_POST['testid'] == null) {
        //добавление нового теста
        /*echo "INSERT INTO Test (Expression,Result,TaskID,Smart,TestForTaskID,SmartHelp) VALUES (\"" . $_POST['expr'] . "\",\"" .
$_POST['val'] . "\",\"" . $_POST['taskid'] . "\",\""
. $smart . "\",\"" . $_POST['testfortask'] . "\",\"" . $_POST['help'] . "\")";*/
        if (mysql_query("INSERT INTO Test (Expression,Result,TaskID,Smart,TestForTaskID,SmartHelp) VALUES (\"" . $_POST['expr'] . "\",\"" .
            $_POST['val'] . "\",\"" . $_POST['taskid'] . "\",\""
            . $smart . "\",\"" . $_POST['testfortask'] . "\",\"" . $_POST['help'] . "\")")
        ) {
            echo "<div><font color=\"green\">Тест успешно добавлен.</font></div><br>";
        } else {
            echo "<div><font color=\"red\">Тест не был добавлен.</font></div><br>";
        }
    } else {
        //редактирование теста
        //echo "UPDATE Test SET Expression='" . $_POST['expr'] . "',Result='" . $_POST['val'] . "',TaskID='" . $_POST['taskid'] . "',
        //Smart='" . $smart . "' WHERE TestID=" . $_POST['testid'];
        if (mysql_query("UPDATE Test SET Expression='" . $_POST['expr'] . "',Result='" . $_POST['val'] . "',TaskID='" . $_POST['taskid'] . "',
        Smart='" . $smart . "',TestForTaskID='" . $_POST['testfortask'] . "',SmartHelp='" . $_POST['help'] . "' WHERE TestID=" . $_POST['testid'])
        ) {
            echo "<font color=\"green\">Тест успешно отредактирован.</font><br>";
        } else {
            echo "<font color=\"red\">Ошибка.</font><br/>";
        }

    }
} else if ($_POST['testid'] != null) {
    //удаление теста
    if (mysql_query("DELETE FROM Test WHERE TestID=" . $_POST['testid'])) {
        echo "<font color=\"green\">Тест успешно удален.</font><br>";
    } else {
        echo "<font color=\"red\">Не удалось удалить тест.</font><br>";
    }
}
?>
<table width="100%">
    <tr>
        <td>
            <form action="add_test.php" method="post">
                Задачи в списке отображаются в виде:<br> <b>{номер домашнего задания - номер задачи в этом задании}</b>
                <?php
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
        <td>
        <td><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>
<br/>

<h2 align="center">Таблица с тестами</h2>

<form action="tests.php" method="POST">
    <p><select size="1" name="taskid">
        <option value="-1">Для всех задач</option>
        <?php
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
    <tr>
        <td>
            <b>Номер домашнего задания</b>
        </td>
        <td>
            <b>Номер задачи в домашнем задании</b>
        </td>
        <td>
            <b>Номер теста для задачи</b>
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
            <b>Подсказка к хитрому</b>
        </td>
        <td>
            <b>Редактирование</b>
        </td>
        <td>
            <b>Удаление</b>
        </td>
    </tr>
    <?php
    $query = "SELECT * FROM Test JOIN Task USING (TaskID)";
    if (isset($_POST['taskid']) && $_POST['taskid'] != -1)
        $query = $query . " WHERE TaskID=" . $_POST['taskid'];
    $query = $query . " ORDER BY TaskID,TestForTaskID";
    $tests = mysql_query($query);
    $q = mysql_num_rows($tests);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tests);
        echo "<tr><td>" . $row[HometaskID] . "</td>
        <td>" . $row[TaskForHometask] . "</td><td>" . $row[TestForTaskID] . "</td><td>" . $row[Expression] . "</td><td>" . $row[Result] . "</td>
        <td>" . $row[Smart] . "</td><td>" . $row[SmartHelp] . "</td>
        <td>" .
            "<form action=\"edit_test.php\" method=\"POST\"><input type=\"hidden\" name=\"testid\" value=\"$row[TestID]\">
        <input type=\"submit\" style=\"background: url(../img/edit.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" .
            "<form action=\"tests.php\" method=\"POST\" onSubmit=\"return confirm('Вы уверены?');\">
            <input type=\"hidden\" name=\"testid\" value=\"$row[TestID]\">
            <input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" style=\"background: url(../img/delete.jpg); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td></tr>";
    }
    ?>
</table>
</body>
</html>