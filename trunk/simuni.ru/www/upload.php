<html>
<head>
  <title>��������� �������� �����</title>
</head>
<body>
<?
mysql_connect("localhost","root","solovyev");
mysql_select_db("simuni");
?>
<p><b>���������</b>:
<br/>

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
	 } else if ($_POST['code']!=null){
	 //���� �������, �� �������� ������� ����
	   $filedir = "../files/filetask".$_POST['tasknum'].".hs";
	   if(!file_exists($filedir)){
            $fp=fopen($filedir,"w");
            fclose($fp);
           }   
	   $real_file_path = realpath($filedir);
	   file_put_contents($real_file_path, $_POST['code'] );
	   //����� ���������� ������
	   //echo $_POST['code'];
	   //echo $real_file_path;
	 }
	 //�������� �������� ��� �����
	 echo "��������� �������� ����...<br>";
$tasktests = mysql_query("SELECT * FROM Test WHERE TaskID=".$_POST['tasknum']);
$q = mysql_num_rows($tasktests);
echo "����� ��������� ������: ".$q."<br>";
for ($i=0; $i<$q; $i++){
	$row = mysql_fetch_array($tasktests);
	echo "����������� ���� �����: ".$i."<br>";
	//$line = exec("ghc -e \"".$row[Expression]."\" ".$filedir,$line,$result);
	//echo "ghc -e \"".$row[Expression]."\" ".$real_file_path;
	$line = exec("ghc -e \"".$row[Expression]."\" ".$real_file_path,$array,$result);
	//echo $result;
	if ($result != 0 || $line == "") {
		echo "�� ������� ��������� ��������� \"".$row[Expression]."\", ��������� ������������ ����������";
		break;
	}
	echo "��������� ��������� ����� ".$line." � ".$row[Result].": ".strcmp ($line, $row[Result])."<br>";
	if (strcmp($line, $row[Result]) != 0){
      if ($row[Smart]==0){
	  echo "��������� ����� ������������ ��������: ".$row[Expression];
	  } else {
	  echo "������ ���� ����� ".$i." �� ������� :(";
	  }
	  break;
	}
}
if ($i==$q) echo "<br/>����� ������� ��������!"; else echo "<br/>���������� �����!";
?>
</body>
</html>