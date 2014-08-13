<?php session_start(); include "../db.php";
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
connect_db();

?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Интерфейс студента</title>
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

<h2 align="center">История выполнения заданий</h2>
<br/>

<form action="history.php" method="POST">
    <p><select size="1" name="resid">
        <option value="-1">Все результаты</option>
        <?php
        $filter = "1";
        //if (isset($_POST['resid']) {
        if ($_POST['resid'] != null) {
            $filter = $_POST['resid'];
        }
        $tasks = mysql_query("SELECT ResultID,Text FROM Result");
        $q = mysql_num_rows($tasks);
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($tasks);
            if (strcmp($filter, $row['ResultID']) == 0) {
                echo "<option selected value=" . $row['ResultID'] . ">" . $row['Text'] . "</option>";
            } else {
                echo "<option value=" . $row['ResultID'] . ">" . $row['Text'] . "</option>";
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
            <b>Условие</b>
        </td>
        <td>
            <b>Результат</b>
        </td>
        <td>
            <b>Посмотреть загруженные решения</b>
        </td>
        <td>
            <b>Срок сдачи</b>
        </td>

    </tr>
    <?php
    $query = "SELECT DISTINCT * FROM Task JOIN Hometask USING (HometaskID) WHERE HometaskID<=ALL
    (SELECT `Value` FROM GeneralInfo WHERE `Name`='CurrentHometaskID')";
    //обрабатываем фильтр
    $query = $query . " ORDER BY HometaskID DESC,TaskForHometask ASC";
    // echo $_SESSION['user_id'];
    // echo $query;
    $tasks = mysql_query($query);
    $q = mysql_num_rows($tasks);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        $qr = "SELECT ResultID, Text FROM Solution JOIN Result USING (ResultID) WHERE TaskID=" . $row[TaskID] . " AND UserID='" . $_SESSION['user_id'] . "' ORDER BY LoadTimestamp DESC";
        $qrresult = mysql_query($qr);
        $count = mysql_num_rows($qrresult);
        if ($count == 0) {
            $result = "Ещё не загружалось ни одного решения";
            $id = "239";
        } else {
            $rowresult = mysql_fetch_array($qrresult);
            $result = $rowresult['Text'];
            $id = $rowresult['ResultID'];
        }
        //фильтрация:
        //echo $filter;
        if ($filter == -1 || strcmp($filter, $id) == 0) {
            echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td>
        <td>" . nl2br(htmlspecialchars($row[Condition])) . "</td><td>" . $result . "</td>
        <td>" .
                "<form action=\"succ.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" class=\"search\" style=\"background: url(../img/view.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" . $row[Deadline] . "</td>
        </tr>";
        }
    }
    ?>

</body>
</html>
