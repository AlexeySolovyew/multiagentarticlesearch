<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"index.php\">здесь</a>");
?>
<html>
<head>
    <title>Результат загрузки файла</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");
?>
<p><b>Результат</b>:
    <br/>

    <?php
    if ($_FILES["filename"]["size"] > 1024 * 3 * 1024) {
        echo ("Размер файла превышает три мегабайта");
        exit;
    }
    $code = "";
    // Проверяем загружен ли файл
    if (is_uploaded_file($_FILES["filename"]["tmp_name"]) && isset($_POST['tasknum'])) {
        // Если файл загружен успешно, перемещаем его
        // из временной директории в конечную
        $filedir = "../../files/" . $_FILES["filename"]["name"];
        //$real_file_path = realpath($filedir);
        //echo $filedir;
        //echo $real_file_path;
        move_uploaded_file($_FILES["filename"]["tmp_name"], $filedir);
        $file_handle = fopen($filedir, "r");
        while (!feof($file_handle)) {
            echo feof($file_handle);
            $line = fgets($file_handle);
            $code = $code.$line."\n";
        }
        echo $code;
        fclose($file_handle);
    } else if ($_POST['code'] != null) {
        //если текстом, то пытаемся создать файл
        $filedir = "../../files/filetask" . $_POST['tasknum'] . ".hs";
        if (!file_exists($filedir)) {
            $fp = fopen($filedir, "w");
            fclose($fp);
        }
        $real_file_path = realpath($filedir);
        file_put_contents($real_file_path, $_POST['code']);
        $code = $_POST['code'];
        //далее отладочная печать
        //echo $_POST['code'];
        //echo $real_file_path;
    }

    //пытаемся прогнать все тесты
    $testresult="";
    echo "Загружаем тестовую базу...<br>";
    $tasktests = mysql_query("SELECT * FROM Test WHERE TaskID=" . $_POST['tasknum']);
    if ($tasktests) {
        $q = mysql_num_rows($tasktests);
        echo "Всего загружено тестов: " . $q . "<br>";
        for ($i = 0; $i < $q; $i++) {
            $row = mysql_fetch_array($tasktests);
            echo "Прогоняется тест номер: " . $i . "<br>";
            //$line = exec("ghc -e \"".$row[Expression]."\" ".$filedir,$line,$result);
            //echo "ghc -e \"".$row[Expression]."\" ".$real_file_path;
            $line = exec("ghc -e \"" . $row[Expression] . "\" " . $real_file_path, $array, $result);
            //echo $result;
			//echo $line;
			//echo "ghc -e \"" . $row[Expression] . "\" " . $real_file_path;
            if ($result != 0 || $line == "") {
                $testresult = "Не удалось вычислить выражение \"" . $row[Expression] . "\", проверьте правильность синтаксиса";
                echo $testresult;
                break;
            }
            if (strcmp($line, $row[Result]) != 0) {
                if ($row[Smart] == 0) {
                    $testresult = "Выражение имеет неправильное значение: " . $row[Expression];
                    echo $testresult;
                } else {
                    $testresult = "Хитрый тест номер " . $i . " не пройден :(<br/>Подсказка: ".$row['SmartHelp'];
                    echo $testresult;
                }
                break;
            }
        }
        if ($i == $q) {
            $testresult = "<br/>Тесты успешно пройдены!";
            echo $testresult; ;
        } else echo "<br/>Попробуйте снова!";

        mysql_query("INSERT INTO Solution (TaskID,UserID,LoadTimestamp,ResultID,Code,TestResult)
         VALUES (".$_POST['tasknum'].",'".$_SESSION['user_id']."',NOW(),0,'".$code."','".$testresult."')");
    } else {
        echo "Неверно выбрана задача...";
    }
    //удаляем временный файл
    if (isset($filedir)) unlink($filedir);
    ?>
    <br/>

<form action="load.php" method="POST"><input type="hidden" name="taskid" value="<?php echo $_POST[tasknum]?>">
<input type="submit" value="Загрузить ещё решение"></form>
</body>
</html>