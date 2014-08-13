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
$ghc_path= "/usr/bin/ghc";
$line = shell_exec($ghc_path." -e \"" . $_GET['cmd'] . "\" " . $real_file_path);
if ($line == "") {
    $testresult = "Cant evaluate \"" . $_GET['cmd'] . "\", check syntax";
    echo $testresult;
} else {
	echo $line;
}
//unlink($filedir);
?>
