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
      <form action="index.php" method="post" enctype="multipart/form-data">
	  ����� ������, ������� �� ���������:
	  <input name="tasknum"><br>
	  ���� � �������: <br>
      <input type="file" name="filename"><br> 
      <input type="submit" value="��������� � ������ ������������"><br>
      </form>

<p>���������:

<?php
   if($_FILES["filename"]["size"] > 1024*3*1024)
   {
     echo ("������ ����� ��������� ��� ���������");
     exit;
   }
   // ��������� �������� �� ����
   if (is_uploaded_file($_FILES["filename"]["tmp_name"]) && $_POST['tasknum'] != null)
   {
     // ���� ���� �������� �������, ���������� ���
     // �� ��������� ���������� � ��������
	 $filedir = "../files/".$_FILES["filename"]["name"];
     move_uploaded_file($_FILES["filename"]["tmp_name"], $filedir);
	 $real_file_path = realpath($filedir);
	 //�������� �������� ��� �����
	 echo "��������� �������� ����...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskNum=".$_POST['tasknum']);
$q = mysql_num_rows($tasktests);
echo "����� ��������� ������: ".$q."<br>";
for ($i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasktests);
	echo "����������� ���� �����: ".$i."<br>";
	//$line = exec("ghc -e \"".$row[Expression]."\" ".$filedir,$line,$result);
	echo "ghc -e \"".$row[Expression]."\" ".$real_file_path;
	$line = exec("ghc -e \"".$row[Expression]."\" ".$real_file_path,$array,$result);
	echo $result;
	if ($result != 0) {
		echo "�� ������� ��������� ��������� \"".$row[Expression]."\", ��������� ������������ ����������";
		break;
	}
	echo "��������� ��������� ����� ".$line." � ".$row[Result].": ".strcmp ($line, $row[Result])."<br>";
	if (strcmp($line, $row[Result]) != 0){
	  echo "����� ����������� �� ���������: ".$row[Expression];
	  break;
	}
}
if ($i==$q) echo "����� ������� ��������!";
   } else {
      echo("����������, ��������� ��� ����");
   }
?>
<br>
<a href="testsall.php">��� �����</a> <br>
<a href="add_test.php">�������� ����</a>
</body></html>