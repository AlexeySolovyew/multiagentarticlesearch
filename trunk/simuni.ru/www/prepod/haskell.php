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
//$ghc_path= "C:\ghc\ghc-7.4.1\bin\ghc";
//$line = exec($ghc_path." -e \"" . $_GET['cmd'] . "\" " . $real_file_path, $array, $result);
$line = exec("D:\ghc\bin\ghc -e \"" . $_GET['cmd'] . "\" " . $real_file_path, $array, $result);
//echo "ghc -e \"" . $_GET['cmd'] . "\" " . $real_file_path;
//echo print_r($array);
if ($result != 0 || $line == "") {
    $testresult = "Cant evaluate \"" . $_GET['cmd'] . "\", check syntax";
    echo $testresult;
} else echo $line;
//unlink($filedir);
?>
