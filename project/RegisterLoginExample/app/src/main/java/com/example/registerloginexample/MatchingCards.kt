package com.example.myfirstandroidst

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.registerloginexample.R
//import kotlinx.android.synthetic.main.activity_card.*
import java.util.*

import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.btn_1
//import kotlinx.android.synthetic.main.activity_main.btn_10
//import kotlinx.android.synthetic.main.activity_main.btn_11
//import kotlinx.android.synthetic.main.activity_main.btn_12
//import kotlinx.android.synthetic.main.activity_main.btn_13
//import kotlinx.android.synthetic.main.activity_main.btn_14
//import kotlinx.android.synthetic.main.activity_main.btn_15
//import kotlinx.android.synthetic.main.activity_main.btn_16
//import kotlinx.android.synthetic.main.activity_main.btn_2
//import kotlinx.android.synthetic.main.activity_main.btn_3
//import kotlinx.android.synthetic.main.activity_main.btn_4
//import kotlinx.android.synthetic.main.activity_main.btn_5
//import kotlinx.android.synthetic.main.activity_main.btn_6
//import kotlinx.android.synthetic.main.activity_main.btn_7
//import kotlinx.android.synthetic.main.activity_main.btn_8
//import kotlinx.android.synthetic.main.activity_main.btn_9
//import kotlinx.android.synthetic.main.activity_main.btn_text_1
//import kotlinx.android.synthetic.main.activity_main.btn_text_10
//import kotlinx.android.synthetic.main.activity_main.btn_text_11
//import kotlinx.android.synthetic.main.activity_main.btn_text_12
//import kotlinx.android.synthetic.main.activity_main.btn_text_13
//import kotlinx.android.synthetic.main.activity_main.btn_text_14
//import kotlinx.android.synthetic.main.activity_main.btn_text_15
//import kotlinx.android.synthetic.main.activity_main.btn_text_16
//import kotlinx.android.synthetic.main.activity_main.btn_text_2
//import kotlinx.android.synthetic.main.activity_main.btn_text_3
//import kotlinx.android.synthetic.main.activity_main.btn_text_4
//import kotlinx.android.synthetic.main.activity_main.btn_text_5
//import kotlinx.android.synthetic.main.activity_main.btn_text_6
//import kotlinx.android.synthetic.main.activity_main.btn_text_7
//import kotlinx.android.synthetic.main.activity_main.btn_text_8
//import kotlinx.android.synthetic.main.activity_main.btn_text_9
//import kotlinx.android.synthetic.main.activity_main.et_count
import kotlinx.android.synthetic.main.content_matching_cards.*
import java.lang.Long.toString
import java.util.Arrays.toString

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}


class cardActivity : Activity() {
    var fruits = arrayOfNulls<Drawable>(16)
    //var fruitNum = arrayOfNulls<Int>(16)
    var fruitNum : Array<Int> = arrayOf(1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8)
    var matchCount : Int = 0
    var successIndex :Array<Int> = arrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    var cancel = 0
    var backgr = arrayOfNulls<TextView>(1)
    var include = arrayOfNulls<View>(2)
    var now_number: TextView? = null
    var button = arrayOfNulls<RelativeLayout>(16)
    var button_text = arrayOfNulls<TextView>(16)
    var count : TextView ?= null
    // var mTimeLeftInMillis : Long = 1200000;
    var ok_button: RelativeLayout? = null

    var openCount : Int = 0
    var button1_index : Int = 0
    var button2_index : Int = 0


    //private CountDownTimer mCountDown = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_cards)
        all_view()
        mixingCard()
        //initFruitNum()

        Handler().postDelayed({
            initBg()
            timer.start()
            all_button_click()
        }, 2000)
        //initBg()
        // timer.start()
        //include[0]!!.visibility = View.VISIBLE
        // include[1]!!.visibility = View.GONE
        // game()
        //all_button_click()
    }

    var number_group1 = 0
    var number_group2 = 0
    private fun game() {
        mixingCard()
    }

    var match_number_int = 1
    var random_button_number_int = 0
    var button_number_01 = IntArray(16)
    var button_number_02 = IntArray(16)
    //    private fun random() {
