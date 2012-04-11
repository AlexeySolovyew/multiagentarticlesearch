<?php  session_start(); ?>
<html>
<head>
    <title>Functional Programming</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<script language='Javascript'>
    function reload(url) {
        location = url
    }
</script>

<!--<a href="index.php">Вы студент?</a><br>
<a href="prepod.php">Вы преподаватель?</a>
-->
<?php
function redirectToStartPage($row)
{
    if ($row['RoleID'] == 1) {
        //редирект на index.php
        //echo "<a href=\"index.php\">Перейти к своей странице</a><br>";
        echo "<script language='Javascript'>
                    setTimeout('reload(\"student/index.php\")', 1);
                  </script>";

    } else {
        echo "<script language='Javascript'>
                    setTimeout('reload(\"prepod/index.php\")', 1);
                  </script>";
    }
}

mysql_connect("localhost", "root", "12345678");
mysql_select_db("simuni");

if (isset($_SESSION['user_id'])) {
    $query = "SELECT *
            FROM `User`
            WHERE `UserID`='" . $_SESSION['user_id'] . "' LIMIT 1";
    $sql = mysql_query($query) or die(mysql_error());
    $row = mysql_fetch_assoc($sql);
    redirectToStartPage($row);
} elseif (isset($_POST['register'])) {
    $queryIns = "INSERT INTO `User` (UserID,Password,Name,Surname,GroupNumber)" .
        "VALUES ('" . $_POST['login'] . "','" . $_POST['password'] . "','" . $_POST['name'] . "','" . $_POST['surname'] . "',
    '" . $_POST['gnum'] . "')";
    if (mysql_query($queryIns)) {
        echo "Вы успешно зарегистрировались!<br>";
    } else {
        echo "Пользователь с таким логином уже существует.<br>";
    }
} elseif (isset($_POST['login']) && isset($_POST['password'])) {
    $login = mysql_real_escape_string($_POST['login']);
    //$password = md5($_POST['password']);
    $password = $_POST['password'];

    // делаем запрос к БД
    // и ищем юзера с таким логином и паролем

    $query = "SELECT *
            FROM `User`
            WHERE `UserID`='{$login}' AND `Password`='{$password}'
            LIMIT 1";
    $sql = mysql_query($query) or die(mysql_error());

    // если такой пользователь нашелся
    if (mysql_num_rows($sql) == 1) {
        // то мы ставим об этом метку в сессии (допустим мы будем ставить ID пользователя)

        $row = mysql_fetch_assoc($sql);
        $_SESSION['user_id'] = $row['UserID'];

        //пользователь найден, редиректим на нужную страницу в зависимости от роли
        redirectToStartPage($row);
    }
    else {
        echo "Неправильный логин или пароль.<br>";
    }
}
?>
<p>Здравствуйте, Вы не авторизованы.</p>
<a href="login.php">Войти</a>

<p><a href="register.php">Зарегистрироваться</a></p>

</body>
</html>