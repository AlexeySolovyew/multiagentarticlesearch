<html>
<head>
<title>Functional Programming</title>
</head>
<body>
<h2><p><b> Форма для загрузки файлов </b></p></h2>
      <form action="upload.php" method="post" enctype="multipart/form-data">
      <input type="file" name="filename"><br> 
      <input type="submit" value="Загрузить"><br>
      </form>

<table width=100% height=100%>
<tr><td align=center>
<?
$filepath = $_GET["filepath"];
echo $filepath;
if ($filepath!=NULL) {
$line = system("ghc -e \"take 10 lst\" $filepath",$result);
echo $line;
echo "<br>";
echo $result;
}
?>
</td></tr>
</table>

</body></html>