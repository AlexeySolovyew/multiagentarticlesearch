<?session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"index.php\">здесь</a>");

/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 15.03.12
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
mysql_connect("localhost", "root", "Phoenix");
mysql_select_db("simuni");
?>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>


    Загруженное рещение: <br>
    <pre><?
        $query = "SELECT * FROM Solution WHERE SolutionID=" . $_POST['solutionid'];
        $solution = mysql_query($query);
        $row = mysql_fetch_array($solution);
        $text = $row['Code'];
        //подсветка комментариев
        echo preg_replace("/---.*\n/","<font color=\"red\">\\0</font>",$text);
        ?></pre><br>
    <p>Результат: <?$hts = mysql_query("SELECT * FROM Result WHERE ResultID=".$row['ResultID']);
        $resultname = mysql_fetch_assoc($hts);
        echo $resultname['Text'];
        ?>
        <br>
     <p>Результат тестирования:<?echo $row[TestResult];?><br/>

<a href="succ.php">Назад</a>

</body>
</html>


