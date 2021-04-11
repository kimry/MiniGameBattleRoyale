package com.kim.mini

import android.app.Service
import android.content.Intent
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
        val ACTION_GAMEEND = "gameEnd"
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
            ACTION_USERLIST -> userList()
            ACTION_LOBBYFINISH -> lobbyFinish()
            ACTION_GAMESTART -> gameStart()
            ACTION_GAMEREADY -> gameReady()
            ACTION_GAMEEND ->gameEnd()
        }

        return super.onStartCommand(intent, flags, startId)
    }
    fun gameEnd(){
        mSocket.emit("gameEnd")
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
    }
    fun userList() {
        Log.i("userList","success")
        mSocket.emit("userlist")
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
        Log.i("srequestID","requested")
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
        mSocket.on("chat message",onMessage)
        mSocket.on("getID",getID)
        mSocket.on("getRoomNumber",getRoomNumber)
        mSocket.on("getRoomList",getRoomList)
        mSocket.on("getUserList",getUserList)
        mSocket.on("userListRenewal",userListRenewal)
        mSocket.on("moveGame",moveGame)
        mSocket.on("endGame",endGame)
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
        var activityIntent = Intent(this, RoomActivity::class.java)
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
        Log.i("endGame", "gameend")
        var activityIntent = Intent(this, onetofiftyActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        activityIntent.putExtra("command","endGame")
        startActivity(activityIntent)
    }
}
