package com.example.registerloginexample

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game1to50.*
import java.lang.Math.random
import java.util.*

class Game1to50 : AppCompatActivity() {
    var fail : Int = 1
    var button = arrayOfNulls<RelativeLayout>(25)
    var button_text = arrayOfNulls<TextView>(25)
    var count : TextView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1to50)

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
        button[16] = btn_17
        button[17] = btn_18
        button[18] = btn_19
        button[19] = btn_20
        button[20] = btn_21
        button[21] = btn_22
        button[22] = btn_23
        button[23] = btn_24
        button[24] = btn_25
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
        button_text[16] = btn_text_17
        button_text[17] = btn_text_18
        button_text[18] = btn_text_19
        button_text[19] = btn_text_20
        button_text[20] = btn_text_21
        button_text[21] = btn_text_22
        button_text[22] = btn_text_23
        button_text[23] = btn_text_24
        button_text[24] = btn_text_25
        count = et_count
    }

    var button_num = 0
    private fun all_button_click() {
        for (i in 0..24) {
            button[i]!!.setOnClickListener {
                if (match_number_int == 50) {

                    var dialog = AlertDialog.Builder(this@Game1to50)
                    dialog.setMessage("성공")

                    dialog.show()
                    fail = 0
                    Handler().postDelayed({
                        val nextIntent = Intent(this@Game1to50, InitialActivity::class.java)
                        startActivity(nextIntent)
                    }, 1500)


                }
                if (match_number_int == button_number_01[i]) {
                    match_number_int += 1
                    button_text[i]
                        ?.setText(Integer.toString(button_number_02[i]))
                }
                if (match_number_int == button_number_02[i]) {
                    match_number_int += 1
                    button_text[i]!!.visibility = View.GONE
                }

            }
        }

    }
    //60000 = 60초
    val timer = object: CountDownTimer(60000, 10){
        override fun onTick(millisUntilFinished: Long){
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {


            var dialog = AlertDialog.Builder(this@Game1to50)
            dialog.setMessage("실패")
            dialog.show()
            if(fail ==1) {
                Handler().postDelayed({
                    val nextIntent = Intent(this@Game1to50, InitialActivity::class.java)
                    startActivity(nextIntent)
                }, 1500)
            }

        }
    }

    private fun updateCountDownText(millisUntilFinished : Long){
        //var minutes : Int = ((mTimeLeftInMillis / 1000) / 60).toInt()
        var seconds : Int = ((millisUntilFinished / 1000) % 60).toInt()
        var milsec : Int = ((millisUntilFinished % 1000)/10).toInt()

        var timeLeftFormat : String = String.format("%02d:%02d",seconds, milsec)

        count?.setText(timeLeftFormat)
    }
}


