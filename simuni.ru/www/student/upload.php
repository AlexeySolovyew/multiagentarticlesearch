<?php session_start();
if (isset($_GET['exit'])) unset($_SESSION['user_id']);
isset($_SESSION['user_id']) or die("Вы не авторизованы. Пожалуйста, авторизуйтесь <a href=\"index.php\">здесь</a>");
?>
<html>
<head>
    <title>Результат загрузки решения</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
<?php
mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni");
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
    $filedir = "../../files/" . $_FILES["filename"]["name"];
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

$real_file_path = realpath($filedir);
set_time_limit(0);
//пытаемся прогнать все тесты
$timeout = 30;
$sleep = 1;
$testresult = "";

//создаем файл, в который будем писать результаты работы haskell (нужно для запуска в фоновом режиме)
$resfiledir = "resres.txt";

echo "Загружаем тестовую базу...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskID=" . $_POST['tasknum'] . " ORDER BY TestForTaskID");
if ($tasktests) {
    $q = mysql_num_rows($tasktests);
    echo "Всего загружено тестов: " . $q . "<br>";
    for ($i = 0; $i < $q; $i++) {
        $row = mysql_fetch_array($tasktests);
        echo "Прогоняется тест номер: " . $i . "<br>";
        //запуск в фоновом режиме интерпретатора
        popen("start /b C:\\ghc\\ghc-7.4.2\\bin\\ghc.exe -e \"" . $row[Expression] . "\" " . $real_file_path . " > " . $resfiledir, "r");
        //echo "Command: "."start /b C:\\ghc\\ghc-7.4.2\\bin\\ghc.exe -e \"" . $row[Expression] . "\" " . $real_file_path . " > ".$resfiledir;

        //echo "execution result: <br/>";
        //$line = exec("C:\\ghc\\ghc-7.4.2\\bin\\ghc.exe -e \"" . $row[Expression] . "\" " . $real_file_path);
        //echo "<br/>";

        //echo "C:\ghc\ghc-7.4.2\bin\ghc.exe -e \"" . $row[Expression] . "\" " . $real_file_path . " > ".$resfiledir;

        //ждем 30 секунд, потом насильно завершаем процесс
        $cur = 0;
        // пока не истекло время отведенное на выполнение скрипта продолжаем ждать

        while (true) {
            sleep($sleep);
            $cur += $sleep;
            //echo "Current timeout: ".$cur;
            $tasklist = exec("tasklist | find /c /i \"ghc.exe\"");
            //echo "Result of tasklist: ".$tasklist." end result";
            //echo "tasklist | find /c /i \"ghc.exe\"";
            //echo $tasklist;
            if ($tasklist != "0") {
                if ($cur >= $timeout) {
                    echo "Программа работает более " . $timeout . " секунд! Подозрение на бесконечный цикл. Прерывание...<br/>";
                    $linekill = exec("taskkill /F /IM ghc.exe");
                    //echo $linekill;
                    break;
                }
            } else {
                break;
            }
        }

        $realresdir = realpath($resfiledir);
        //echo $realresdir;

        $line = file_get_contents($realresdir);
        $line = trim($line);
        //echo 'Result, printed to file: '.$line;
        if ($line == "") {
            $testresult = "Не удалось вычислить выражение \"" . $row[Expression] . "\", проверьте правильность синтаксиса";
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
        ;
    } else echo "<br/>Попробуйте снова!";

    //записываем попытку в базу
    mysql_query("INSERT INTO Solution (TaskID,UserID,LoadTimestamp,ResultID,Code,TestResult)
         VALUES (" . $_POST['tasknum'] . ",'" . $_SESSION['user_id'] . "',NOW(),0,'" . str_replace("'", "''", $code) . "','" . $testresult . "')");
} else {
    echo "Неверно выбрана задача...";
}
//удаляем временный файл
//if (isset($filedir)) unlink($filedir);
?>
<br/>
</body>
</html>