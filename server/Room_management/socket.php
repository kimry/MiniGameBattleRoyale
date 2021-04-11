<?php
use Workerman\Worker;
use PHPSocketIO\SocketIO;
require_once __DIR__ . '/vendor/autoload.php';

// Listen port 2021 for socket.io client
$io = new SocketIO(8090);
$io->recentNumber =1;
$io->roomList=[];
$io->userList=[];
echo "server start!!!!!!\n";
$io->on('connection', function ($socket) use ($io) {
    echo "client connect!!!!!!\n";
    $socket->on('login',function($userid) use($socket) {
        $socket->userid = $userid;
        $socket->roomNumber = "0";
        $socket->join("0");
        $socket->join($socket->userid);
        echo "$socket->userid / $socket->roomNumber login!!!\n";
    });
    $socket->on('enter',function($roomNumber) use($io,$socket){
        $socket->leave($socket->roomNumber);
        $socket->roomNumber=$roomNumber;
        $socket->join($socket->roomNumber);
        if($roomNumber!="0")
        {
            array_push($io->userList["$socket->roomNumber"]["id"],$socket->userid);
            $io->to($socket->roomNumber)->emit('userListRenewal');
        }
        echo "$socket->userid / $socket->roomNumber enter!!!\n";
    });
    $socket->on('chat message', function($msg) use($socket,$io) {
        echo "chat message!!!!!\n";
        echo "$socket->userid / $socket->roomNumber : $msg\n";
        $io->to($socket->roomNumber)->emit('chat message', $socket->userid, $socket->roomNumber, $msg);
    });
    $socket->on('requestID', function() use($socket){
        echo "$socket->userid request ID!!!\n";
        $socket->emit('getID',$socket->userid, $socket->roomNumber);
    });
    $socket->on('disconnect',function() use($socket,$io){
        if($socket->roomNumber !="0")
        {
            echo "$socket->userid exit room $socket->roomNumber!!!\n";
            $io->userList["$socket->roomNumber"]["id"]=array_diff($io->userList["$socket->roomNumber"]["id"],array($socket->userid));
            if(count($io->userList["$socket->roomNumber"]["id"])==0)
            {
                echo "room $socket->roomNumber is deleted!!!\n";
                $io->roomList = array_diff($io->roomList,array($socket->roomNumber));
            }
            else
            {
                $io->to($socket->roomNumber)->emit('userListRenewal');
            }
        }
        echo "$socket->userid disconnect!!!!!\n";
    });
    $socket->on('roomlist',function() use($io,$socket){
        echo "roomlist send!!\n";
        foreach($io->roomList as $number)
        {
            $socket->emit('getRoomList',$number);
        }
    });
    $socket->on('createroom',function() use($io,$socket){
        $io->userList["$io->recentNumber"]=[];
        $io->userList["$io->recentNumber"]["id"]=[];
        $io->userList["$io->recentNumber"]["ready"]=[];
        array_push($io->roomList,"$io->recentNumber");
        echo "room $io->recentNumber create!!!!\n";
        $socket->emit('getRoomNumber',"$io->recentNumber");
        $io->recentNumber+=1;
    });
    $socket->on('userDelete',function() use($io,$socket){
        echo "$socket->userid exit room $socket->roomNumber!!!\n";
        $io->userList["$socket->roomNumber"]["id"]=array_diff($io->userList["$socket->roomNumber"]["id"],array($socket->userid));
        if(count($io->userList["$socket->roomNumber"]["id"])==0)
        {
            echo "room $socket->roomNumber is deleted!!!\n";
            $io->roomList = array_diff($io->roomList,array($socket->roomNumber));
        }
        else
        {
            $io->to($socket->roomNumber)->emit('userListRenewal');
        }
    });
    $socket->on('userlist',function() use($io,$socket){
        echo "userlist send!!\n";
        foreach($io->userList["$socket->roomNumber"]["id"] as $id)
        {
            $socket->emit('getUserList',$id);
        }
    });
    $socket->on('gameStart',function() use($io,$socket){
        $ucount = count($io->userList["$socket->roomNumber"]["id"]);
        $rcount = count($io->userList["$socket->roomNumber"]["ready"]);
        echo "room userlist count : $ucount!!!\n";
        if($rcount==$ucount-1){
            echo "room $socket->roomNumber start possible!!!\n";
            $io->to($socket->roomNumber)->emit('moveGame','possible');
        }
        else{
            echo "room $socket->roomNumber start impossible!!!\n";
            $socket->emit('moveGame','impossible');
        }
        
    });
    $socket->on('gameReady',function() use($io,$socket){
        if(in_array($socket->userid,$io->userList["$socket->roomNumber"]["ready"])){
            echo "user $socket->userid cancel to ready in room $socket->roomNumber!!!\n";
            $io->userList["$socket->roomNumber"]["ready"]=array_diff($io->userList["$socket->roomNumber"]["ready"],array($socket->userid));
        }
        else{
            echo "user $socket->userid ready in room $socket->roomNumber!!!\n";
            array_push($io->userList["$socket->roomNumber"]["ready"],$socket->userid);
        }
    });
    $socket->on('gameEnd',function() use($io,$socket){
        echo "room $socket->roomNumber game end!!!\n";
        $socket->broadcast->to($socket->roomNumber)->emit('endGame');
    });
});

Worker::runAll();
?>