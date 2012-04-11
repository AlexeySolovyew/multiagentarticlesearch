<?phpsession_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
mysql_connect("localhost", "root", "Phoenix");
mysql_select_db("simuni");
?>
<html>
<head>
    <title>Интерфейс студента</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<h1>Здравствуйте, <?php echo $_SESSION['user_id']; ?>! </h1><br>
<a href="load.php">Загрузить решение</a> <br>
<a href="succ.php">Все загруженные решения</a> <br>
<a href="index.php?exit=true">Выйти</a>

<table border="1">
    <caption>
        <h2>Ваши успехи:</h2>
    </caption>
    <tr>
        <td>
            <b>Номер д.з.</b>
        </td>
        <td>
            <b>Номер задачи в д.з.</b>
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
    (SELECT `Value` FROM GeneralInfo WHERE `Name`='CurrentHometaskID')";
    //обрабатываем фильтр
    $query=$query." ORDER BY HometaskID DESC";
    // echo $_SESSION['user_id'];
    // echo $query;
    $tasks = mysql_query($query);
    $q = mysql_num_rows($tasks);
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        $qr = "SELECT ResultID FROM Solution WHERE TaskID=".$row[TaskID]." AND UserID='".$_SESSION['user_id']."'";
        $qrresult = mysql_query($qr);
        $count = mysql_num_rows($qrresult);
        $result = "Не проверена";
        if ($count==0) $result="Ещё не загружалось ни одного решения";
        for ($j = 0; $j<$count;$j++){
            $rowresult = mysql_fetch_array($qrresult);
            if ($rowresult[ResultID]==3) {
                $result="Списана";
                break;
            } elseif ($rowresult[ResultID]==1){
                $result="Зачтена";
                break;
            } elseif ($rowresult[ResultID] == 2){
                $result="Проверена, но не зачтена";
            }
        }
        echo "<tr><td>" . $row[HometaskID] . "</td><td>" . $row[TaskForHometask] . "</td>
        <td>".$row[Condition]."</td><td>" . $result . "</td>
        <td>" .
            "<form action=\"succ.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" value=\"загруженные решения задачи\"></form></td><td>" . $row[Deadline] . "</td>
        <td>" .
            "<form action=\"load.php\" method=\"POST\"><input type=\"hidden\" name=\"taskid\" value=\"$row[TaskID]\">
        <input type=\"submit\" value=\"загрузить\"></form></td></tr>";
    }
    ?>

</body>
</html>
