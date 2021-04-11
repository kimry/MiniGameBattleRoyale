package com.example.registerloginexample

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_matching_cards.*
//import kotlinx.android.synthetic.main.content_matching_cards.*
import java.util.*

class MatchingCard : AppCompatActivity() {
    var fail : Int = 1
    var fruits = arrayOfNulls<Drawable>(16)
    var fruitNum : Array<Int> = arrayOf(1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8)
    var matchCount : Int = 0
    var successIndex :Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    var backgr = arrayOfNulls<TextView>(1)
    var now_number: TextView? = null
    var button = arrayOfNulls<RelativeLayout>(16)
    var button_text = arrayOfNulls<TextView>(16)
    var count : TextView?= null

    var openCount : Int = 0
    var button1_index : Int = 0
    var button2_index : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_cards)
        set_button()
        mixingCard()

        Handler().postDelayed({
            initBg()
            timer.start()
            all_button_click()
        }, 2000)

    }



    private fun mixingCard(){
        val random = Random()
        for (i in 0..500){
            val rand = random.nextInt(15)
            val temp : Drawable? = button[0]!!.background
            button[0]!!.background = button[rand]!!.background
            button[rand]!!.background = temp

            val temp2 : Int = fruitNum[0]
            fruitNum[0] = fruitNum[rand]
            fruitNum[rand] = temp2
        }
        for (i in 0..15){
            fruits[i] = button[i]!!.background
        }

    }
    private fun initBg(){
        for (i in 0..15){
            button[i]!!.background = backgr[0]!!.background
        }
    }


    private fun set_button(){
        now_number = findViewById<View>(R.id.et_count) as TextView
        button[0] = btn_1
        button[1] = btn_2
        button[2] = btn_3
        button[3] = btn_4
        button[4] = btn_5
        button[5] = btn_6
        button[6] = btn_7
        button[7] = btn_8
        button[8] = btn_9
        button[9] = btn_10
        button[10] = btn_11
        button[11] = btn_12
        button[12] = btn_13
        button[13] = btn_14
        button[14] = btn_15
        button[15] = btn_16
        button_text[0] = btn_text_1
        button_text[1] = btn_text_2
        button_text[2] = btn_text_3
        button_text[3] = btn_text_4
        button_text[4] = btn_text_5
        button_text[5] = btn_text_6
        button_text[6] = btn_text_7
        button_text[7] = btn_text_8
        button_text[8] = btn_text_9
        button_text[9] = btn_text_10
        button_text[10] = btn_text_11
        button_text[11] = btn_text_12
        button_text[12] = btn_text_13
        button_text[13] = btn_text_14
        button_text[14] = btn_text_15
        button_text[15] = btn_text_16

        backgr[0] = bg
        count = et_count
    }


    private fun all_button_click() {

        for ( i in 0..15) {
            button[i]!!.setOnClickListener {
                openCount++

                if(successIndex[i] == 1) {
                    openCount--
                }

                if(openCount == 1){
                    button[i]!!.background = fruits[i]
                    button1_index = i
                }
                if(openCount == 2){
                    button[i]!!.background = fruits[i]
                    button2_index = i

                    if(button1_index == button2_index){
                        openCount = 1
                    }
                    if(openCount == 2) {
                        Handler().postDelayed({
                            if (fruitNum[button1_index] != fruitNum[button2_index]) {
                                button[button1_index]!!.background = backgr[0]!!.background
                                button[button2_index]!!.background = backgr[0]!!.background
                                openCount = 0
                            }
                            if (fruitNum[button1_index] == fruitNum[button2_index]) {
                                button[button1_index]!!.background = fruits[button2_index]
                                button[button2_index]!!.background = fruits[button1_index]
                                matchCount++
                                successIndex[button2_index] = 1
                                successIndex[button1_index] = 1
                                openCount = 0
                                Log.d("matchcount : ", Integer.toString(matchCount))

                                if(matchCount == 8){
                                    var dialog = AlertDialog.Builder(this@MatchingCard)
                                    dialog.setMessage("성공!!")
                                    dialog.show()

                                    fail = 0

                                    Handler().postDelayed({
                                        val nextIntent = Intent(this@MatchingCard, Game1to50::class.java)
                                        startActivity(nextIntent)
                                    }, 1500)


                                }
                            }
                            openCount = 0
                        }, 600)
                    }

                }

            }
        }
    }
    val timer = object: CountDownTimer(60000, 10){
        override fun onTick(millisUntilFinished: Long){
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {


            var dialog = AlertDialog.Builder(this@MatchingCard)
            dialog.setMessage("실패!!")
            dialog.show()
            if(fail == 1) {
                Handler().postDelayed({
                    val nextIntent = Intent(this@MatchingCard, Game1to50::class.java)
                    startActivity(nextIntent)
                }, 1500)
            }

        }
    }

    private fun updateCountDownText(millisUntilFinished : Long){
        var seconds : Int = ((millisUntilFinished / 1000) % 60).toInt()
        var milsec : Int = ((millisUntilFinished % 1000)/10).toInt()

        var timeLeftFormat : String = String.format("%02d:%02d",seconds, milsec)

        count?.setText(timeLeftFormat)
    }

}