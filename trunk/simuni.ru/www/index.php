<html>
<head>
<title>Functional Programming</title>
</head>
<body>
<h2><p><b> ����� ��� �������� ������ </b></p></h2>
      <form action="index.php" method="post" enctype="multipart/form-data">
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
   if(is_uploaded_file($_FILES["filename"]["tmp_name"]))
   {
     // ���� ���� �������� �������, ���������� ���
     // �� ��������� ���������� � ��������
     move_uploaded_file($_FILES["filename"]["tmp_name"], "../files/".$_FILES["filename"]["name"]);
$line = system("ghc -e \"take 10 lst\" ../files/".$_FILES["filename"]["name"],$result);
echo $line;
echo "<br>";
echo $result;
   } else {
      echo("���� �� ��������");
   }

	
?>

</body></html>