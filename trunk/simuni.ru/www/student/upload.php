<?php session_start(); include "../db.php";
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"index.php\">здесь</a>");
?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="../favicon.ico">
    <title>Результат загрузки решения</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
connect_db();
?>
<table width="100%">
    <tr>
        <td>
            <form action="load.php" method="POST"><input type="hidden" name="taskid"
                                                         value="<?php echo $_POST[tasknum]?>">
                <input type="submit" value="Загрузить ещё решение"></form>
        </td>
        <td><a href="index.php">На главную</a>
        <td>
    </tr>
</table>
<br/>

<h2 align="center">Результат тестирования</h2>
<br/>

<?php
if ($_FILES["filename"]["size"] > 1024 * 3 * 1024) {
    echo ("Размер файла превышает три мегабайта");
    exit;
}
$code = "";
$filedir = "";
// Проверяем загружен ли файл
if (is_uploaded_file($_FILES["filename"]["tmp_name"]) && isset($_POST['tasknum'])) {
    // Если файл загружен успешно, перемещаем его
    // из временной директории в конечную
    $filedir = "../files/" . $_FILES["filename"]["name"];
    //$real_file_path = realpath($filedir);
    //echo $filedir;
    //echo $real_file_path;
    move_uploaded_file($_FILES["filename"]["tmp_name"], $filedir);
    $file_handle = fopen($filedir, "r");
    while (!feof($file_handle)) {
        //echo feof($file_handle);
        $line = fgets($file_handle);
        $code = $code . $line . "\n";
    }
    //echo $code;
    fclose($file_handle);
} else if ($_POST['code'] != null) {
    //если текстом, то пытаемся создать файл
	$code = stripslashes($_POST['code']);
    $filedir = "../files/filetask" . $_POST['tasknum'] . ".hs";
	$resfile = file_put_contents($filedir, $code);
    
    //далее отладочная печать
    //echo $_POST['code'];
	//echo $code;
    //echo $filedir;
	//echo "resfile: ".$resfile;
}

$real_file_path = realpath($filedir);
//echo $filedir."\n";
//echo $real_file_path;
set_time_limit(0);
//пытаемся прогнать все тесты
$timeout = 30;
$sleep = 1;
$testresult = "";

//создаем файл, в который будем писать результаты работы haskell (нужно для запуска в фоновом режиме)
$resfiledir = dirname($real_file_path)."/resres.txt";

echo "Загружаем тестовую базу...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskID=" . $_POST['tasknum'] . " ORDER BY TestForTaskID");
if ($tasktests) {
    $q = mysql_num_rows($tasktests);
    echo "Всего загружено тестов: " . $q . "<br>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasktests);
        echo "Прогоняется тест номер: " . $i . "<br>";
        //запуск в фоновом режиме интерпретатора
		$ghc_path= "/usr/bin/ghc";
        $process_id = shell_exec($ghc_path." -e \"" . $row[Expression] . "\" " . $real_file_path . " > " . $resfiledir." & echo $!");
		//echo $ghc_path." -e \"".$row[Expression] . "\" " . $real_file_path . " > " . $resfiledir." & echo $!";
		//echo "Process id:".$process_id;
        
        //ждем 30 секунд, потом насильно завершаем процесс
        $cur = 0;
        // пока не истекло время отведенное на выполнение скрипта продолжаем ждать

        while (true) {
            sleep($sleep);
            $cur += $sleep;
            //echo "Current timeout: ".$cur;
            $tasklist = shell_exec("ps -p ".$process_id." echo $!");
			//echo "ps -p ".$process_id." echo $!";
			//echo "In: ".$tasklist." this: ".$process_id;
			//echo "Strval:".strval($process_id);
			//$resresres = strpos($tasklist,strval($process_id)." ?");
			//echo "Strpos: ".$resresres;
			//echo "Strpos: ".strpos($tasklist,"PID");
			//echo "Strlen: ".strlen($tasklist);
			
            
            if (strlen($tasklist)<=50) {
                break;
            } else {
				if ($cur >= $timeout) {
                    echo "Программа работает более " . $timeout . " секунд! Подозрение на бесконечный цикл. Прерывание...<br/>";
                    $linekill = shell_exec("kill ".$process_id);
                    //echo $linekill;
                    break;
                }
             
            }
        }

        //echo "resfiledir:".$resfiledir;

        $line = file_get_contents($resfiledir);
        $line = trim($line);
        //echo 'Result, printed to file: '.$line;
        if ($line == "") {
			if ($row[Smart] == 0) {
				$testresult = "Не удалось вычислить выражение \"" . $row[Expression] . "\", проверьте правильность синтаксиса";
			} else {
			$testresult = "Не удалось вычислить тестовое выражение, проверьте правильность синтаксиса";
			}
            echo $testresult;
            break;
        }
        if ($line != $row[Result]) {
            if ($row[Smart] == 0) {
                $testresult = "Выражение имеет неправильное значение: " . $row[Expression];
                echo $testresult;
            } else {
                $testresult = "Хитрый тест номер " . $i . " не пройден :(<br/>Подсказка: " . $row['SmartHelp'];
                echo $testresult;
            }
            break;
        }
    }

    if ($i == $q) {
        $testresult = "<br/>Тесты успешно пройдены!";
        echo $testresult;
    } else {
        echo "<br/>Попробуйте снова!";
    }

    //записываем попытку в базу
    $saved = mysql_query("INSERT INTO Solution (TaskID,UserID,LoadTimestamp,ResultID,Code,TestResult)
         VALUES (" . $_POST['tasknum'] . ",'" . $_SESSION['user_id'] . "',NOW(),0,'" . mysql_real_escape_string($code) . "','" . mysql_real_escape_string($testresult) . "')");
    if ($saved == 0) {
        echo "<br/>Не удалось сохранить попытку в системе, проверьте кодировку файла или текста, рекомендуемая кодировка: UTF-8";
    } else {
        echo "<br/>Решение успешно сохранено!";
    }


} else {
    echo "Неверно выбрана задача...";
}
//удаляем временный файл
if (isset($filedir)) unlink($filedir);
if (isset($resfiledir)) unlink($resfiledir);
?>
<br/>
</body>
</html>