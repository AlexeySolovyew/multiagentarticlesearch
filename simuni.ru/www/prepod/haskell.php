<?php
//пытаемся создать файл
$filedir = "../../files/tmpfile.hs";
if (!file_exists($filedir)) {
    $fp = fopen($filedir, "w");
    fclose($fp);
}
$real_file_path = realpath($filedir);
file_put_contents($real_file_path, $_GET['code']);
$code = $_GET['code'];
$testresult = "";
$line = exec("ghc -e \"" . $_GET['cmd'] . "\" " . $real_file_path, $array, $result);
//$line = system("ghc -e \"" . $_GET['cmd'] . "\" " . $real_file_path, $array, $result);
//echo "ghc -e \"" . $_GET['cmd'] . "\" " . $real_file_path;
//echo $_GET['code'];
//echo $line;
if ($result != 0 || $line == "") {
    $testresult = "Cant evaluate \"" . $_GET['cmd'] . "\", check syntax";
    echo $testresult;
} else echo $line;
//unlink($filedir);
?>
