<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
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
include "../db.php";

?>
<table width="100%">
    <tr>
        <td align="right"><a href="index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>
<br/>

<h2 align="center">Загрузка решения</h2>
<br/>

<form action="upload.php" method="post" enctype="multipart/form-data">
    <u>Номер задачи:</u> <br>
    <?php
    $tasks = mysql_query("SELECT * FROM Task WHERE HometaskID <= ALL
    (SELECT `Value` FROM GeneralInfo WHERE `Name`='CurrentHometaskID' )");
    $q = mysql_num_rows($tasks);
    echo "<p><select size=\"1\" name=\"tasknum\">";
    echo "<option disabled>Выберите задачу</option>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasks);
        if (isset($_POST['taskid']) && strcmp($_POST['taskid'], $row['TaskID']) == 0) {
            $cond = nl2br(htmlspecialchars($row['Condition']));
            echo "<option selected value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        } else {
            echo "<option value=" . $row['TaskID'] . ">" . $row['HometaskID'] . " - " . $row['TaskForHometask'] . "</option>";
        }
    }
    echo "</select><br>";
    if (isset($cond)) echo "<u>Условие:</u> <br/>" . $cond . "<br/><br/>";
    ?>
    <br/>
    Пожалуйста, выберите вариант формата загружаемого решения (необходимо выбрать ровно один из вариантов)
    <br/><br/>
    <u>Файл с задачей:</u> (поддерживаемые форматы: *.hs, *.txt, *.lhs) <br>
    <input type="file" name="filename"><br>
    или<br>
    <u>Текст задачи:</u> <br>
    <textarea type="text" rows="10" cols="50" maxlength="1000" name="code"></textarea><br>
    <input type="submit" value="Загрузить и начать тестирование"><br>
</form>
<br>
</body>
</html>