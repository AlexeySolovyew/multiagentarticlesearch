<html>
<head>
  <title>Результат загрузки файла</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>
<p><b>Результат</b>:
<br/>

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
	 } else if ($_POST['code']!=null){
	 //если текстом, то пытаемся создать файл
	   $filedir = "../files/filetask".$_POST['tasknum'].".hs";
	   if(!file_exists($filedir)){
            $fp=fopen($filedir,"w");
            fclose($fp);
           }   
	   $real_file_path = realpath($filedir);
	   file_put_contents($real_file_path, $_POST['code'] );
	   //далее отладочная печать
	   //echo $_POST['code'];
	   //echo $real_file_path;
	 }
	 //пытаемся прогнать все тесты
	 echo "Загружаем тестовую базу...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskID=".$_POST['tasknum']);
$q = mysql_num_rows($tasktests);
echo "Всего загружено тестов: ".$q."<br>";
for ($i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasktests);
	echo "Прогоняется тест номер: ".$i."<br>";
	//$line = exec("ghc -e \"".$row[Expression]."\" ".$filedir,$line,$result);
	//echo "ghc -e \"".$row[Expression]."\" ".$real_file_path;
	$line = exec("ghc -e \"".$row[Expression]."\" ".$real_file_path,$array,$result);
	//echo $result;
	if ($result != 0 || $line == "") {
		echo "Не удалось вычислить выражение \"".$row[Expression]."\", проверьте правильность синтаксиса";
		break;
	}
	echo "Результат сравнения строк ".$line." и ".$row[Result].": ".strcmp ($line, $row[Result])."<br>";
	if (strcmp($line, $row[Result]) != 0){
      if ($row[Smart]==0){
	  echo "Выражение имеет неправильное значение: ".$row[Expression];
	  } else {
	  echo "Хитрый тест номер ".$i." не пройден :(";
	  }
	  break;
	}
}
if ($i==$q) echo "<br/>Тесты успешно пройдены!"; else echo "<br/>Попробуйте снова!";
?>
</body>
</html>