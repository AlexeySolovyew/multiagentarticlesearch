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
    <LINK REL="SHORTCUT ICON" href="favicon.ico">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script type="text/javascript" src="http://userapi.com/js/api/openapi.js?49"></script>

    <script type="text/javascript">
        VK.init({apiId:2910180});
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
                        <td><input type="text" name="login"/></td>
                    </tr>
                    <tr>
                        <td>Пароль:</td>
                        <td><input type="password" name="password"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Войти"/></td>
                    </tr>
                </table>
            </form>
        </td>
        <td align="center" width="50%">
            <div id="vk_auth"></div>
            <script type="text/javascript">
                VK.Widgets.Auth("vk_auth", {width:"200px", authUrl:'<?php echo str_replace("login.php", "index.php", $_SERVER["REQUEST_URI"]); ?>'});
            </script>
        </td>
    </tr>
</table>

<div align="center"><i>The System created by Alexey Solovyew as the diploma work<br/> mailto:
    alexey.solovyew@gmail.com<br/>2011-2012</i></div>


</body>
</html>