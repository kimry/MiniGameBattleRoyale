package com.kim.mini

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.kim.mini.databinding.ActivityOpmatchingCardBinding
import java.util.*

class OpmatchingCardActivity : AppCompatActivity() {

    //뷰 바인딩
    private lateinit var binding: ActivityOpmatchingCardBinding

    private lateinit var serviceIntent : Intent

    var fail: Int = 1
    var fruits = arrayOfNulls<Drawable>(16)
    var fruitNum = IntArray(17)
    var matchCount: Int = 0
    var backgr = arrayOfNulls<TextView>(1)
    var button = arrayOfNulls<RelativeLayout>(16)

    var openCount: Int = 0
    var button1_index: Int = 0
    var button2_index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩
        binding = ActivityOpmatchingCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //서비스인텐트 연결
        serviceIntent = Intent(this, ConnectionService::class.java)

        serviceIntent.action = ConnectionService.ACTION_OPPONENTSCREEN
        serviceIntent.putExtra("Opuserid",intent.getStringExtra("opuserid"))
        serviceIntent.putExtra("game","mc")
        startService(serviceIntent)

        set_button()

        binding.btnExit.setOnClickListener {
            serviceIntent.action= ConnectionService.ACTION_OPSCREENEXIT
            startService(serviceIntent)

            val nextIntent = Intent(this, WaitingActivity::class.java)
            nextIntent.putExtra("game","mc")
            startActivity(nextIntent)
            finish()
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

        backgr[0] = binding.bg
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.getStringExtra("command")) {
            "opFinish" -> opFinish()
            "getScreen" -> getScreen(intent.getIntArrayExtra("btn"),intent.getIntExtra("openCount",0),intent.getIntExtra("btn_index",0))
            "getAction" -> getAction(intent.getIntExtra("point",0))
            "backwaitingroom" -> backwaitingroom()
        }
    }
    fun backwaitingroom() {
        val nextIntent = Intent(this, WaitingActivity::class.java)
        nextIntent.putExtra("game","mc")
        startActivity(nextIntent)
        finish()
    }
    fun opFinish() {
        serviceIntent.action = ConnectionService.ACTION_OPFINISH
        startService(serviceIntent)

        val nextIntent = Intent(this, WaitingActivity::class.java)
        nextIntent.putExtra("game","mc")
        startActivity(nextIntent)
        finish()
    }
    fun getScreen(btn : IntArray?, openCount_s : Int, btn_index : Int){
        fruitNum = btn!!
        openCount = openCount_s
        button1_index = btn_index
        for(i in 0..15) {
            if(fruitNum[i]>10)
            {
                fruits[i]=button[i]!!.background
            }
            else{
                fruits[i]=button[i]!!.background
            }
        }
        for(i in 0..15){
            if(fruitNum[i]>10)
            {
                button[i]!!.background=fruits[fruitNum[i]-11]
            }else if(openCount_s==1&&btn_index==i) {
                button[i]!!.background=fruits[fruitNum[i]-1]
            }
            else
            {
                button[i]!!.background = backgr[0]!!.background
            }
        }
    }
    fun getAction(point : Int) {
        openCount++

        if (fruitNum[point] > 10) {
            openCount--
        }

        if (openCount == 1) {
            button[point]!!.background = fruits[fruitNum[point]-1]
            button1_index = point
        }
        if (openCount == 2) {
            button[point]!!.background = fruits[fruitNum[point] - 1]
            button2_index = point

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
                        button[button1_index]!!.background = fruits[fruitNum[button2_index] - 1]
                        button[button2_index]!!.background = fruits[fruitNum[button1_index] - 1]
                        matchCount++
                        fruitNum[button2_index] += 10
                        fruitNum[button1_index] += 10
                        openCount = 0
                        Log.d("matchcount : ", Integer.toString(matchCount))
                    }
                    openCount = 0
                }, 600)
            }
        }
    }
}