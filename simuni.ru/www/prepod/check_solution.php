<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 15.03.12
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='".$_SESSION['user_id']."'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID]!=2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/jquery.terminal.css"/>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="../js/jquery.mousewheel-min.js"></script>
    <script type="text/javascript" src="../js/jquery.terminal-0.4.11.js"></script>
</head>
<body>
<?php
if ($_POST['code'] != null) {
    $query = "UPDATE Solution SET Code='" . $_POST['code'] . "',ResultID=" . $_POST['resultid'] . " WHERE SolutionID=" . $_POST['solutionid'];
    if (mysql_query($query)) {
        echo "<font color=\"green\">Решение студента успешно отредактировано.</font><br>";
    } else {
        echo "<font color=\"red\">Ошибка.</font><br>";
    }
}
$query = "SELECT * FROM Solution JOIN Task USING (TaskID) WHERE SolutionID=" . $_POST['solutionid'];
$solution = mysql_query($query);
$row = mysql_fetch_array($solution);
?>
<form action="check_solution.php" method="POST">
<table width="100%">
<tr>
<td>
<u>Статус решения:</u>
<select size="1" name="resultid">
        <?php
        $hts = mysql_query("SELECT * FROM Result");
        $q = mysql_num_rows($hts);
        for ($i = 0; $i < $q; $i++) {
            $tmprow = mysql_fetch_array($hts);
            if (strcmp($row['ResultID'], $tmprow['ResultID']) == 0) {
                echo "<option selected value=\"" . $tmprow['ResultID'] . "\">" . $tmprow['Text'] . "</option>";
            } else {
                echo "<option value=\"" . $tmprow['ResultID'] . "\">" . $tmprow['Text'] . "</option>";
            }
        }
        ?>
    </select>
</td>
<td>
<input type="submit" value="Сохранить изменения">
</td>
<td><form action="add_test.php" method="post">
    <input type="hidden" name="taskid" value="<?php echo $row[TaskID]?>">
    <input type="submit" value="Новый тест для этой задачи">
</form></td>
<td><a href="../index.php">На главную</a></td>
</tr>
</table>
<br/>
<h2 align = "center">Работа с присланным решением</h2>
<br/>
    <input type="hidden" name="solutionid" value="<?php echo $_POST['solutionid'];?>">
    <u>Задача:</u> <br/><?php echo $row[HometaskID]." - ".$row[TaskForHometask]?><br/><br/>
    <u>Условие:</u> <br/><?php echo $row[Condition]?><br/><br/>
    <u>Текст решения*:</u> <br>
	* комментарии, помеченные "---", будут выделяться цветом в системе студента</br>
    <textarea id="code" rows="15" cols="115" name="code"><?php echo $row['Code'];?></textarea><br>
</form>


<!-- консоль -->
<div id="term"></div>
<script type="text/javascript">
    jQuery(document).ready(function ($) {
        $('#term').terminal(function (command, term) {
            if (command === 'p') {
                term.pause('Paused...');
            } else if (command !== '') {
                term.pause();
                jQuery.ajax({
                    type:"get",
                    url:"haskell.php",
                    data:{code:$('#code').val(), cmd:command},
                    timeout:60000,
                    error:function () {
                        term.echo("Cant evaluate expression :(");
                        term.resume();
                    },
                    success:function (data) {
                        term.echo(data);
                        term.resume();
                    }
                });
            }
            else {
                term.echo('Please enter a command');
            }
        }, {
            greetings:'Here you can evaluate expressions\nType \"p\" for editing code of the solution, when edited, press button below',
            name:'haskell',
            height:200,
            prompt:'ghc>'});
    });
</script>
</body>
</html>

