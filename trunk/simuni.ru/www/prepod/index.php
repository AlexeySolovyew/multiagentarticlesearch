<?phpsession_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
?>
<html>
<?php
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row['RoleID']!=2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<head>
    <title>Интерфейс преподавателя</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<h2>Текущее домашнее задание:</h2><br>
Студенту показаны задачи из д.з. только до текущего включительно.
<br/>
<?php
if (isset($_POST['hometaskid'])){
    if (mysql_query("UPDATE GeneralInfo SET Value=".$_POST['hometaskid']." WHERE `Name`='CurrentHometaskID'")) {
        echo "Текущее д.з. успешно изменено.<br>";
    } else {
        echo "Ошибка.<br>";
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
<br>
<a href="hometasks.php">Домашние задания</a> <br/>
<a href="tasks.php">Задачи</a> <br>
<a href="tests.php">Тесты</a> <br>
<a href="results.php">Результаты всех студентов</a><br/>
<a href="index.php?exit=true">Выйти</a>
<br>
<br>
<form action="index.php" method="POST">
    <p><select size="1" name="resultid">
        <?php
        $hts = mysql_query("SELECT * FROM Result");
        $q = mysql_num_rows($hts);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($hts);
            if (strcmp($_POST['resultid'], $row['ResultID']) == 0) {
                echo "<option selected value=\"" . $row['ResultID'] . "\">" . $row['Text'] . "</option>";
            } else {
                echo "<option value=\"" . $row['ResultID'] . "\">" . $row['Text'] . "</option>";
            }
        }
        if ($_POST['resultid'] == null || strcmp($_POST['resultid'], "-1") == 0) {
            echo "<option selected value=\"-1\">Все</option>";
        } else {
            echo "<option value=\"-1\">Все</option>";
        }
        ?>
    </select>
        <input type="submit" value="фильтровать">
</form>
<table border="1">
    <caption>
        <h2>Загруженные решения:</h2>
    </caption>
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
    $result = $_POST['resultid'];
    $query = "SELECT DISTINCT * FROM Solution JOIN Task USING (TaskID) JOIN Result USING (ResultID)";
    if ($result != null && $result != -1) {
        $query = $query . " WHERE ResultID=" . $result;
    }
    $query=$query." ORDER BY LoadTimestamp DESC";
    //echo $query;
    $solutions = mysql_query($query);
    $q = mysql_num_rows($solutions);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($solutions);
        $sqluser = "SELECT `Name`,Surname FROM User WHERE `UserID`='" . $row['UserID'] . "'";
        //echo $sqluser;
        $res = mysql_query($sqluser);
        $rowuser = mysql_fetch_assoc($res);
        echo "<tr><td>" . $rowuser[Name] . " " . $rowuser[Surname] . "</td>
        <td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td>
        <td>".$row[LoadTimestamp]."</td><td>" . $row[Text] . "</td>
        <td>" .
            "<form action=\"check_solution.php\" method=\"POST\"><input type=\"hidden\" name=\"solutionid\" value=\"$row[SolutionID]\">
        <input type=\"submit\" value=\"проверить\"></form></td><td>" . $row[TestResult] . "</td>";
    }
    ?>
</table>



</body>
</html>
