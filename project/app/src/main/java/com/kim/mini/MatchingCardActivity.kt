package com.kim.mini

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.textclassifier.ConversationAction
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.kim.mini.databinding.ActivityMatchingCardBinding
import java.sql.Connection
import java.util.*

class MatchingCardActivity : AppCompatActivity() {

    //뷰 바인딩 함수
    private lateinit var binding: ActivityMatchingCardBinding

    //서비스인텐트 함수
    private lateinit var serviceIntent : Intent

    var fail: Int = 1
    var fruits = arrayOfNulls<Drawable>(16)
    var fruitNum = intArrayOf(1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8)
    var matchCount: Int = 0
    var backgr = arrayOfNulls<TextView>(1)
    var now_number: TextView? = null
    var button = arrayOfNulls<RelativeLayout>(16)
    var count: TextView? = null

    var openCount: Int = 0
    var button1_index: Int = 0
    var button2_index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩
        binding = ActivityMatchingCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        set_button()
        mixingCard()

        Handler().postDelayed({
            initBg()
            timer.start()
            all_button_click()
        }, 2000)

    }


    private fun mixingCard() {
        for (i in 0..15) {
            fruits[i] = button[i]!!.background
        }
        val random = Random()
        for (i in 0..500) {
            val rand = random.nextInt(15)
            val temp: Drawable? = button[0]!!.background
            button[0]!!.background = button[rand]!!.background
            button[rand]!!.background = temp

            val temp2: Int = fruitNum[0]
            fruitNum[0] = fruitNum[rand]
            fruitNum[rand] = temp2
        }
    }

    private fun initBg() {
        for (i in 0..15) {
            button[i]!!.background = backgr[0]!!.background
        }
    }

    private fun set_button() {
        now_number = findViewById<View>(R.id.et_count) as TextView
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

        backgr[0] = binding.bg
        count = binding.etCount
    }


    private fun all_button_click() {

        for (i in 0..15) {
            button[i]!!.setOnClickListener {
                openCount++

                serviceIntent.action = ConnectionService.ACTION_BTNACTION
                serviceIntent.putExtra("point",i)
                serviceIntent.putExtra("game","mc")
                startService(serviceIntent)

                if (fruitNum[i] > 10) {
                    openCount--
                }

                if (openCount == 1) {
                    button[i]!!.background = fruits[fruitNum[i]-1]
                    button1_index = i
                }
                if (openCount == 2) {
                    button[i]!!.background = fruits[fruitNum[i]-1]
                    button2_index = i

                    if (button1_index == button2_index) {
                        openCount = 1
                    }
                    if (openCount == 2) {
                        Handler().postDelayed({
                            if (fruitNum[button1_index] != fruitNum[button2_index]) {
                                button[button1_index]!!.background = backgr[0]!!.background
                                button[button2_index]!!.background = backgr[0]!!.background
                                openCount = 0
                            }
                            if (fruitNum[button1_index] == fruitNum[button2_index]) {
                                button[button1_index]!!.background = fruits[fruitNum[button2_index]-1]
                                button[button2_index]!!.background = fruits[fruitNum[button1_index]-1]
                                matchCount++
                                fruitNum[button2_index] += 10
                                fruitNum[button1_index] += 10
                                openCount = 0
                                Log.d("matchcount : ", Integer.toString(matchCount))

                                if (matchCount == 8) {
                                    Toast.makeText(this,"Win!!", Toast.LENGTH_SHORT).show();
                                    timer.cancel()
                                    fail = 0

                                    Handler().postDelayed({
                                        serviceIntent.action = ConnectionService.ACTION_GAMECLEAR
                                        serviceIntent.putExtra("game","mc")
                                        startService(serviceIntent)

                                        val nextIntent = Intent(this@MatchingCardActivity, WaitingActivity::class.java)
                                        nextIntent.putExtra("game","mc")
                                        startActivity(nextIntent)
                                        finish()
                                    }, 1000)


                                }
                            }
                            openCount = 0
                        }, 600)
                    }

                }

            }
        }
    }

    val timer = object : CountDownTimer(90000, 10) {
        override fun onTick(millisUntilFinished: Long) {
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {
            Toast.makeText(this@MatchingCardActivity,"Win!!",Toast.LENGTH_SHORT).show();

            if (fail == 1) {
                Handler().postDelayed({
                    val nextIntent = Intent(this@MatchingCardActivity, WaitingActivity::class.java)
                    startActivity(nextIntent)
                }, 1500)
            }

        }
    }

    private fun updateCountDownText(millisUntilFinished: Long) {
        var seconds: Int = ((millisUntilFinished / 1000) % 90).toInt()
        var milsec: Int = ((millisUntilFinished % 1000) / 10).toInt()

        var timeLeftFormat: String = String.format("%02d:%02d", seconds, milsec)

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
        serviceIntent.action = ConnectionService.ACTION_MCSENDSCREEN
        serviceIntent.putExtra("btn",fruitNum)
        serviceIntent.putExtra("openCount",openCount)
        serviceIntent.putExtra("btn_index",button1_index)
        serviceIntent.putExtra("opuserid",opuserid)
        startService(serviceIntent)
    }
}