<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
?>
<html>
<?php
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row['RoleID'] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<head>
    <title>Интерфейс преподавателя</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<table width="100%">
<tr>
<td align="right"><a href="../index.php">На главную</a><td>
</tr>
</table>
<br/>
<h2 align="center">Таблица с результатами</h2>
<br/>
<table border="1">
    
    <tr>
        <td>
            <b>Студент</b>
        </td>
        <td>
            <b>Номер группы</b>
        </td>
        <td>
            <b>Количество зачтенных задачек</b>
        </td>
        <td>
            <b>Количество баллов</b>
        </td>
    </tr>
    <?php
    $qusers = "SELECT * FROM `User` WHERE RoleID=1 ORDER BY Surname";
    $users = mysql_query($qusers);
    $countusers = mysql_num_rows($users);
    for ($i = 0; $i < $countusers; $i++) {
        $rowuser = mysql_fetch_array($users);
        $query = "SELECT DISTINCT TaskID FROM `User` JOIN Solution USING (UserID) JOIN Task USING (TaskID)
    WHERE ResultID=1 AND UserID='".$rowuser[UserID]."'";
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
		if (!isset($balls)) $balls="0";
        echo "<tr><td>" . $rowuser[Surname] . " " . $rowuser[Name] . "</td><td>" . $rowuser[GroupNumber] . "</td>
        <td>" . $counttasks . "</td><td>" . $balls . "</td></tr>";

    }
    ?>
</table>
</body>
</html>
