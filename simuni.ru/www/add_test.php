<html>
<head>
<title>���������� ����� - ���� ��</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>
<b>�������� ��������� ������ �����:</b>
<?
if ($_POST['tasknum']!=null){
echo "<p>���������� ������ ����� � ������ �����: <b>".$_POST['tasknum']."</b>";
} else {
echo "�� ������ ����� ������, ������";
}
?>
<form action="testsall.php" method="post">
	
<?
 
$maxnum = mysql_query("SELECT MAX(TestForTaskID) AS TestNum FROM Test WHERE TaskID=".$_POST['tasknum']);
$row = mysql_fetch_array($maxnum);
$testfortask = $row['TestNum']+1;
echo "<p>��� ����� ��� <b>".$testfortask."</b> ���� ��� ������ ������.<br>";
echo "<input type=\"hidden\" name=\"testfortask\" value=\"".$testfortask."\">";
echo "<input type=\"hidden\" name=\"tasknum\" value=\"".$_POST['tasknum']."\">";

?>	  
      ���������:
	  <input name="expr"><br> 
	  ��������� ��������:
	  <input name="val"><br>
	  ������ ����:
	  <input name="smart" type="checkbox"><br>
      <input type="submit" value="�������� ����"><br>
</form> 
</html>