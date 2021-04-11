<?php

$id=$_POST['userID'];
$pw=$_POST['userPW'];
include_once 'using_account_info.php';
if(findid($id))
{
    $reponse["code"]=0;
    echo json_encode($reponse);
}
else
{
    $reponse["code"]=1;
    echo json_encode($reponse);
    include_once 'using_account_info.php';
    create($id, $pw);
}
?>