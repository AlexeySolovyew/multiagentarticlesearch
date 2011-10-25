<html>
<head>
<title>Functional Programming</title>
</head>
<body>
<h2><p><b> Форма для загрузки файлов </b></p></h2>
      <form action="index.php" method="post" enctype="multipart/form-data">
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
   if(is_uploaded_file($_FILES["filename"]["tmp_name"]))
   {
     // Если файл загружен успешно, перемещаем его
     // из временной директории в конечную
     move_uploaded_file($_FILES["filename"]["tmp_name"], "../files/".$_FILES["filename"]["name"]);
$line = system("ghc -e \"take 10 lst\" ../files/".$_FILES["filename"]["name"],$result);
echo $line;
echo "<br>";
echo $result;
   } else {
      echo("Файл не загружен");
   }

	
?>

</body></html>