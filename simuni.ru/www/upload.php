<html>
<head>
  <title>��������� �������� �����</title>
</head>
<body>
<?php
   if($_FILES["filename"]["size"] > 1024*3*1024)
   {
     echo ("������ ����� ��������� ��� ���������");
     exit;
   }
   // ��������� �������� �� ����
   if(is_uploaded_file($_FILES["filename"]["tmp_name"]))
   {
     // ���� ���� �������� �������, ���������� ���
     // �� ��������� ���������� � ��������
     move_uploaded_file($_FILES["filename"]["tmp_name"], "../files/".$_FILES["filename"]["name"]);
   } else {
      echo("������ �������� �����");
   }
//$line = system("ghc -e \"take 10 lst\" \"../files/$_FILES["filename"]["name"]\",$result);
//echo $line;
//echo "<br>";
//echo $result;
header('Location: http://simuni.ru/index.php?result=true');
	
?>
</body>
</html>