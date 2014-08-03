<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"index.php\">здесь</a>");
include "../db.php";

?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<table width="100%">
    <tr>
        <td align="right"><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>
<br/>

<h2 align="center">Загруженные решения</h2>
<br/>

<form action="succ.php" method="POST">
    <p><select size="1" name="taskid">
        <option value="-1">Для всех задач</option>
        <?php
        $tasks = mysql_query("SELECT TaskID,HometaskID,TaskForHometask FROM Task
        WHERE HometaskID <= ALL (SELECT Value FROM GeneralInfo WHERE `Name`='CurrentHometaskID')");
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
            <b>Номер задачи в задании</b>
        </td>
        <td>
            <b>Время загрузки</b>
        </td>
        <td>
            <b>Результат</b>
        </td>
        <td>
            <b>Подробнее</b>
        </td>
        <td>
            <b>Результат тестирования</b>
        </td>
    </tr>
    <?php
    $query = "SELECT DISTINCT * FROM Solution JOIN Task USING (TaskID) JOIN Result USING (ResultID)
     JOIN `User` USING (UserID) WHERE UserID='" . $_SESSION['user_id'] . "'";
    //обрабатываем фильтр
    if (isset($_POST['taskid']) && $_POST['taskid'] != -1) $query = $query . " AND Task.TaskID=" . $_POST['taskid'];
    $query = $query . " ORDER BY LoadTimestamp DESC";
    // echo $_SESSION['user_id'];
    // echo $query;
    $solutions = mysql_query($query);
    $q = mysql_num_rows($solutions);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($solutions);
        echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td>
        <td>" . $row[LoadTimestamp] . "</td><td>" . $row[Text] . "</td>
        <td>" .
            "<form action=\"watch_solution.php\" method=\"POST\"><input type=\"hidden\" name=\"solutionid\" value=\"$row[SolutionID]\">
        <input type=\"submit\" class=\"search\" style=\"background: url(../img/check.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" . $row[TestResult] . "</td></tr>";
    }
    ?>
    <br/>

</table>
</body>
</html>