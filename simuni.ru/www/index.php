<html>
<head>
<title>Functional Programming</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>

<h2><p><b> ����� ��� �������� ������ � �������� </b></p></h2>
      <form action="upload.php" method="post" enctype="multipart/form-data">
	  ����� ������, ������� �� ���������:
	  <input name="tasknum"><br>
	  ���� � �������: <br>
      <input type="file" name="filename"><br> 
      <input type="submit" value="��������� � ������ ������������"><br>
      </form>
<br>
<a href="testsall.php">��� �����</a> <br>
<form action="add_test.php" method="post">
<?
$tasks = mysql_query("SELECT * FROM Task");
$q = mysql_num_rows($tasks);
echo "<p><select size=\"1\" name=\"tasknum\">";
echo "<option disabled>�������� ������</option>";
for ( $i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasks);
	echo "<option value=".$row['TaskID'].">".$row['TaskID']."</option>";
}
echo "</select>";
?>
<br>
<input type="submit" value="�������� ����� ����">
</form>

</body></html>