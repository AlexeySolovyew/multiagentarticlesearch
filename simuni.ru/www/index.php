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
      <form action="index.php" method="post" enctype="multipart/form-data">
	  Номер задачи, которую вы заливаете:
	  <input name="tasknum"><br>
	  Файл с задачей: <br>
      <input type="file" name="filename"><br> 
      <input type="submit" value="Загрузить и начать тестирование"><br>
      </form>

<p>Результат:

<?php
   if($_FILES["filename"]["size"] > 1024*3*1024)
   {
     echo ("Размер файла превышает три мегабайта");
     exit;
   }
   // Проверяем загружен ли файл
   if (is_uploaded_file($_FILES["filename"]["tmp_name"]) && $_POST['tasknum'] != null)
   {
     // Если файл загружен успешно, перемещаем его
     // из временной директории в конечную
	 $filedir = "../files/".$_FILES["filename"]["name"];
     move_uploaded_file($_FILES["filename"]["tmp_name"], $filedir);
	 $real_file_path = realpath($filedir);
	 //пытаемся прогнать все тесты
	 echo "Загружаем тестовую базу...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskNum=".$_POST['tasknum']);
$q = mysql_num_rows($tasktests);
echo "Всего загружено тестов: ".$q."<br>";
for ($i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasktests);
	echo "Прогоняется тест номер: ".$i."<br>";
	//$line = exec("ghc -e \"".$row[Expression]."\" ".$filedir,$line,$result);
	echo "ghc -e \"".$row[Expression]."\" ".$real_file_path;
	$line = exec("ghc -e \"".$row[Expression]."\" ".$real_file_path,$array,$result);
	echo $result;
	if ($result != 0) {
		echo "Не удалось вычислить выражение \"".$row[Expression]."\", проверьте правильность синтаксиса";
		break;
	}
	echo "Результат сравнения строк ".$line." и ".$row[Result].": ".strcmp ($line, $row[Result])."<br>";
	if (strcmp($line, $row[Result]) != 0){
	  echo "Тесты зафейлились на выражении: ".$row[Expression];
	  break;
	}
}
if ($i==$q) echo "Тесты успешно пройдены!";
   } else {
      echo("Пожалуйста, заполните все поля");
   }
?>
<br>
<a href="testsall.php">Все тесты</a> <br>
<a href="add_test.php">Добавить тест</a>
</body></html>