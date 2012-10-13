<?php session_start(); ?>
<html>
<head>
    <title>Functional Programming</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <LINK REL="SHORTCUT ICON" href="favicon.ico">
</head>
<body>
<script language='Javascript'>
    function reload(url) {
        location = url
    }
</script>
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

mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni");

if (isset($_SESSION['user_id'])) {
    $query = "SELECT *
            FROM `User`
            WHERE `UserID`='" . $_SESSION['user_id'] . "' LIMIT 1";
    $sql = mysql_query($query) or die(mysql_error());
    $row = mysql_fetch_assoc($sql);
    redirectToStartPage($row);
} elseif (isset($_POST['register'])) {
    if (strcmp($_POST['password'], $_POST['password2']) != 0) {
        echo "<font color=\"red\">Пожалуйста, введите в обоих полях ввода пароля одинаковые пароли.</font><br>";

    } else {
        $queryIns = "INSERT INTO `User` (UserID,Password,Name,Surname,GroupNumber)" .
            "VALUES ('" . $_POST['login'] . "','" . $_POST['password'] . "','" . $_POST['name'] . "','" . $_POST['surname'] . "',
    '" . $_POST['gnum'] . "')";
        if (mysql_query($queryIns)) {
            echo "<font color=\"green\">Вы успешно зарегистрировались!</font><br>";
        } else {
            echo "<font color=\"red\">Вы не зарегистрированы. Пожалуйста, проверьте правильность полей.</font><br>";
        }
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
    } else {
        echo "<font color=\"red\">Неправильный логин или пароль.</font><br>";
    }
} elseif (isset($_GET['uid'])) {
//авторизация через вк
    $login = $_GET['uid'];
    $query = "SELECT *
            FROM `User`
            WHERE `UserID`='{$login}'
            LIMIT 1";

    $sql = mysql_query($query) or die(mysql_error());


    // если такой пользователь не нашелся
    if (mysql_num_rows($sql) < 1) {
        //новый пользователь вошел через ВК, регаем
        $queryIns = "INSERT INTO `User` (UserID,Password,Name,Surname,GroupNumber)" .
            "VALUES ('" . $_GET['uid'] . "','haloween','" . $_GET['first_name'] . "','" . $_POST['last_name'] . "',
    0)";
        if (!mysql_query($queryIns)) die("<font color=\"red\">Трудности с новыми пользователями, пожалуйста, попробуйте позже.</font>" . $queryIns);

    } else {
        //сравниваем хэш для серкьюрности
        //echo md5("2910180".$_GET['uid']."kudiQOGdUMycn0QKYaRe");
        //echo $_GET['hash'];
        if (strcmp($_GET['hash'], md5("2910180" . $_GET['uid'] . "kudiQOGdUMycn0QKYaRe")) != 0) die("<font color=\"red\">Ошибка входа.
		Если у вас в имени есть нестандартные символы, избавьтесь от них.</font>");

    }

    $_SESSION['user_id'] = $login;
    //пользователь найден, редиректим на нужную страницу в зависимости от роли
    echo "<script language='Javascript'>
                    setTimeout('reload(\"student/index.php\")', 1);
                  </script>";


}
?>
<p>Здравствуйте, Вы не авторизованы.</p>
<a href="login.php">Войти</a>

<p><a href="register.php">Зарегистрироваться</a></p>

</body>
</html>