////        val random = Random()
////        random_button_number_int = random.nextInt(16)
////        if (number_group1 == 1 && button_number_01[random_button_number_int] == 0) {
////            button_number_01[random_button_number_int] = number_group2
////        } else if (number_group1 == 1 && number_group2 <= 16) {
////            random()
////        }
////        if (number_group1 == 2 && button_number_02[random_button_number_int] == 0) {
////            button_number_02[random_button_number_int] = number_group2 + 16
////        } else if (number_group1 == 2 && number_group2 <= 16) {
////            random()
////        }
//
//    }
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

//    private fun initFruitNum(){
//        for (i in 0..7){
//            fruitNum[i] = i+1
//        }
//        for (i in 0..7){
//            fruitNum[i+8] = i+1
//        }
//    }

    private fun all_view() {
        // include[0] = findViewById(R.id.activity_main_view)
        // include[1] = findViewById(R.id.include_success_view)
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
//        button[16] = btn_17
//        button[17] = btn_18
//        button[18] = btn_19
//        button[19] = btn_20
//        button[20] = btn_21
//        button[21] = btn_22
//        button[22] = btn_23
//        button[23] = btn_24
//        button[24] = btn_25
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
//        button_text[16] = btn_text_17
//        button_text[17] = btn_text_18
//        button_text[18] = btn_text_19
//        button_text[19] = btn_text_20
//        button_text[20] = btn_text_21
//        button_text[21] = btn_text_22
//        button_text[22] = btn_text_23
//        button_text[23] = btn_text_24
//        button_text[24] = btn_text_25
        backgr[0] = bg
        count = et_count
        //ok_button = findViewById<View>(R.id.ok_button) as RelativeLayout
    }

    var button_num = 0
    private fun all_button_click() {
//        for (i in 0..15) {
//            button[i]!!.setOnClickListener {
//                if (match_number_int == 50) {
//                    //include[1]!!.visibility = View.VISIBLE
//                    // include[0]!!.visibility = View.GONE
//                    // dialog.setMessage("성공했어!!")
//                }
//                if (match_number_int == button_number_01[i]) {
//                    match_number_int += 1
//                    button_text[i]
//                        ?.setText(Integer.toString(button_number_02[i]))
//                }
//                if (match_number_int == button_number_02[i]) {
//                    match_number_int += 1
//                    button_text[i]!!.visibility = View.GONE
//                }
//
//            }
//        }
        for ( i in 0..15) {
            button[i]!!.setOnClickListener {
//                button[i]!!.background = fruits[i]
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
                                Log.d("버튼 1의 인덱스값 : ", Integer.toString(button1_index))
                                Log.d("버튼 2의 인덱스값 : ", Integer.toString(button2_index))
                                Log.d("다를때 버튼 1의 bg값 : ", Integer.toString(fruitNum[button1_index]))
                                Log.d("다를때 버튼 2의 bg값 : ", Integer.toString(fruitNum[button2_index]))
                                button[button1_index]!!.background = backgr[0]!!.background
                                button[button2_index]!!.background = backgr[0]!!.background
                                openCount = 0
                            }
                            if (fruitNum[button1_index] == fruitNum[button2_index]) {
                                Log.d("버튼 1의 인덱스값 : ", Integer.toString(button1_index))
                                Log.d("버튼 2의 인덱스값 : ", Integer.toString(button2_index))
                                Log.d("같을때 버튼 1의 bg값 : ", Integer.toString(fruitNum[button1_index]))
                                Log.d("같을때 버튼 2의 bg값 : ", Integer.toString(fruitNum[button2_index]))
                                button[button1_index]!!.background = fruits[button2_index]
                                button[button2_index]!!.background = fruits[button1_index]
                                matchCount++
                                successIndex[button2_index] = 1
                                successIndex[button1_index] = 1
                                openCount = 0
                                Log.d("matchcount : ", Integer.toString(matchCount))

                                if(matchCount == 8){
                                    var dialog = AlertDialog.Builder(this@cardActivity)
                                    dialog.setMessage("성공했어!!")
                                    // dialog.setMessage(login)
                                    dialog.show()
                                }
                            }
                            openCount = 0
                        }, 600)
                    }

                }

            }
        }
    }
    val timer = object: CountDownTimer(10000, 10){
        override fun onTick(millisUntilFinished: Long){
            // mTimeLeftInMillis = millisUntilFinished
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {


            var dialog = AlertDialog.Builder(this@cardActivity)
            dialog.setMessage("성공했어!!")
            // dialog.setMessage(login)
            dialog.show()

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