<?php
/*
error_reporting(E_ALL);
ini_set("display_errors", 1);
*/
function findid($id)
{
    $dbHost=""; // DBurl 입력
    $dbName=""; // DBname 입력
    $dbUser=""; // DBuser 입력
    $dbPass=""; // DBpw 입력
    $pdo = new PDO("mysql:host={$dbHost};dbname={$dbName}",$dbUser,$dbPass);
    $statement =  $pdo -> query('select * from account_info where id="'.$id.'"');
    $row = $statement ->fetch();
    return $row;
}
function create($id, $pw)
{
    $dbHost="api.odyssea-ogc.com:13306";
    $dbName="riyoung";
    $dbUser="riyoung";
    $dbPass="rlafldud";
    $pdo = new PDO("mysql:host={$dbHost};dbname={$dbName}",$dbUser,$dbPass);
    $statement =  $pdo -> query("insert into account_info(id,pw) values('".$id."','".$pw."')");
}
function go_out($addr)
{
    $dbHost="api.odyssea-ogc.com:13306";
    $dbName="riyoung";
    $dbUser="riyoung";
    $dbPass="rlafldud";
    $pdo = new PDO("mysql:host={$dbHost};dbname={$dbName}",$dbUser,$dbPass);
    $statement =  $pdo -> query("delete from accout_info where id='1'");
}
?>
