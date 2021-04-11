<?php

$id=$_POST['userID'];
$pw=$_POST['userPW'];
include_once 'using_account_info.php';
$result = findid($id);
if($result and $result['pw']===$pw)
{
    $reponse["code"]=1;
    echo json_encode($reponse);
}
else
{
    $reponse["code"]=0;
    echo json_encode($reponse);
}
?>