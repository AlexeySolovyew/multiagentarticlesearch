<html>
<head>
<title>Functional Programming</title>
</head>
<body>

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
<a href="add_test.php">Добавить тест</a>
</body></html>