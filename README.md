# 미니게임 배틀로얄

## 목차
### 1. 소개
### 2. 서버
### 3. 클라이언트

## 1. 소개
![first](https://user-images.githubusercontent.com/79510083/118403882-78f64380-b6ab-11eb-9541-fe3aba79795d.png)

### - php와 kotlin을 이용한 배틀로얄류 실시간 멀티플레이 게임
### - 총 3라운드(카드맞추기, 1to50, 사천성)로 진행되며 4명에서 시작해 한명씩 탈락하여 마지막 한명이 1등을 차지하는 방식
## 2. 서버
### - php / phpsocket.io library 사용
### - http
* Server/User_management/signup.php
* Server/User_management/login.php
### - websocket
* login
``` php
$socket->on('login',function($userid) use($socket) {
  $socket->userid = $userid;
  $socket->roomNumber = "0";
  $socket->join("0");
  $socket->join($socket->userid);
  echo "$socket->userid / $socket->roomNumber login!!!\n";
  $socket->emit('moveLobby');
});
```
* enter
* chat messasge
* requestID
* disconnect
* roomlist
* createroom
* userDelete
* userlist
* gameStart
* gameReady
* requestOpScreen
* sendScreen
* sendAction
* opscreenExit
* mcsendScreen
* gameClear
* onFinish
* timeout
## 3. 클라이언트
### - android studio를 이용한 kotlin 기반의 android 클라이언트
