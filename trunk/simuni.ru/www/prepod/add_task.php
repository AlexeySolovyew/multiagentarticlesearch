<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"../index.php\">здесь</a>");?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Добавление задачи - курс ФП</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni");
$queryyuser = "SELECT RoleID FROM `User` WHERE UserID='" . $_SESSION['user_id'] . "'";
$resuser = mysql_query($queryyuser);
$row = mysql_fetch_array($resuser);
if ($row[RoleID] != 2) die("Студенту нельзя лезть к материалам преподавателя!");
?>
<?php
if (isset($_POST['condition'])) {
    //echo "INSERT INTO Task (TaskForHometask,HometaskID,Condition) VALUES (\"" . $_POST['taskforhometask'] . "\",\"" . $_POST['hometaskid'] . "\",
    //\"" . $_POST['condition'] . "\")";
    //обработка запроса на добавление новой задачи
    if (mysql_query("INSERT INTO Task (TaskForHometask,HometaskID,`Condition`,Price) VALUES (\"" . $_POST['taskforhometask'] . "\",\"" . $_POST['hometaskid'] . "\",
    '" . $_POST['condition'] . "'," . $_POST['price'] . ")"
    )
    ) {
        echo "<font color=\"green\">Задача успешно добавлена.</font><br>";
    } else {
        echo "<font color=\"red\">Задача не была добавлена.</font><br>";
    }
}
?>
<table width="100%">
    <tr>
        <td align="right"><a href="tasks.php">К задачам</a>
        <td>
        <td align="right"><a href="../index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>

<h2 align="center">Страница добавления задачи</h2>
<br>
<br>

<form action="add_task.php" method="post">

    Домашнее задание:<br>
    <?php $tmp = mysql_query("SELECT Topic FROM Hometask WHERE HometaskID=" . $_POST['hometaskid']);
    $row = mysql_fetch_array($tmp);
    $name = $row['Topic'];
    echo "<b>" . $_POST['hometaskid'] . " - " . $name . "</b>";?>
    <br/>
    <br/>
    <?php
    $maxnum = mysql_query("SELECT MAX(TaskForHometask) AS TaskNum FROM Task WHERE HometaskID=" . $_POST['hometaskid']);
    $row = mysql_fetch_array($maxnum);
    if (!isset($row['TaskNum'])) {
        echo "Это будет первая задача для данного задания.";
    } else {
        echo "В этом задании уже есть <b>";
        echo $row['TaskNum'];
        echo "</b> задач(а) для данного задания";
    }
    ?>
    <br/>
    <br/>
    Номер задачи в задании:<br>
    <input name="taskforhometask"
           value="<?php if (isset($_POST['taskforhometask'])) echo $_POST['taskforhometask']; ?>"><br><br/>
    Условие:<br>
    <textarea cols="50" rows="10"
              name="condition"><?php if (isset($_POST['condition'])) echo $_POST['condition']; ?></textarea><br><br/>
    Цена задачи:<br>
    <input name="price" value="<?php if (isset($_POST['price'])) echo $_POST['price']; ?>"><br><br/>
    <input type="hidden" name="hometaskid" value="<?php if (isset($_POST['hometaskid'])) echo $_POST['hometaskid'];?>">
    <input type="submit" value="Добавить задачу"><br>
</form>
</html>