<html>
<head>
<title>��� ����� - ���� ��</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
if ($_POST['tasknum']!=null){

	mysql_query("INSERT INTO Test (Expression,Result,TaskNum) VALUES (\"".$_POST['expr']."\",\"".$_POST['val']."\",\"".$_POST['tasknum']."\")");
	echo "<div color=\"green\">���� ������� ��������.</div><br>";
}
?>
<table border="1">
<caption>
������ ���� ������
</caption>
<tr>
<td>
<b>����� ������</b>
</td>
<td>
<b>���������</b>
</td>
<td>
<b>��������� ��������</b>
</td>
</tr>
<?
$tests = mysql_query("SELECT * FROM Test");
$q = mysql_num_rows($tests);
for ( $i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tests);
	echo "<tr><td>".$row[TaskNum]."</td><td>".$row[Expression]."</td><td>".$row[Result]."</td></tr>";
}
?>
</table>

<a href="add_test.php">�������� ����</a>
<a href="index.php">�� �������</a>

</form> 
</html>