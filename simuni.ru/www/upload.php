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
?>
<a href="index.php?filepath=../files/<? echo $_FILES["filename"]["name"]; ?>">�� �������</a>
</body>
</html>