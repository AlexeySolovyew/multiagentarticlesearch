<?php
function connect_db() {
mysql_connect("localhost", "root", "sTRS9LDpJMTXuUwE");
mysql_select_db("simuni4");
mysql_set_charset('utf8');
mysql_query("SET NAMES utf8");
mysql_query("SET CHARACTER SET utf8");
}
?>