<?php session_start(); include "../db.php";
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
connect_db();

$queryyuser = "SELECT * FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Горячие задачи</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
</head>
<body>
<table width="100%">
    <tr>
        <td><a href="load.php">Загрузить решение</a></td>
        <td><a href="succ.php">Все загруженные решения</a></td>
        <td><a href="history.php">История задач</a></td>
        <td><a href="profile.php">Мой профиль</a></td>
        <td><a href="../login.php?exit=true">Выйти</a></td>
    </tr>
</table>
<h1>Здравствуйте, &nbsp  <?php  echo $row['Name']; ?>! </h1>

<div align="center"><h1>
    <?php
    $query = "SELECT DISTINCT TaskID FROM `User` JOIN Solution USING (UserID) JOIN Task USING (TaskID)
    WHERE ResultID=1 AND UserID='" . $_SESSION['user_id'] . "'";
    //echo $query;
    $querypoints = "SELECT SUM(Price) AS Priceall FROM Task WHERE TaskID IN (" . $query . ")";
    //echo $querypoints;
    $utasks = mysql_query($query);
    //посчитали к-во задачек юзверя
    $counttasks = mysql_num_rows($utasks);
    $uprice = mysql_query($querypoints);
    $array = mysql_fetch_array($uprice);
    //посчитали к-во баллов юзверя
    $balls = $array[Priceall];
    //ставим ноль вручную, если вообще ни одной зачтенной задачки
    if (!isset($balls)) $balls = "0";
    echo "Общее число баллов: <b>" . $balls . "</b>";
    ?>
</h1>
</div>
<br>


<table border="1">
    <caption>
        <h2>Горячие задачи*</h2><br>
    </caption>
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
        <td>
            <b>Загрузить решение</b>
        </td>

    </tr>
    <?php
    $query = "SELECT DISTINCT * FROM Task JOIN Hometask USING (HometaskID) WHERE HometaskID<=ALL
    (SELECT `Value` FROM GeneralInfo WHERE `Name`='CurrentHometaskID') AND Deadline>=NOW()";
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
            $resid = "239";
        } else {
            $rowresult = mysql_fetch_array($qrresult);
            $result = $rowresult['Text'];
            $resid = $rowresult['ResultID'];
        }
        //проверяем на зачтенность
        if (strcmp("1", $resid) != 0) {
            echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td>
        <td>" . nl2br(htmlspecialchars($row[Condition])) . "</td><td>" . $result . "</td>
        <td>" .
                "<form action=\"succ.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" class=\"search\" style=\"background: url(../img/check.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td><td>" . $row[Deadline] . "</td>
        <td>" .
                "<form action=\"load.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" class=\"search\" style=\"background: url(../img/download.png); height:50px; width:50px; line-height:12px;\" value=\"\"></form></td></tr>";
        }
    }
    ?>
</table>
<p>* отображены задачи, которые не зачтены, но крайний срок сдачи ещё не прошел<br/><br/>

<?php
include "../footer.php";
?>

</body>
</html>
