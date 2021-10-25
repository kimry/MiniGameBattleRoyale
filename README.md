# 미니게임 배틀로얄

## 목차
>1. 소개
>      1. 기획의도
>      2. 팀원구성
>      3. 시스템 설계
>2. 서버
>3. 클라이언트


# 소개

* 기획의도
  * Fall guys, pubg 등 여러 사람들과 대결하여 마지막까지 살아남는 배틀로얄류의 게임들 이 큰 성공을 거두고 있습니다. 하지만 최소 10분에서 20분 이상 걸리는 게임플레이 시 간은 가볍게 게임을 즐기고 싶은 사람들에게 부담으로 다가올 수 있습니다. 이러한 사람 들을 위해 한 라운드에 1분안팎으로 끝낼 수 있는 여러 미니게임들을 차용하여 긴 플레이 시간에 지친 사람들에게 잠깐의 힐링타임을 제공하고자 미니게임 배틀로얄이라는 프로 젝트를 생각하게 됐습니다.
* 팀원구성
  * 김리영 : 서버개발 및 게임을 제외한 클라이언트 개발
  * 조석윤 : 라운드 진행을 위한 게임 개발
* 시스템 설계 및 개발 환경

![first](https://user-images.githubusercontent.com/79510083/121180078-667ebc80-c89b-11eb-8bf7-73d68c18436b.png)

* 기술구현 내용
  * socket.io를 이용한 서버와 클라이언트 간의 실시간 양방향 통신
  * 라운드 진행을 위한 미니게임 개발
  * 로그인을 위한 login Activity 개발
  * 회원가입을 위한 signup Activity 개발
  * 방 만들기/진입을 위한 Lobby Activity 개발
  * 게임 시작과 유저 진입을 위한 Room Activity 개발
  * 미니게임 통과 후 대기 및 상대방 화면 선택을 위한 Waiting Activity 개발
  * 미니게임 통과 후 상대방 화면을 실시간으로 확인하기 위한 opgame Activity 개발

## 서버
- php / phpsocket.io library 사용
### http
>* Server/User_management/signup.php
>* Server/User_management/login.php
### socket.io
>* login
>* enter
>* chat messasge
>* requestID
>* disconnect
>* roomlist
>* createroom
>* userDelete
>* userlist
>* gameStart
>* gameReady
>* requestOpScreen
>* sendScreen
>* sendAction
>* opscreenExit
>* mcsendScreen
>* gameClear
>* onFinish
>* timeout
## 클라이언트
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
