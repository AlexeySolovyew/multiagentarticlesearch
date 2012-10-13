<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Алексей
 * Date: 20.03.12
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */


?>
<html>
<head>
    <LINK REL="SHORTCUT ICON" href="favicon.ico">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1 align="center">Регистрация нового пользователя</h1>
<br/>
<b>Пожалуйста, заполните все поля</b>
<br/>
<br/>

<form action="index.php" method="post">
    <input type="hidden" name="register" value="1">
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
            <td>Подтверждение пароля:</td>
            <td><input type="password" name="password2"/></td>
        </tr>
        <tr>
            <td>Имя:</td>
            <td><input name="name"/></td>
        </tr>
        <tr>
            <td>Фамилия:</td>
            <td><input name="surname"/></td>
        </tr>
        <tr>
            <td>Номер группы:</td>
            <td><input name="gnum"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Зарегистрироваться"/></td>
        </tr>
    </table>
</form>
</body>
</html>
