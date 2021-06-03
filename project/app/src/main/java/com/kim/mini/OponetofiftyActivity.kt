package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.kim.mini.databinding.ActivityOponetofiftyBinding


class OponetofiftyActivity : AppCompatActivity() {
    //뷰 바인딩 함수
    private lateinit var binding : ActivityOponetofiftyBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent : Intent

    //user권한 함수
    private lateinit var state : String

    var fail : Int = 1
    var button = arrayOfNulls<RelativeLayout>(26)
    var button_text = arrayOfNulls<TextView>(26)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        //뷰 바인딩
        binding = ActivityOponetofiftyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent.action = ConnectionService.ACTION_OPPONENTSCREEN
        serviceIntent.putExtra("Opuserid",intent.getStringExtra("opuserid"))
        serviceIntent.putExtra("game","otf")
        startService(serviceIntent)

        binding.btnExit.setOnClickListener {

            serviceIntent.action= ConnectionService.ACTION_OPSCREENEXIT
            startService(serviceIntent)

            val nextIntent = Intent(this, WaitingActivity::class.java)
            nextIntent.putExtra("game","otf")
            startActivity(nextIntent)

            finish()
        }
    }

    var match_number_int = 1

    var button_number_01 = IntArray(25)
    var button_number_02 = IntArray(25)
    private fun set_button() {
        button[0] = binding.btn1
        button[1] = binding.btn2
        button[2] = binding.btn3
        button[3] = binding.btn4
        button[4] = binding.btn5
        button[5] = binding.btn6
        button[6] = binding.btn7
        button[7] = binding.btn8
        button[8] = binding.btn9
        button[9] = binding.btn10
        button[10] = binding.btn11
        button[11] = binding.btn12
        button[12] = binding.btn13
        button[13] = binding.btn14
        button[14] = binding.btn15
        button[15] = binding.btn16
        button[16] = binding.btn17
        button[17] = binding.btn18
        button[18] = binding.btn19
        button[19] = binding.btn20
        button[20] = binding.btn21
        button[21] = binding.btn22
        button[22] = binding.btn23
        button[23] = binding.btn24
        button[24] = binding.btn25
        button_text[0] = binding.btnText1
        button_text[1] = binding.btnText2
        button_text[2] = binding.btnText3
        button_text[3] = binding.btnText4
        button_text[4] = binding.btnText5
        button_text[5] = binding.btnText6
        button_text[6] = binding.btnText7
        button_text[7] = binding.btnText8
        button_text[8] = binding.btnText9
        button_text[9] = binding.btnText10
        button_text[10] = binding.btnText11
        button_text[11] = binding.btnText12
        button_text[12] = binding.btnText13
        button_text[13] = binding.btnText14
        button_text[14] = binding.btnText15
        button_text[15] = binding.btnText16
        button_text[16] = binding.btnText17
        button_text[17] = binding.btnText18
        button_text[18] = binding.btnText19
        button_text[19] = binding.btnText20
        button_text[20] = binding.btnText21
        button_text[21] = binding.btnText22
        button_text[22] = binding.btnText23
        button_text[23] = binding.btnText24
        button_text[24] = binding.btnText25
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.getStringExtra("command")) {
            "opFinish" -> opFinish()
            "getScreen" -> getScreen(intent.getIntArrayExtra("btn1"),intent.getIntArrayExtra("btn2"),intent.getIntExtra("n",0))
            "getAction" ->getAction(intent.getIntExtra("point",0))
            "backwaitingroom" -> backwaitingroom()
        }
    }
    fun backwaitingroom() {
        val nextIntent = Intent(this, WaitingActivity::class.java)
        nextIntent.putExtra("game","otf")
        startActivity(nextIntent)
        finish()
    }
    fun opFinish() {

        serviceIntent.action = ConnectionService.ACTION_OPFINISH
        startService(serviceIntent)

        val nextIntent = Intent(this, WaitingActivity::class.java)
        nextIntent.putExtra("game","otf")
        startActivity(nextIntent)
        finish()
    }
    fun getScreen(btn1 : IntArray?, btn2 : IntArray?, n : Int){

        match_number_int = n
        button_number_01 = btn1!!
        button_number_02 = btn2!!

        set_button()
        for(i in 0..24)
        {
            if(button_number_01!![i] != 0) {
                button_text[i]?.text=button_number_01[i].toString()
            }
            else{
                if(button_number_02!![i] != 0)
                {
                    button_text[i]?.text=button_number_02[i].toString()
                }
                else
                {
                    button_text[i]?.text=""
                }
            }
        }
    }
    fun getAction(point : Int) {
        if(button_number_01[point] != 0) {
            button_text[point]?.setText(button_number_02[point].toString())
            button_number_01[point] =0
        }
        else{
            if(button_number_02[point] != 0)
            {
                button_text[point]?.setText("")
                button_number_02[point] =0
            }
        }
    }
}