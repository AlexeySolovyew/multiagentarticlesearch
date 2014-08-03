<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
?>
<html>
<?php
include "../db.php";

$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row['RoleID'] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Интерфейс преподавателя</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
</head>
<table border=1>
    <tr>
        <td valign="top">
            <a href="hometasks.php">Домашние задания</a> <br/>
            <a href="tasks.php">Задачи</a> <br>
            <a href="tests.php">Тесты</a> <br>
            <a href="results.php">Результаты всех студентов</a><br/>
            <a href="../login.php?exit=true">Выйти</a>
        </td>
        <td>
            <body>
            <h2>Текущее домашнее задание:</h2><br>
            <br/>
            <?php
            $result = "0";
            if (isset($_POST['resultid'])) $result = $_POST['resultid'];
            if (isset($_POST['hometaskid'])) {
                if (mysql_query("UPDATE GeneralInfo SET Value=" . $_POST['hometaskid'] . " WHERE `Name`='CurrentHometaskID'")) {
                    echo "<font color=\"green\">Текущее д.з. успешно изменено.</font><br>";
                } else {
                    echo "<font color=\"red\">Ошибка. Пожалуйста, попробуйте позже.</font><br>";
                }
            }
            $info = mysql_query("SELECT * from GeneralInfo WHERE `Name`='CurrentHometaskID'");
            $currhtid = mysql_fetch_array($info);
            $hometask = mysql_query("SELECT * from Hometask WHERE HometaskID=" . $currhtid['Value']);
            $currhometask = mysql_fetch_array($hometask);
            echo "<b>" . $currhometask['HometaskID'] . " - " . $currhometask['Topic'] . "</b>";
            ?>
            <form action="index.php" method="POST">
                <p><select size="1" name="hometaskid">
                    <option selected disabled>Выберите домашнее задание</option>
                    <?php
                    $hts = mysql_query("SELECT HometaskID,Topic FROM Hometask");
                    $q = mysql_num_rows($hts);
                    for ($i = 0; $i < $q; $i++) {
                        $row = mysql_fetch_array($hts);
                        echo "<option value=" . $row['HometaskID'] . ">" . $row['HometaskID'] . " - " . $row['Topic'] . "</option>";
                    }
                    ?>
                </select>
                    <input type="submit" value="изменить">
            </form>
            <br/>
            Студентам доступны задачи из домашних заданий только до текущего <u>включительно</u>.
            <br>
            <br>
            <br>

            <h2 align="center">Таблица загруженных студентами решений</h2>

            <form action="index.php" method="POST">
                <p><select size="1" name="resultid">
                    <?php
                    $hts = mysql_query("SELECT * FROM Result");
                    $q = mysql_num_rows($hts);
                    for ($i = 0; $i < $q; $i++) {
                        $row = mysql_fetch_array($hts);
                        if (strcmp($result, $row['ResultID']) == 0) {
                            echo "<option selected value=\"" . $row['ResultID'] . "\">" . $row['Text'] . "</option>";
                        } else {
                            echo "<option value=\"" . $row['ResultID'] . "\">" . $row['Text'] . "</option>";
                        }
                    }
                    if ($result == null || strcmp($result, "-1") == 0) {
                        echo "<option selected value=\"-1\">Все</option>";
                    } else {
                        echo "<option value=\"-1\">Все</option>";
                    }
                    ?>
                </select>
                    <input type="submit" value="фильтровать">
            </form>
            <table border="1">
                <tr>
                    <td>
                        <b>Студент</b>
                    </td>
                    <td>
                        <b>Номер д.з.</b>
                    </td>
                    <td>
                        <b>Номер задачи в д.з.</b>
                    </td>
                    <td>
                        <b>Когда загружено</b>
                    </td>
                    <td>
                        <b>Результат</b>
                    </td>
                    <td>
                        <b>Проверка</b>
                    </td>
                    <td>
                        <b>Результат тестирования</b>
                    </td>
                </tr>
                <?php
                //запрос на множество пар задача - юзер
                $query = "SELECT DISTINCT UserID,TaskID FROM Solution JOIN Task USING (TaskID) JOIN `User` USING (UserID)";
                //echo $query;
                $solutions = mysql_query($query);
                $q = mysql_num_rows($solutions);
                for ($i = 0; $i < $q; $i++) {
                    $row = mysql_fetch_array($solutions);
                    $sqluser = "SELECT `Name`,Surname FROM User WHERE `UserID`='" . $row['UserID'] . "'";
                    //запрос на получение последнего решения, загруженного пользователем по данной задаче
                    $sqlsolution = "SELECT * FROM Solution JOIN Result USING (ResultID) JOIN Task USING (TaskID)
        WHERE TaskID=" . $row['TaskID'] . " AND UserID='" . $row['UserID'] . "' ORDER BY LoadTimestamp DESC";
                    //echo $sqlsolution;
                    //echo $sqluser;
                    $res = mysql_query($sqluser);
                    $rowuser = mysql_fetch_assoc($res);
                    $sol = mysql_query($sqlsolution);
                    $rowsol = mysql_fetch_assoc($sol);
                    if ($result == -1 || strcmp($result, $rowsol['ResultID']) == 0) {
                        echo "<tr><td>" . $rowuser[Name] . " " . $rowuser[Surname] . "</td>
        <td>" . $rowsol[HometaskID] . "</td><td>" . $rowsol[TaskForHometask] . "</td>
        <td>" . $rowsol[LoadTimestamp] . "</td><td>" . $rowsol[Text] . "</td>
        <td>" .
                            "<form action=\"check_solution.php\" method=\"POST\"><input type=\"hidden\" name=\"solutionid\" value=\"$rowsol[SolutionID]\">
        <input type=\"submit\" style=\"background: url(../img/check.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" . $rowsol[TestResult] . "</td>";
                    }
                }
                ?>
            </table>
        </td>
    </tr>
</table>


</body>
</html>
