package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.kim.mini.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {

    //뷰 바인딩 함수
    private lateinit var binding: ActivityLobbyBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent :Intent

    //상단 userID 함수
    var userID : String = "null"

    //roomListview 저장함수
    var roomList = ArrayList<RoomlistModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        //뷰 바인딩
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        //userid 요청
        serviceIntent.action = ConnectionService.ACTION_REQUESTID
        startService(serviceIntent)

        //RoomList 요청
        binding.roomList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.roomList.setHasFixedSize(true)
        serviceIntent.action = ConnectionService.ACTION_ROOMLIST
        startService(serviceIntent)

        //로그아웃 버튼 클릭
        binding.btnLogout.setOnClickListener {
            serviceIntent.action = ConnectionService.ACTION_DISCONNECTION
            startService(serviceIntent)

            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)

            finish()
        }

        //방만들기 버튼 클릭
        binding.btnCreate.setOnClickListener {
            serviceIntent.action = ConnectionService.ACTION_CREATEROOM
            startService(serviceIntent)
        }

        //보내기 버튼 클릭
        binding.btnSend.setOnClickListener {
            serviceIntent.putExtra("msg", "${binding.inputText.text}")
            serviceIntent.action = ConnectionService.ACTION_CHATMESSAGE
            startService(serviceIntent)
            binding.inputText.setText("")
        }

        //새로고침 버튼 클릭
        binding.btnRenewal.setOnClickListener {
            serviceIntent.action = ConnectionService.ACTION_ROOMLIST
            startService(serviceIntent)

            roomList = ArrayList<RoomlistModel>()
            binding.roomList.adapter = RoomlistAdapter(roomList)
        }
    }
    override fun onBackPressed(){
        serviceIntent.action = ConnectionService.ACTION_DISCONNECTION
        startService(serviceIntent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.getStringExtra("command"))
        {
            "showMsg" -> showMsg(intent.getStringExtra("userID").toString(),intent.getStringExtra("msg").toString())
            "getId" -> getId(intent.getStringExtra("userID").toString())
            "getRoomNumber" -> getRoomNumber(intent.getStringExtra("roomNumber").toString())
            "getRoomList" -> getRoomList(intent.getStringExtra("roomLists").toString())
            "finish" -> Finish()
        }
    }
    fun showMsg(ID : String, msg : String){
        binding.textResult.append("\n${ID} : ${msg}")
    }
    fun getId(ID : String) {
        userID = ID
        binding.textUserID.setText("ID : ${userID}")
        binding.textResult.setText("${userID}님의 접속을 환영합니다.")
    }
    fun getRoomNumber(roomNumber : String){
        serviceIntent.putExtra("roomNumber", "$roomNumber")
        serviceIntent.action = ConnectionService.ACTION_ENTERROOM
        startService(serviceIntent)

        val nextIntent = Intent(this, RoomActivity::class.java)
        nextIntent.putExtra("state","master")
        nextIntent.putExtra("preActivity","Lobby")
        startActivity(nextIntent)
        finish()
    }
    fun getRoomList(roomLists : String)
    {
        roomList.add(RoomlistModel(roomLists))
        binding.roomList.adapter = RoomlistAdapter(roomList)
    }
    fun Finish()
    {
        val nextIntent = Intent(this, RoomActivity::class.java)
        nextIntent.putExtra("state","nomal")
        startActivity(nextIntent)
        finish()
    }
}