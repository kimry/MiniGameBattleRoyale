package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kim.mini.databinding.ActivityRoomBinding
import com.kim.mini.databinding.ActivityWaitingBinding

class WaitingActivity : AppCompatActivity() {

    private lateinit var game : String
    //뷰 바인딩
    private lateinit var binding: ActivityWaitingBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent :Intent

    //userID 함수
    private lateinit var userID : String

    //userListview 저장함수
    var userList = ArrayList<UserlistModel>()

    //user state 함수
    private lateinit var state : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        binding = ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        //userList 요청
        binding.userList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.userList.setHasFixedSize(true)

        //보내기 버튼 클릭
        binding.btnSend.setOnClickListener {
        }

        //현재 게임
        game = intent.getStringExtra("game").toString()

        userListRenewal()


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
            "getUserList" -> getUserList(intent.getStringExtra("userLists").toString())
            "userListRenewal" -> userListRenewal()
            "Finish" -> Finish(intent.getStringExtra("opuserid").toString())
            "WmoveGame" -> WmoveGame()
        }
    }
    fun showMsg(ID : String, msg : String){
        binding.textResult.append("\n${ID} : ${msg}")
    }
    fun getUserList(userLists : String) {
        userList.add(UserlistModel(userLists))
        binding.userList.adapter = OpuserlistAdapter(userList)
    }
    fun userListRenewal(){
        serviceIntent.putExtra("place","Waiting")
        serviceIntent.action = ConnectionService.ACTION_USERLIST
        startService(serviceIntent)

        userList = ArrayList<UserlistModel>()
        binding.userList.adapter = OpuserlistAdapter(userList)
    }
    fun Finish(opuserid : String){
        var nextIntent : Intent
        if(game=="mc") {
            nextIntent = Intent(this, OpmatchingCardActivity::class.java)
        }else
        {
            nextIntent = Intent(this, OponetofiftyActivity::class.java)
        }
        nextIntent.putExtra("opuserid",opuserid)
        startActivity(nextIntent)

        finish()
    }
    fun WmoveGame() {
        var nextIntent : Intent
        if(game=="mc")
        {
            nextIntent = Intent(this, onetofiftyActivity::class.java)
        }else{
            nextIntent = Intent(this, shisenActivity::class.java)
        }
        Toast.makeText(this,"곧 게임이 시작합니다.",Toast.LENGTH_SHORT).show();

        Handler().postDelayed({

            startActivity(nextIntent)

            finish()
        }, 1500)
    }
}