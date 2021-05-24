<?php
use Workerman\Worker;
use PHPSocketIO\SocketIO;
require_once __DIR__ . '/vendor/autoload.php';

// Listen port 8090 for socket.io client
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
        $socket->emit('moveLobby');
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


    $socket->on('userlist',function($place) use($io,$socket){
        echo "userlist send!! place : $place\n";
        foreach($io->userList["$socket->roomNumber"]["id"] as $id)
        {
            $socket->emit('getUserList',$id,$place);
        }
    });


    $socket->on('gameStart',function() use($io,$socket){
        $io->userList["$socket->roomNumber"]["round"]=1;
        $io->userList["$socket->roomNumber"]["clearuserid"]=[];
        $ucount = count($io->userList["$socket->roomNumber"]["id"]);
        $rcount = count($io->userList["$socket->roomNumber"]["ready"]);
        echo "gameStart!!\n";
        if($ucount==4)
        {
            if($rcount==$ucount-1){
                echo "room $socket->roomNumber start possible!!!\n";
                $io->roomList = array_diff($io->roomList,array($socket->roomNumber));
                $io->to($socket->roomNumber)->emit('moveGame','possible');
            }
            else{
                echo "room $socket->roomNumber start impossible!!!\n";
                $socket->emit('moveGame','impossible');
            }
        }
        else{
            echo "user is not enough!!!\n";
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


    $socket->on('requestOpScreen', function($opuserid, $game) use($io,$socket){
        $room = $opuserid."Screen";
        echo "$socket->userid join to $room!!\n";
        echo "$socket->userid request $opuserid!!!\n";
        $socket->opid=$room;
        $socket->join($room);
        $io->to($opuserid)->emit('requestScreen',$socket->userid,$game);
    });


    $socket->on('sendScreen',function($opuserid, $btn1_1,$btn1_2,$btn1_3,$btn1_4,$btn1_5,$btn1_6,$btn1_7,$btn1_8,$btn1_9,$btn1_10,
                                                 $btn1_11,$btn1_12,$btn1_13,$btn1_14,$btn1_15,$btn1_16,$btn1_17,$btn1_18,$btn1_19,$btn1_20,
                                                 $btn1_21,$btn1_22,$btn1_23,$btn1_24,$btn1_25,
                                                 $btn2_1,$btn2_2,$btn2_3,$btn2_4,$btn2_5,$btn2_6,$btn2_7,$btn2_8,$btn2_9,$btn2_10,
                                                 $btn2_11,$btn2_12,$btn2_13,$btn2_14,$btn2_15,$btn2_16,$btn2_17,$btn2_18,$btn2_19,$btn2_20,
                                                 $btn2_21,$btn2_22,$btn2_23,$btn2_24,$btn2_25, $count, $n) use($io,$socket){
        echo "$socket->userid send otfscreen to $opuserid!!!\n";
        $io->to($opuserid)->emit('getScreen',$btn1_1,$btn1_2,$btn1_3,$btn1_4,$btn1_5,$btn1_6,$btn1_7,$btn1_8,$btn1_9,$btn1_10,
                                             $btn1_11,$btn1_12,$btn1_13,$btn1_14,$btn1_15,$btn1_16,$btn1_17,$btn1_18,$btn1_19,$btn1_20,
                                             $btn1_21,$btn1_22,$btn1_23,$btn1_24,$btn1_25,
                                             $btn2_1,$btn2_2,$btn2_3,$btn2_4,$btn2_5,$btn2_6,$btn2_7,$btn2_8,$btn2_9,$btn2_10,
                                             $btn2_11,$btn2_12,$btn2_13,$btn2_14,$btn2_15,$btn2_16,$btn2_17,$btn2_18,$btn2_19,$btn2_20,
                                             $btn2_21,$btn2_22,$btn2_23,$btn2_24,$btn2_25, $count, $n);
    });


    $socket->on('sendAction',function($point,$game) use($io, $socket){
        $room = $socket->userid."Screen";
        echo "$socket->userid send $point to $room in $game!!!\n";
        $io->to($room)->emit("getAction",$point, $game);
    });


    $socket->on('opscreenExit',function() use($socket){
        echo "$socket->userid enter waitingroom from $socket->opid Screen\n!!";
        $socket->leave($socket->opid);
    });

    
    $socket->on('mcsendScreen',function($opuserid,$btn1,$btn2,$btn3,$btn4,$btn5,$btn6,$btn7,$btn8,$btn9,$btn10,
                                        $btn11,$btn12,$btn13,$btn14,$btn15,$btn16,$openCount,$btn_index) use($socket, $io){
        echo "$socket->userid send mcscreen to $opuserid!!!\n";
        $io->to($opuserid)->emit('mcgetScreen',$btn1,$btn2,$btn3,$btn4,$btn5,$btn6,$btn7,$btn8,$btn9,$btn10,
                                             $btn11,$btn12,$btn13,$btn14,$btn15,$btn16,$openCount,$btn_index);
    });
    $socket->on('gameClear',function($game) use($socket, $io){
        echo "$socket->userid $game clear!!\n";
        array_push($io->userList["$socket->roomNumber"]["clearuserid"],$socket->userid);
        if(count($io->userList["$socket->roomNumber"]["clearuserid"])==count($io->userList["$socket->roomNumber"]["id"])-1)
        {
            $loser=[];
            $loser=array_diff($io->userList["$socket->roomNumber"]["id"],$io->userList["$socket->roomNumber"]["clearuserid"]);
            foreach($loser as $id)
            {
                echo "$socket->roomNumber loser is $id!!!\n";
                $io->to($id)->emit('endGame',$game);
            }

            sleep(1);

            $io->userList["$socket->roomNumber"]["id"]=$io->userList["$socket->roomNumber"]["clearuserid"];
            $io->userList["$socket->roomNumber"]["clearuserid"]=[];
            if($game!='ss')
            {
                $io->to($socket->roomNumber)->emit('WmoveGame');
            }
            else{
                $socket->leave("$socket->roomNumber");
                $socket->join("0");
                echo "$socket->userid / $socket->roomNumber enter!!!\n";
                $socket->roomNumber = "0";
            }
        }
    });
});

Worker::runAll();
?>