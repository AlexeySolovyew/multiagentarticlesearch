<html>
<head>
<title>Functional Programming</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>

<h2><p><b> Форма для загрузки файлов с задачами </b></p></h2>
      <form action="upload.php" method="post" enctype="multipart/form-data">
	  Номер задачи, которую вы заливаете:
	  <input name="tasknum"><br>
	  Файл с задачей: <br>
      <input type="file" name="filename"><br> 
      <input type="submit" value="Загрузить и начать тестирование"><br>
      </form>
<br>
<a href="testsall.php">Все тесты</a> <br>
<form action="add_test.php" method="post">
<?
$tasks = mysql_query("SELECT * FROM Task");
$q = mysql_num_rows($tasks);
echo "<p><select size=\"1\" name=\"tasknum\">";
echo "<option disabled>Выберите задачу</option>";
for ( $i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasks);
	echo "<option value=".$row['TaskID'].">".$row['TaskID']."</option>";
}
echo "</select>";
?>
<br>
<input type="submit" value="Добавить новый тест">
</form>

</body></html>