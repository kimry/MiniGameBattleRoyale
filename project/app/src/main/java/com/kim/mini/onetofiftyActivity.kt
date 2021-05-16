package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.kim.mini.databinding.ActivityOnetofiftyBinding
import java.util.*

class onetofiftyActivity : AppCompatActivity() {

    //뷰 바인딩 함수
    private lateinit var binding : ActivityOnetofiftyBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent :Intent

    //user권한 함수
    private lateinit var state : String

    var fail : Int = 1
    var button = arrayOfNulls<RelativeLayout>(25)
    var button_text = arrayOfNulls<TextView>(25)
    var count : TextView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        //뷰 바인딩
        binding = ActivityOnetofiftyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //user state 확인
        state = intent.getStringExtra("state").toString()
        set_button()
        Handler().postDelayed({
            timer.start()
            play()
            all_button_click()
        }, 3000)
    }

    var number_group1 = 0
    var number_group2 = 0
    private fun play() {
        number_group1 = 1
        while (number_group1 <= 2) {
            number_group2 = 1
            while (number_group2 <= 25) {
                random()
                number_group2++
            }
            number_group1++
        }
        for (print_button_num in 0..24) {
            button_text[print_button_num]
                ?.setText(Integer.toString(button_number_01[print_button_num]))
        }
    }

    var match_number_int = 1
    var random_button_number_int = 0
    var button_number_01 = IntArray(25)
    var button_number_02 = IntArray(25)
    private fun random() {
        val random = Random()
        random_button_number_int = random.nextInt(25)
        if (number_group1 == 1 && button_number_01[random_button_number_int] == 0) {
            button_number_01[random_button_number_int] = number_group2
        } else if (number_group1 == 1 && number_group2 <= 25) {
            random()
        }
        if (number_group1 == 2 && button_number_02[random_button_number_int] == 0) {
            button_number_02[random_button_number_int] = number_group2 + 25
        } else if (number_group1 == 2 && number_group2 <= 25) {
            random()
        }
    }

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
        count = binding.etCount
    }
    private fun all_button_click() {
        for (i in 0..24) {
            button[i]!!.setOnClickListener {
                if (match_number_int == button_number_01[i]) {
                    match_number_int += 1
                    button_text[i]?.setText(Integer.toString(button_number_02[i]))
                    button_number_01[i]=0
                    serviceIntent.action = ConnectionService.ACTION_BTNACTION
                    serviceIntent.putExtra("game","otf")
                    serviceIntent.putExtra("point",i)
                    startService(serviceIntent)

                }
                else
                if (match_number_int == button_number_02[i]) {
                    button_text[i]!!.visibility = View.GONE
                    button_number_02[i]=0
                    serviceIntent.action = ConnectionService.ACTION_BTNACTION
                    serviceIntent.putExtra("game","otf")
                    serviceIntent.putExtra("point",i)
                    startService(serviceIntent)
                    if (match_number_int == 50) {
                        Toast.makeText(this,"Win!!",Toast.LENGTH_SHORT).show();
                        startService(serviceIntent)
                        timer.cancel()
                        fail = 0
                        Handler().postDelayed({
                            val nextIntent = Intent(this, WaitingActivity::class.java)
                            nextIntent.putExtra("game","otf")
                            startActivity(nextIntent)

                            serviceIntent.action = ConnectionService.ACTION_GAMECLEAR
                            serviceIntent.putExtra("game","otf")
                            startService(serviceIntent)
                            finish()
                        }, 1000)
                    }
                    match_number_int += 1
                }

            }
        }

    }

    //60000 = 60초
    val timer = object: CountDownTimer(90000, 10){
        override fun onTick(millisUntilFinished: Long){
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {


            Toast.makeText(this@onetofiftyActivity,"Lose!!",Toast.LENGTH_SHORT).show();
            if(fail ==1) {
                Handler().postDelayed({
                    val nextIntent = Intent(this@onetofiftyActivity, RoomActivity::class.java)
                    nextIntent.putExtra("state",state)
                    nextIntent.putExtra("preActivity","Game")
                    startActivity(nextIntent)
                    finish()
                }, 1500)
            }

        }
    }

    private fun updateCountDownText(millisUntilFinished : Long){
        //var minutes : Int = ((mTimeLeftInMillis / 1000) / 60).toInt()
        var seconds : Int = ((millisUntilFinished / 1000) % 120).toInt()
        var milsec : Int = ((millisUntilFinished % 1000)/10).toInt()

        var timeLeftFormat : String = String.format("%02d:%02d",seconds, milsec)

        count?.setText(timeLeftFormat)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.getStringExtra("command")) {
            "endGame" -> endGame()
            "requestScreen" -> requestScreen(intent.getStringExtra("opuserid"))
        }
    }
    fun endGame() {
        Toast.makeText(this,"Lose!!",Toast.LENGTH_SHORT).show();
        Handler().postDelayed({
            timer.cancel()
            val nextIntent = Intent(this, LobbyActivity::class.java)
            startActivity(nextIntent)
            finish()
        },1500)
    }
    fun requestScreen(opuserid : String?) {
        serviceIntent.action = ConnectionService.ACTION_SENDSCREEN
        serviceIntent.putExtra("btn1",button_number_01)
        serviceIntent.putExtra("btn2",button_number_02)
        serviceIntent.putExtra("n",match_number_int)
        serviceIntent.putExtra("time",count!!.text)
        serviceIntent.putExtra("opuserid",opuserid)
        startService(serviceIntent)
    }

}