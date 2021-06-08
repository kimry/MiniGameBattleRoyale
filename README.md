# 미니게임 배틀로얄

## 목차
### 1. 소개
### 2. 서버
### 3. 클라이언트

## 1. 소개
![first](https://user-images.githubusercontent.com/79510083/121180078-667ebc80-c89b-11eb-8bf7-73d68c18436b.png)

### - php와 kotlin을 이용한 배틀로얄류 실시간 멀티플레이 게임
### - 총 3라운드(카드맞추기, 1to50, 사천성)로 진행되며 4명에서 시작해 한명씩 탈락하여 마지막 한명이 1등을 차지하는 방식
## 2. 서버
### - php / phpsocket.io library 사용
### - http
* Server/User_management/signup.php
* Server/User_management/login.php
### - websocket
* login
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
* MainActivity<br>
![2](https://user-images.githubusercontent.com/79510083/121180474-ddb45080-c89b-11eb-9d8c-9214f518ec9b.png)
* SignupActivity<br>
![3](https://user-images.githubusercontent.com/79510083/121180496-e3aa3180-c89b-11eb-80b8-d2c703d03855.png)
* LobbyActivity<br>
![4](https://user-images.githubusercontent.com/79510083/121180520-e9077c00-c89b-11eb-9b2f-53f3bf632d43.png)
* RoomActivity<br>
![5](https://user-images.githubusercontent.com/79510083/121180550-f15fb700-c89b-11eb-9453-6fcee26687f1.png)
* onetofiftyActivity<br>
![6](https://user-images.githubusercontent.com/79510083/121180563-f6246b00-c89b-11eb-831f-ecb309dd9c8d.png)
* MatchingcardActivity<br>
![7](https://user-images.githubusercontent.com/79510083/121180592-fb81b580-c89b-11eb-97c7-33d40c7302a8.png)
* shisenActivity<br>
![8](https://user-images.githubusercontent.com/79510083/121180624-00df0000-c89c-11eb-9e6d-3dc8c7718e17.png)
