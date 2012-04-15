<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 10.03.12
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
?>
<h2>Загрузка решения</h2>

<form action="upload.php" method="post" enctype="multipart/form-data">
    Номер задачи: <br>
    <?php
    $tasks = mysql_query("SELECT * FROM Task WHERE HometaskID <= ALL
    (SELECT `Value` FROM GeneralInfo WHERE `Name`='CurrentHometaskID' )");
    $q = mysql_num_rows($tasks);
    echo "<p><select size=\"1\" name=\"tasknum\">";
    echo "<option disabled>Выберите задачу</option>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        if (isset($_POST['taskid']) && strcmp($_POST['taskid'],$row['TaskID'])==0) {
            $cond = $row['Condition'];
            echo "<option selected value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        } else {
            echo "<option value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        }
    }
    echo "</select><br>";
    if (isset($cond)) echo "Условие: <br/>".$cond."<br/><br/>";
    ?>
    Файл с задачей: <br>
    <input type="file" name="filename"><br>
    или<br>
    Текст задачи: <br>
    <textarea type="text" rows="10" cols="50" maxlength="1000" name="code"></textarea><br>
    <input type="submit" value="Загрузить и начать тестирование"><br>
</form>
<a href="index.php">Назад</a>
<br>
</body>
</html>