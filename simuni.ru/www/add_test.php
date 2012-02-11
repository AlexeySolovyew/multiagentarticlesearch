<html>
<head>
<title>Добавление теста - курс ФП</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>
<b>Выберите параметры нового теста:</b>
<?
if ($_POST['tasknum']!=null){
echo "<p>Добавление нового теста к задаче номер: <b>".$_POST['tasknum']."</b>";
} else {
echo "Не указан номер задачи, ахтунг";
}
?>
<form action="testsall.php" method="post">
	
<?
 
$maxnum = mysql_query("SELECT MAX(TestForTaskID) AS TestNum FROM Test WHERE TaskID=".$_POST['tasknum']);
$row = mysql_fetch_array($maxnum);
$testfortask = $row['TestNum']+1;
echo "<p>Это будет уже <b>".$testfortask."</b> тест для данной задачи.<br>";
echo "<input type=\"hidden\" name=\"testfortask\" value=\"".$testfortask."\">";
echo "<input type=\"hidden\" name=\"tasknum\" value=\"".$_POST['tasknum']."\">";

?>	  
      Выражение:
	  <input name="expr"><br> 
	  Ожидаемое значение:
	  <input name="val"><br>
	  Хитрый тест:
	  <input name="smart" type="checkbox"><br>
      <input type="submit" value="Добавить тест"><br>
</form> 
</html>