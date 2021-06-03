package com.kim.mini

import android.app.Service
import android.content.Intent
import android.content.Intent.getIntent
import android.os.IBinder
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class ConnectionService : Service() {

    private lateinit var mSocket : Socket

    companion object {
        val ACTION_CONNECTION = "connection"
        val ACTION_CHATMESSAGE = "chatMessage"
        val ACTION_ENTERROOM = "enterRoom"
        val ACTION_DISCONNECTION = "disconnection"
        val ACTION_REQUESTID = "requestID"
        val ACTION_ROOMLIST = "roomList"
        val ACTION_CREATEROOM = "createRoom"
        val ACTION_ROOMEXIT = "roomExit"
        val ACTION_USERLIST = "userList"
        val ACTION_LOBBYFINISH = "lobbyFinish"
        val ACTION_GAMESTART = "gameStart"
        val ACTION_GAMEREADY = "gameReady"
        val ACTION_OPPONENTSCREEN = "opponentscreen"
        val ACTION_SENDSCREEN = "sendScreen"
        val ACTION_WAITROOMFINISH = "waitingroomFinish"
        val ACTION_BTNACTION = "btnAction"
        val ACTION_OPSCREENEXIT = "opscreenExit"
        val ACTION_MCSENDSCREEN = "mcsendscreen"
        val ACTION_GAMECLEAR = "gameClear"
        val ACTION_OPFINISH = "opFinish"
        val ACTION_TIMEOUT = "timeout"
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent?.action
        when(action) {
            ACTION_CONNECTION -> connection(intent.getStringExtra("userID").toString())
            ACTION_CHATMESSAGE -> chatMessage(intent.getStringExtra("msg").toString())
            ACTION_ENTERROOM -> enterRoom(intent.getStringExtra("roomNumber").toString())
            ACTION_DISCONNECTION -> disconnection()
            ACTION_REQUESTID -> requestID()
            ACTION_ROOMLIST -> roomList()
            ACTION_CREATEROOM -> createRoom()
            ACTION_ROOMEXIT -> roomExit(intent.getStringExtra("state").toString())
            ACTION_USERLIST -> userList(intent.getStringExtra("place").toString())
            ACTION_LOBBYFINISH -> lobbyFinish()
            ACTION_GAMESTART -> gameStart()
            ACTION_GAMEREADY -> gameReady()
            ACTION_OPPONENTSCREEN -> opponentscreen(intent.getStringExtra("Opuserid").toString(),
                                                    intent.getStringExtra("game").toString())
            ACTION_SENDSCREEN -> sendScreen(
                intent.getStringExtra("opuserid"),
                intent.getIntArrayExtra("btn1"),
                intent.getIntArrayExtra("btn2"),
                intent.getStringExtra("count"),
                intent.getIntExtra("n",0))
            ACTION_WAITROOMFINISH -> waitingroomFinish(intent.getStringExtra("Opuserid").toString())
            ACTION_BTNACTION -> btnAction(intent.getStringExtra("game"),intent.getIntExtra("point",0))
            ACTION_OPSCREENEXIT -> opscreenExit()
            ACTION_MCSENDSCREEN -> mcsendscreen(
                intent.getStringExtra("opuserid"),
                intent.getIntArrayExtra("btn"),
                intent.getIntExtra("openCount",0),
                intent.getIntExtra("btn_index",0))
            ACTION_GAMECLEAR -> gameClear(intent.getStringExtra("game"))
            ACTION_OPFINISH -> opFinish()
            ACTION_TIMEOUT -> timeout(intent.getStringExtra("game"))
        }

        return super.onStartCommand(intent, flags, startId)
    }
    fun timeout(game : String?) {
        mSocket.emit("timeout",game)
    }
    fun opFinish()
    {
        mSocket.emit("opFinish")
    }
    fun gameClear(game : String?)
    {
        mSocket.emit("gameClear",game)
    }
    fun mcsendscreen(opuserid : String?, btn : IntArray?, openCount : Int?, btn_index : Int?)
    {
        mSocket.emit("mcsendScreen",opuserid, btn!![0],btn[1],btn[2],btn[3],btn[4],btn[5],btn[6],btn[7],btn[8],btn[9],btn[10],btn[11],btn[12],btn[13],btn[14],
                                                     btn[15],openCount,btn_index)
    }
    fun opscreenExit()
    {
        mSocket.emit("opscreenExit")
    }
    fun btnAction(game : String?, point : Int)
    {
        mSocket.emit("sendAction",point,game)
    }
    fun waitingroomFinish(opuserid : String)
    {
        var activityIntent : Intent
        activityIntent = Intent(this, WaitingActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","Finish")
        activityIntent.putExtra("opuserid",opuserid)
        startActivity(activityIntent)
    }
    fun sendScreen(opuserid : String?, btn1 : IntArray?,btn2 : IntArray?, count : String?, n : Int){

        mSocket.emit("sendScreen",opuserid, btn1!![0],btn1[1],btn1[2],btn1[3],btn1[4],btn1[5],btn1[6],btn1[7],btn1[8],btn1[9],
                                                   btn1[10],btn1[11],btn1[12],btn1[13],btn1[14],btn1[15],btn1[16],btn1[17],btn1[18],btn1[19],
                                                   btn1[20],btn1[21],btn1[22],btn1[23],btn1[24],
                                                   btn2!![0],btn2[1],btn2[2],btn2[3],btn2[4],btn2[5],btn2[6],btn2[7],btn2[8],btn2[9],
                                                   btn2[10],btn2[11],btn2[12],btn2[13],btn2[14],btn2[15],btn2[16],btn2[17],btn2[18],btn2[19],
                                                   btn2[20],btn2[21],btn2[22],btn2[23],btn2[24],count,n)
    }
    fun opponentscreen(opuserid : String, game : String){
        mSocket.emit("requestOpScreen",opuserid,game)
    }
    fun gameStart(){
        mSocket.emit("gameStart")
    }
    fun gameReady() {
        mSocket.emit("gameReady")
    }
    fun lobbyFinish() {
        Log.i("lobbyfinish","success")
        var activityIntent : Intent
        activityIntent = Intent(this, LobbyActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","finish")
        startActivity(activityIntent)
    }
    fun userList(place : String) {
        Log.i("userList","success")
        mSocket.emit("userlist",place)
    }
    fun roomExit(state : String){
        mSocket.emit("userDelete")
        mSocket.emit("enter","0")
    }
    fun createRoom() {
        mSocket.emit("createroom")

    }
    fun roomList(){
        mSocket.emit("roomlist")
    }

    fun requestID(){
        mSocket.emit("requestID")
    }
    fun disconnection() {
        mSocket.disconnect()
    }
    fun chatMessage(msg: String){
        try {
            mSocket.emit("chat message",msg)
            Log.i("sendMsg", "success $msg")
        } catch (e: URISyntaxException) {
            Log.i("sendMsg", "fail")
        }
    }

    fun enterRoom(roomNumber : String){
        mSocket.emit("enter",roomNumber)
    }
    fun connection(userID : String){
        try {
            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = IO.socket("http://api.odyssea-ogc.com:8090")
            Log.d("success", "successed to connect")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
        mSocket.connect()
        mSocket.emit("login",userID)
        mSocket.on("moveLobby",moveLobby)
        mSocket.on("chat message",onMessage)
        mSocket.on("getID",getID)
        mSocket.on("getRoomNumber",getRoomNumber)
        mSocket.on("getRoomList",getRoomList)
        mSocket.on("getUserList",getUserList)
        mSocket.on("userListRenewal",userListRenewal)
        mSocket.on("moveGame",moveGame)
        mSocket.on("endGame",endGame)
        mSocket.on("requestScreen",requestScreen)
        mSocket.on("getScreen",getScreen)
        mSocket.on("getAction",getAction)
        mSocket.on("mcgetScreen",mcgetScreen)
        mSocket.on("WmoveGame",WmoveGame)
        mSocket.on("backwaitingroom",backwaitingroom)
        mSocket.on("opFinish",opFinish)
    }
    private var moveLobby = Emitter.Listener {
        var activityIntent : Intent
        activityIntent = Intent(this, MainActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","moveLobby")
        startActivity(activityIntent)
    }
    private var onMessage = Emitter.Listener{
        var activityIntent : Intent
        when(it[1]) {
            "0" -> activityIntent = Intent(this, LobbyActivity::class.java)

            else -> activityIntent = Intent(this, RoomActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","showMsg")
        activityIntent.putExtra("userID","${it[0]}")
        activityIntent.putExtra("msg","${it[2]}")
        startActivity(activityIntent)

        Log.d("onMessage","successed to get message")
    }
    private var getID = Emitter.Listener {
        var activityIntent : Intent
        when(it[1]) {
            "0" -> activityIntent = Intent(this, LobbyActivity::class.java)

            else -> activityIntent = Intent(this, RoomActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","getId")
        activityIntent.putExtra("userID","${it[0]}")
        startActivity(activityIntent)
    }
    private var getRoomNumber = Emitter.Listener {
        var activityIntent =Intent(this,LobbyActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","getRoomNumber")
        activityIntent.putExtra("roomNumber","${it[0]}")
        startActivity(activityIntent)
    }
    private var getRoomList = Emitter.Listener {
        var activityIntent = Intent(this, LobbyActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        activityIntent.putExtra("command", "getRoomList")
        activityIntent.putExtra("roomLists", "${it[0]}")
        startActivity(activityIntent)
    }
    private var getUserList = Emitter.Listener {
        var activityIntent : Intent
        if(it[1].toString()=="Waiting") {
            Log.i("nowplace",(it[1]).toString())
            activityIntent = Intent(this,WaitingActivity::class.java)
        }
        else {
            Log.i("nowplace",(it[1]).toString())
            activityIntent = Intent(this, RoomActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command", "getUserList")
        activityIntent.putExtra("userLists", "${it[0]}")
        startActivity(activityIntent)
    }
    private var userListRenewal = Emitter.Listener {
        var activityIntent = Intent(this, RoomActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        activityIntent.putExtra("command", "userListRenewal")
        startActivity(activityIntent)
    }
    private var moveGame = Emitter.Listener{
        var activityIntent = Intent(this, RoomActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        activityIntent.putExtra("command", "moveGame")
        activityIntent.putExtra("possible","${it[0]}")
        startActivity(activityIntent)
    }
    private var endGame = Emitter.Listener {
        lateinit var activityIntent : Intent
        if(it[0]=="mc")
        {
            activityIntent = Intent(this, MatchingCardActivity::class.java)
        }else if(it[0]=="otf") {
            activityIntent = Intent(this, onetofiftyActivity::class.java)
        }else {
            activityIntent = Intent(this, shisenActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        mSocket.emit("enter","0")
        activityIntent.putExtra("command","endGame")
        startActivity(activityIntent)
    }
    private var requestScreen = Emitter.Listener {

        lateinit var activityIntent : Intent
        if(it[1]=="otf") {
            activityIntent = Intent(this, onetofiftyActivity::class.java)
        } else if(it[1]=="mc") {
            activityIntent = Intent(this, MatchingCardActivity::class.java)
        } else {
            activityIntent = Intent(this, onetofiftyActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        activityIntent.putExtra("command", "requestScreen")
        activityIntent.putExtra("opuserid", it[0].toString())
        startActivity(activityIntent)
    }
    private var getScreen = Emitter.Listener {
        var activityIntent = Intent(this,OponetofiftyActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","getScreen")
        var btn1 : IntArray = intArrayOf(it[0].toString().toInt(),it[1].toString().toInt(),it[2].toString().toInt(),it[3].toString().toInt(),it[4].toString().toInt(),
                it[5].toString().toInt(),it[6].toString().toInt(),it[7].toString().toInt(),it[8].toString().toInt(),it[9].toString().toInt(),it[10].toString().toInt(),
                it[11].toString().toInt(),it[12].toString().toInt(),it[13].toString().toInt(),it[14].toString().toInt(),it[15].toString().toInt(),it[16].toString().toInt(),
                it[17].toString().toInt(),it[18].toString().toInt(),it[19].toString().toInt(),it[20].toString().toInt(),it[21].toString().toInt(),it[22].toString().toInt(),
                it[23].toString().toInt(),it[24].toString().toInt())
        activityIntent.putExtra("btn1",btn1)
        var btn2 : IntArray = intArrayOf(it[25].toString().toInt(),it[26].toString().toInt(),it[27].toString().toInt(),it[28].toString().toInt(),it[29].toString().toInt(),
                it[30].toString().toInt(),it[31].toString().toInt(),it[32].toString().toInt(),it[33].toString().toInt(),it[34].toString().toInt(),it[35].toString().toInt(),
                it[36].toString().toInt(),it[37].toString().toInt(),it[38].toString().toInt(),it[39].toString().toInt(),it[40].toString().toInt(),it[41].toString().toInt(),
                it[42].toString().toInt(),it[43].toString().toInt(),it[44].toString().toInt(),it[45].toString().toInt(),it[46].toString().toInt(),it[47].toString().toInt(),
                it[48].toString().toInt(),it[49].toString().toInt())
        activityIntent.putExtra("btn2", btn2)
        var n = it[51].toString().toInt()
        activityIntent.putExtra("n",n)
        startActivity(activityIntent)
    }
    private var mcgetScreen = Emitter.Listener{
        var activityIntent = Intent(this,OpmatchingCardActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","getScreen")
        var btn : IntArray = intArrayOf(it[0].toString().toInt(),it[1].toString().toInt(),it[2].toString().toInt(),it[3].toString().toInt(),it[4].toString().toInt(),
            it[5].toString().toInt(),it[6].toString().toInt(),it[7].toString().toInt(),it[8].toString().toInt(),it[9].toString().toInt(),it[10].toString().toInt(),
            it[11].toString().toInt(),it[12].toString().toInt(),it[13].toString().toInt(),it[14].toString().toInt(),it[15].toString().toInt())
        var openCount : Int = it[16].toString().toInt()
        var btn_index : Int = it[17].toString().toInt()
        activityIntent.putExtra("btn",btn)
        activityIntent.putExtra("openCount",openCount)
        activityIntent.putExtra("btn_index",btn_index)
        startActivity(activityIntent)
    }
    private var getAction = Emitter.Listener {
        lateinit var activityIntent :Intent
        if(it[1]=="otf") {
            activityIntent = Intent(this, OponetofiftyActivity::class.java)
        }
        else if(it[1]=="mc")
        {
            activityIntent = Intent(this, OpmatchingCardActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","getAction")
        activityIntent.putExtra("point",it[0].toString().toInt())
        startActivity(activityIntent)
    }
    private var WmoveGame = Emitter.Listener {
        var activityIntent = Intent(this,WaitingActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","WmoveGame")
        startActivity(activityIntent)
    }
    private var backwaitingroom = Emitter.Listener {
        lateinit var activityIntent : Intent
        if(it[0]=="mc") {
            activityIntent = Intent(this,OpmatchingCardActivity::class.java)
        }else if(it[0]=="otf") {
            activityIntent = Intent(this,OponetofiftyActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","backwaitingroom")
        startActivity(activityIntent)
    }
    private var opFinish = Emitter.Listener {
        lateinit var activityIntent : Intent
        if(it[0]=="mc") {
            activityIntent = Intent(this,OpmatchingCardActivity::class.java)
        }else if(it[0]=="otf") {
            activityIntent = Intent(this,OponetofiftyActivity::class.java)
        }
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.putExtra("command","opFinish")
        startActivity(activityIntent)
    }
}
