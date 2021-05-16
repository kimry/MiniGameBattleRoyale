package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kim.mini.databinding.ActivityRoomBinding

class RoomActivity : AppCompatActivity() {

    //뷰바인딩 함수
    private lateinit var binding: ActivityRoomBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent :Intent

    //userID 함수
    private lateinit var userID : String

    //userListview 저장함수
    var userList = ArrayList<UserlistModel>()

    //user state 함수
    private lateinit var state : String

    private lateinit var preActivity : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        //뷰 바인딩
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        //userid 요청
        serviceIntent.action = ConnectionService.ACTION_REQUESTID
        startService(serviceIntent)

        //user state 확인
        state = intent.getStringExtra("state").toString()
        if(state == "master"){
            binding.btnStart.setText("시작")
        }
        else{
            binding.btnStart.setText("준비")
        }

        //userList 요청
        binding.userList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.userList.setHasFixedSize(true)

        //preActivity 확인
        preActivity = intent.getStringExtra("preActivity").toString()
        if(preActivity == "Game") {
            Log.i("preActivity","sunggong")
            userListRenewal()
        }

        //나가기 버튼 클릭
        binding.btnExit.setOnClickListener {
            if(state=="master"){
                serviceIntent.putExtra("state", "master")
            }
            else{
                serviceIntent.putExtra("state", "nomal")
            }
            serviceIntent.action = ConnectionService.ACTION_ROOMEXIT
            startService(serviceIntent)

            val nextIntent = Intent(this, LobbyActivity::class.java)
            startActivity(nextIntent)

            finish()
        }

        //보내기 버튼 클릭
        binding.btnSend.setOnClickListener {
            serviceIntent.putExtra("msg", "${binding.inputText.text}")
            serviceIntent.action = ConnectionService.ACTION_CHATMESSAGE
            startService(serviceIntent)
            binding.inputText.setText("")
        }

        //시작 버튼 클릭
        binding.btnStart.setOnClickListener {
            if(state=="master") {
                serviceIntent.action=ConnectionService.ACTION_GAMESTART
                startService(serviceIntent)
            }
            else{
                serviceIntent.action=ConnectionService.ACTION_GAMEREADY
                startService(serviceIntent)
            }
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
            "getUserList" -> getUserList(intent.getStringExtra("userLists").toString())
            "userListRenewal" -> userListRenewal()
            "moveGame" -> moveGame(intent.getStringExtra("possible").toString())
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
    fun getUserList(userLists : String) {
        userList.add(UserlistModel(userLists))
        binding.userList.adapter = UserlistAdapter(userList)
    }
    fun userListRenewal(){
        serviceIntent.putExtra("place","Room")
        serviceIntent.action = ConnectionService.ACTION_USERLIST
        startService(serviceIntent)

        userList = ArrayList<UserlistModel>()
        binding.userList.adapter = UserlistAdapter(userList)
    }
    fun moveGame(possible : String){
        Log.i("moveGame","${possible}")
        if(possible=="possible"){

            val nextIntent = Intent(this, MatchingCardActivity::class.java)
            if(state=="master") {
                nextIntent.putExtra("state", "master")
            }
            else{
                nextIntent.putExtra("state", "nomal")
            }
            startActivity(nextIntent)
            finish()
        }
        else{
            Toast.makeText(this,"모두 준비가 완료되지 않았습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}