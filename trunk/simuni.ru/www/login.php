<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<?phpphp
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 20.03.12
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
?>
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
</body>
</html>