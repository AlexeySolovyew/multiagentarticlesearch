<?php session_start();
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 20.03.12
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
 if (isset($_GET['exit'])) unset($_SESSION['user_id']);
?>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css">
	<script type="text/javascript" src="http://userapi.com/js/api/openapi.js?49"></script>

<script type="text/javascript">
  VK.init({apiId: API_ID});
</script>
</head>
<body>
<table>
<tr>
<td width="50%">
Вход:<br>
<form action="index.php" method="post">
    <table>
        <tr>
            <td>Логин:</td>
            <td><input type="text" name="login" /></td>
        </tr>
        <tr>
            <td>Пароль:</td>
            <td><input type="password" name="password" /></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Войти" /></td>
        </tr>
    </table>
</form>
</td>
<td width="50%">
<div id="vk_auth"></div>
<script type="text/javascript">
VK.Widgets.Auth("vk_auth", {width: "200px", authUrl: '/register.php'});
</script>
</td>
</tr>
</table>



</body>
</html>