package com.kim.mini

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.kim.mini.databinding.ActivityShisenBinding
import java.util.*

class shisenActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShisenBinding

    private lateinit var serviceIntent : Intent

    var success: Int = 0
    var route = mutableListOf<Int>(0,0) // 경로를 나타내는 변수
    var turningPoint = mutableListOf<Int>(0,0) // 꺽이는 부분을 나타내는 변수 && turningPoint[0]과 turningPoint[1]은 둘다 0인 garbage값
    var turnDir = IntArray(3) // 몇번 꺽이는지와 어떻게 꺽이는지 나타내는 변수
    var exitSign = 0 // 게임이 끝난지 확인하기 위한 변수. 10이되면 모든 짝을 맞춘거기 때문에 게임을 성공적으로 끝낸거다.


    var mapp = Array<IntArray>(6,{ IntArray(7) })
    var tmapp = Array<IntArray>(6,{ IntArray(7) })
    var opencount : Int = 0

    var Button = Array<Array<RelativeLayout?>>(7, { arrayOfNulls<RelativeLayout>(7) })



    var count: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shisen)

        binding = ActivityShisenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(this, ConnectionService::class.java)

        set_bg()
        changeMap()
        timer.start()
        all_button_click()

    }
    private fun set_bg() {


        Button[0][0] = binding.btn0
        Button[0][1] = binding.btn1
        Button[0][2] = binding.btn2
        Button[0][3] = binding.btn3
        Button[0][4] = binding.btn4
        Button[0][5] = binding.btn5
        Button[0][6] = binding.btn6

        Button[1][0] = binding.btn10
        Button[1][1] = binding.btn11
        Button[1][2] = binding.btn12
        Button[1][3] = binding.btn13
        Button[1][4] = binding.btn14
        Button[1][5] = binding.btn15
        Button[1][6] = binding.btn16

        Button[2][0] = binding.btn20
        Button[2][1] = binding.btn21
        Button[2][2] = binding.btn22
        Button[2][3] = binding.btn23
        Button[2][4] = binding.btn24
        Button[2][5] = binding.btn25
        Button[2][6] = binding.btn26

        Button[3][0] = binding.btn30
        Button[3][1] = binding.btn31
        Button[3][2] = binding.btn32
        Button[3][3] = binding.btn33
        Button[3][4] = binding.btn34
        Button[3][5] = binding.btn35
        Button[3][6] = binding.btn36

        Button[4][0] = binding.btn40
        Button[4][1] = binding.btn41
        Button[4][2] = binding.btn42
        Button[4][3] = binding.btn43
        Button[4][4] = binding.btn44
        Button[4][5] = binding.btn45
        Button[4][6] = binding.btn46

        Button[5][0] = binding.btn50
        Button[5][1] = binding.btn51
        Button[5][2] = binding.btn52
        Button[5][3] = binding.btn53
        Button[5][4] = binding.btn54
        Button[5][5] = binding.btn55
        Button[5][6] = binding.btn56

        Button[6][0] = binding.line12
        Button[6][1] = binding.line14
        Button[6][2] = binding.line21
        Button[6][3] = binding.line32
        Button[6][4] = binding.line8
        Button[6][5] = binding.line6
        Button[6][6] = binding.line6

        Button[3][1]!!.background = Button[1][1]!!.background
        Button[3][2]!!.background = Button[1][2]!!.background
        Button[3][3]!!.background = Button[1][3]!!.background
        Button[3][4]!!.background = Button[1][4]!!.background
        Button[3][5]!!.background = Button[1][5]!!.background

        Button[4][1]!!.background = Button[2][1]!!.background
        Button[4][2]!!.background = Button[2][2]!!.background
        Button[4][3]!!.background = Button[2][3]!!.background
        Button[4][4]!!.background = Button[2][4]!!.background
        Button[4][5]!!.background = Button[2][5]!!.background


        mapp[0][0] = 0
        mapp[0][1] = 0
        mapp[0][2] = 0
        mapp[0][3] = 0
        mapp[0][4] = 0
        mapp[0][5] = 0
        mapp[0][6] = 0

        mapp[1][0] = 0
        mapp[1][1] = 1
        mapp[1][2] = 2
        mapp[1][3] = 3
        mapp[1][4] = 4
        mapp[1][5] = 5
        mapp[1][6] = 0

        mapp[2][0] = 0
        mapp[2][1] = 6
        mapp[2][2] = 7
        mapp[2][3] = 8
        mapp[2][4] = 9
        mapp[2][5] = 10
        mapp[2][6] = 0

        mapp[3][0] = 0
        mapp[3][1] = 1
        mapp[3][2] = 2
        mapp[3][3] = 3
        mapp[3][4] = 4
        mapp[3][5] = 5
        mapp[3][6] = 0

        mapp[4][0] = 0
        mapp[4][1] = 6
        mapp[4][2] = 7
        mapp[4][3] = 8
        mapp[4][4] = 9
        mapp[4][5] = 10
        mapp[4][6] = 0

        mapp[5][0] = 0
        mapp[5][1] = 0
        mapp[5][2] = 0
        mapp[5][3] = 0
        mapp[5][4] = 0
        mapp[5][5] = 0
        mapp[5][6] = 0

        turnDir[0] = 0
        turnDir[1] = 0
        turnDir[2] = 0



        count = binding.etCount
    }



    var temp_Button = arrayOfNulls<RelativeLayout>(2)
    var indexI : Int = -1
    var indexJ : Int = -1

    private fun all_button_click() {
        for(i in 1..4){
            for(j in 1..5){
                if(Button[i][j]!!.background != Button[0][0]!!.background){ //지워진 그림에서는 버튼이 클릭되지 않게함.
                    Button[i][j]!!.setOnClickListener{
                        opencount++

                        if(opencount == 1){// 버튼이 한 번 클릭 되었을때 그때의 index i,j를 저장함
                            temp_Button[0] = Button[i][j]
                            indexI = i
                            indexJ = j
                        }
                        if(opencount == 2){//버튼이 2번 클릭 되었을때 첫번째와 두번째 클릭된 버튼의 이미지를 같은지 체크함
                            temp_Button[1] = Button[i][j]
                            opencount = 0

                            if(Button[indexI][indexJ]!!.background == Button[i][j]!!.background){
                                if(indexI == i && indexJ == j){// 같은 버튼을 두 번 클릭할때를 제외하기 위한 if문
                                    opencount =0
                                    indexI = -1
                                    indexJ = -1
                                }
                                else { //클릭된 두 개의 버튼의 이미지가 같을때(물론 같은 버튼이 두번 클릭 되는 경우는 제외)
                                    //Button[indexI][indexJ]!!.background = Button[0][0]!!.background
                                    //Button[i][j]!!.background = Button[0][0]!!.background

                                    tmapp[indexI][indexJ] = -1
                                    tmapp[i][j] = -1
                                    dfs2(indexI, indexJ, i, j, indexI, indexJ, 0, 0)
                                    if(success == 1){

//                                        Handler().postDelayed({
//                                            Button[indexI][indexJ]!!.background = Button[0][0]!!.background
//                                            Button[i][j]!!.background = Button[0][0]!!.background
//
//                                        }, 1000)

                                        mapp[indexI][indexJ] = 0
                                        mapp[i][j] = 0

                                        route[0] = indexI
                                        route[1] = indexJ

                                        //아래를 다시 대입하기 전에 -2라는 단서를 가지고 경로를 뽑아내야되는데 어떻게 해야될까??

                                        changeLine()

                                        //tmapp에 다시 mapp을 대입.
                                        changeMap()

                                        success = 0
                                        exitSign++

                                        if(exitSign == 10){
                                            Toast.makeText(this,"Win!!", Toast.LENGTH_SHORT).show();
                                            timer.cancel()
                                            serviceIntent.action = ConnectionService.ACTION_GAMECLEAR
                                            serviceIntent.putExtra("game","ss")

                                            startService(serviceIntent)
                                            Handler().postDelayed({
                                                val nextIntent = Intent(this, LobbyActivity::class.java)
                                                startActivity(nextIntent)

                                                finish()
                                            }, 1500)
                                        }


                                    }
                                    else if(success == 0){
                                        tmapp[indexI][indexJ] = mapp[indexI][indexJ]
                                        tmapp[i][j] = mapp[i][j]
                                    }
                                    godsu()


                                    Handler().postDelayed({ //changeLine함수에 있는 delay 핸들러때문에 시간을 조절하지않으면 먼저 route값이 초기화되어 오류가 발생
                                        resizeRoute()
                                    }, 150) //150이 적당해보임

                                    resizeTurningPoint()

                                    turnDir[0] = 0
                                    turnDir[1] = 0
                                    turnDir[2] = 0


                                    indexI = -1
                                    indexJ = -1
                                }
                            }
                            else if(temp_Button[0]!!.background != temp_Button[1]!!.background)
                            {opencount = 0}


                        }



                    }//clickListener 끝나는 부분
                }// 이중 for문 안의 이미지 버튼인지 확인하는 if문 안의 부분
            }
        }
    }



    private fun dfs2(x : Int,y : Int ,z : Int ,w : Int, a : Int, b : Int, dir: Int, turn : Int ) {

        //dir은 그전에서 어느방향으로 갔는지를 따지는 변수(1: 위, 2: 오른쪽, 3: 아래, 4: 왼쪽), 0:맨 처음시작.
        //turn 지금까지 몇번꺽었는지를 나타내는 변수 0~2까지 가능


        //함수에서 사용할 변수 turnDir 은 총 6가지 경우가 있다.
        //21 : 오른쪽에서 위로
        //41 : 왼쪽에서 위로
        //23 : 오른쪽에서 아래로
        //43 : 왼쪽에서 아래로
        //12 : 위쪽에서 오른쪽으로
        //14 : 위쪽에서 왼쪽으로
        //32 : 아래쪽에서 오른쪽으로
        //34 : 아래쪽에서 왼쪽으로

        Log.i("dir","x :"+x+"  y : "+ y)

        if(turn >2){
            return
        }

        //위가 목적지면
        Log.i("mooth","위")
        if(x>0 && (a != x-1 || b != y) && tmapp[x-1][y] == -1){
            if(dir ==1 || (dir != 1 && turn <= 1)) {

//                Handler().postDelayed({
//                    Button[a][b]!!.background = Button[0][0]!!.background
//                    Button[z][w]!!.background = Button[0][0]!!.background
//
//                }, 150)
                Button[a][b]!!.background = Button[0][0]!!.background
                Button[z][w]!!.background = Button[0][0]!!.background

                tmapp[a][b] = 0
                tmapp[z][w] = 0

                route.add(x-1)
                route.add(y)

                success = 1

                if(dir ==2){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 21
                }
                else if(dir==4){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 41
                }
            }

            return }
        //오른쪽이 목적지면
        Log.i("mooth","오른쪽")
        if(y<6 && (a != x || b != y+1) &&tmapp[x][y+1] == -1){ // 오른쪽이 목적지일때
            if(dir ==2 || (dir != 2 && turn <=1)) {

//                Handler().postDelayed({
//                    Button[a][b]!!.background = Button[0][0]!!.background
//                    Button[z][w]!!.background = Button[0][0]!!.background
//
//                }, 150)
                Button[a][b]!!.background = Button[0][0]!!.background
                Button[z][w]!!.background = Button[0][0]!!.background

                tmapp[a][b] = 0
                tmapp[z][w] = 0

                success = 1

                route.add(x)
                route.add(y+1)

                if(dir ==1){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 12
                }
                else if(dir==3){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 32
                }
            }

            return }
        //아래가 목적지면
        Log.i("mooth","아래")
        if(x<5 && (a != x+1 || b != y) && tmapp[x+1][y] == -1){
            if(dir ==3 || (dir != 3 && turn <=1)) {
//                Handler().postDelayed({
//                    Button[a][b]!!.background = Button[0][0]!!.background
//                    Button[z][w]!!.background = Button[0][0]!!.background
//
//                }, 150)
                Button[a][b]!!.background = Button[0][0]!!.background
                Button[z][w]!!.background = Button[0][0]!!.background

                tmapp[a][b] = 0
                tmapp[z][w] = 0

                success = 1

                route.add(x+1)
                route.add(y)

                if(dir ==2){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 23
                }
                else if(dir==4){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 43
                }
            }

            return }
        //왼쪽이 목적지면
        Log.i("mooth","왼")
        if(y>0 && (a != x || b != y-1) && tmapp[x][y-1] == -1){
            if(dir == 4 ||(dir != 4 && turn <=1)) {
//                Handler().postDelayed({
//                    Button[a][b]!!.background = Button[0][0]!!.background
//                    Button[z][w]!!.background = Button[0][0]!!.background
//
//                }, 150)
                Button[a][b]!!.background = Button[0][0]!!.background
                Button[z][w]!!.background = Button[0][0]!!.background

                tmapp[a][b] = 0
                tmapp[z][w] = 0

                success = 1

                route.add(x)
                route.add(y-1)

                if(dir ==1){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 14
                }
                else if(dir==3){
                    turningPoint.add(x)
                    turningPoint.add(y)
                    turnDir[turn] = 34
                }
            }

            return }




        //위로 이동할 수 있으면
        if(x>0 && (tmapp[x-1][y] == 0 )&& success == 0) {
            Log.i("dir","위")


            route.add(x-1)
            route.add(y)
            tmapp[x][y] = -2

            if(dir == 1 || dir == 0) {
                dfs2(x - 1, y, z, w, a, b, 1, turn)
            }
            else{
                turningPoint.add(x) // dfs2 위에서 하나 아래에서 실행하나 다를거없을거 같은데 혹시 오류날 수도 있으니 주석달아둠
                turningPoint.add(y)

                if(dir== 2) turnDir[turn] = 21
                else if(dir ==4) turnDir[turn] = 41

                dfs2(x - 1, y, z, w, a, b, 1, turn+1)

                if(success != 1) {
                    turningPoint.removeAt(turningPoint.size - 1)
                    turningPoint.removeAt(turningPoint.size - 1)
                    turnDir[turn] = 0
                }


            }

            //경로를 나타낼 route의 마지막 인수 2개를 없앤다.
            if(success != 1) {
                route.removeAt(route.size - 1)
                route.removeAt(route.size - 1)
            }

        }

        //오른쪽으로 이동할 수 있으면
        if(y<6 && (tmapp[x][y+1] == 0) && success == 0) {
            Log.i("dir","오른쪽")


            route.add(x)
            route.add(y+1)
            tmapp[x][y] = -2

            if(dir == 2 || dir == 0) {
                dfs2(x , y+1, z, w, a, b, 2, turn)
            }
            else{
                turningPoint.add(x)
                turningPoint.add(y)
                if(dir== 1) turnDir[turn] = 12
                else if(dir ==3) turnDir[turn] = 32
                dfs2(x , y+1, z, w, a, b, 2, turn+1)

                if(success != 1) {
                    turningPoint.removeAt(turningPoint.size - 1)
                    turningPoint.removeAt(turningPoint.size - 1)
                    turnDir[turn] = 0
                }


            }


            //경로를 나타낼 route의 마지막 인수 2개를 없앤다.
            if(success != 1) {
                route.removeAt(route.size - 1)
                route.removeAt(route.size - 1)
            }
        }

        //아래로 이동할 수 있으면
        if(x<5 && (tmapp[x+1][y] == 0) && success == 0) {
            Log.i("dir","아래")


            route.add(x+1)
            route.add(y)
            tmapp[x][y] = -2

            if(dir == 3 || dir == 0) {
                dfs2(x + 1, y, z, w, a, b, 3, turn)
            }
            else{
                turningPoint.add(x)
                turningPoint.add(y)
                if(dir== 2) turnDir[turn] = 23
                else if(dir ==4) turnDir[turn] = 43
                dfs2(x + 1, y, z, w, a, b, 3, turn+1)

                if(success != 1) {
                    turningPoint.removeAt(turningPoint.size - 1)
                    turningPoint.removeAt(turningPoint.size - 1)
                    turnDir[turn] = 0
                }


            }


            //경로를 나타낼 route의 마지막 인수 2개를 없앤다.
            if(success != 1) {
                route.removeAt(route.size - 1)
                route.removeAt(route.size - 1)
            }
        }


        //왼쪽으로 이동할 수 있으면
        if(y>0 && (tmapp[x][y-1] == 0) && success ==0) {
            Log.i("dir","왼쪽")


            route.add(x)
            route.add(y-1)
            tmapp[x][y] = -2

            if(dir == 4 || dir == 0) {
                dfs2(x , y-1, z, w, a, b, 4, turn)
            }
            else{
                turningPoint.add(x)
                turningPoint.add(y)
                if(dir== 1) turnDir[turn] = 14
                else if(dir ==3) turnDir[turn] = 34
                dfs2(x , y-1, z, w, a, b, 4, turn+1)

                if(success != 1) {
                    turningPoint.removeAt(turningPoint.size - 1)
                    turningPoint.removeAt(turningPoint.size - 1)
                    turnDir[turn] = 0
                }


            }

            //경로를 나타낼 route의 마지막 인수 2개를 없앤다.
            if(success != 1) {
                route.removeAt(route.size - 1)
                route.removeAt(route.size - 1)

            }
        }


        tmapp[x][y] = mapp[x][y]
        return
    }

    private fun changeMap(){
        for(i in 0..5){
            for(j in 0..6){
                tmapp[i][j] = mapp[i][j]
            }
        }
    }



    private fun resizeRoute(){
        var n : Int = route.size-3
        for(i in 0..n){
            route.removeAt(route.size-1)
        }
        route[0] = 0
        route[1] = 0
    }
    private fun resizeTurningPoint(){
        var n : Int = turningPoint.size-3
        for(i in 0..n){
            turningPoint.removeAt(turningPoint.size-1)
        }
        turningPoint[0] = 0
        turningPoint[1] = 0
    }


    private fun godsu() : Int{
        var n : Int = 0
        if(turnDir[0] == 0) n =0
        else if(turnDir[1] ==0) n=1
        else  n =2

        for (i in 0..n) {
            Log.i("godsu", "n = $n, turnDir[$i] = ${turnDir[i]}")
        }
        return n

    }

    private fun changeLine(){
        var n : Int = 0
        var tempI : Int = 0

        n = godsu()

        if(n == 0){
            if(route[0] == route[route.size-2]){
                //경로상에 line6를 삽입해야된다
                for(i in 2..route.size-4 step 2){
                    Button[route[i]][route[i+1]]!!.background = Button[6][5]!!.background

                }
            }
            else if(route[1] == route[route.size-1]){
                //경로상에 line8를 삽입해야된다
                for(i in 2..route.size-4 step 2){
                    Button[route[i]][route[i+1]]!!.background = Button[6][4]!!.background

                }
            }

        }//if n==0일때의 끝

        else if(n==1){
            if(route[0] == route[2]){ //처음을 가로로 시작한 경우
                // 일단 완전 딱붙어 있는 경우는 배제하고 생각해서 짜는중 -> 나중에 그 부분 추가해야될듯
                //경로상에 line6를 삽입해야된다
                for(i in 2..route.size-4 step 2){
                    if(turningPoint[2] == route[i] && turningPoint[3] == route[i+1]){
                        if(turnDir[0] == 43) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[0] == 23) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[0] == 21) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[0] == 41) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][5]!!.background
                    }

                }//for문의 끝

                for(i in tempI .. route.size-4 step 2){
                    Button[route[i]][route[i + 1]]!!.background = Button[6][4]!!.background
                }

            }
            else if(route[1] == route[3]){//처음을 세로로 시작한 경우
                // 일단 완전 딱붙어 있는 경우는 배제하고 생각해서 짜는중 -> 나중에 그 부분 추가해야될듯
                //경로상에 line8를 삽입해야된다
                for(i in 2..route.size-4 step 2){
                    if(turningPoint[2] == route[i] && turningPoint[3] == route[i+1]){
                        if(turnDir[0] == 12) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[0] == 14) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[0] == 34) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[0] == 32) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][4]!!.background
                    }

                }//for문의 끝

                for(i in tempI .. route.size-4 step 2){
                    Button[route[i]][route[i + 1]]!!.background = Button[6][5]!!.background
                }
            }

            tempI = 0

        }//else if n==1일때의 끝

        else if(n ==2){
            if(route[0] == route[2]){ //처음을 가로로 시작한 경우
                // 일단 완전 딱붙어 있는 경우는 배제하고 생각해서 짜는중 -> 나중에 그 부분 추가해야될듯
                //경로상에 line6를 삽입해야된다
                for(i in 2..route.size-4 step 2){//첫번째 turningPoint까지의 for문
                    if(turningPoint[2] == route[i] && turningPoint[3] == route[i+1]){
                        if(turnDir[0] == 43) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[0] == 23) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[0] == 21) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[0] == 41) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][5]!!.background
                    }

                }//for문의 끝

                for(i in tempI .. route.size-4 step 2){//두번째 turnigPoint까지의 for문
                    if(turningPoint[4] == route[i] && turningPoint[5] == route[i+1]){
                        if(turnDir[1] == 12) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[1] == 14) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[1] == 34) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[1] == 32) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][4]!!.background
                    }

                }

                for(i in tempI .. route.size-4 step 2){
                    Button[route[i]][route[i + 1]]!!.background = Button[6][5]!!.background
                }

                tempI = 0

            }// n==2일때 가로로 시작한 경우의 끝

            else if(route[1] == route[3]){ //처음을 세로로 시작한 경우
                // 일단 완전 딱붙어 있는 경우는 배제하고 생각해서 짜는중 -> 나중에 그 부분 추가해야될듯
                //경로상에 line6를 삽입해야된다
                for(i in 2..route.size-4 step 2){
                    if(turningPoint[2] == route[i] && turningPoint[3] == route[i+1]){
                        if(turnDir[0] == 12) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[0] == 14) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[0] == 34) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[0] == 32) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][4]!!.background
                    }

                }//for문의 끝

                for(i in tempI .. route.size-4 step 2){
                    if(turningPoint[4] == route[i] && turningPoint[5] == route[i+1]){
                        if(turnDir[1] == 43) Button[route[i]][route[i + 1]]!!.background = Button[6][0]!!.background
                        else if(turnDir[1] == 23) Button[route[i]][route[i + 1]]!!.background = Button[6][1]!!.background
                        else if(turnDir[1] == 21) Button[route[i]][route[i + 1]]!!.background = Button[6][2]!!.background
                        else if(turnDir[1] == 41) Button[route[i]][route[i + 1]]!!.background = Button[6][3]!!.background

                        tempI = i + 2
                        break
                    }
                    else {
                        Button[route[i]][route[i + 1]]!!.background = Button[6][5]!!.background
                    }

                }

                for(i in tempI .. route.size-4 step 2){
                    Button[route[i]][route[i + 1]]!!.background = Button[6][4]!!.background
                }

                tempI = 0

            }// n==2일때 세로로 시작한 경우의 끝
        }// n ==2 일때의 끝

        //바꾼 line이미지들을 잠시 후 다시 원래의 배경으로 바꿔주는 부분을 여기에 작성해야됨. route를 따라가면서 다시 전부 원래 배경으로 바꿔주면 될듯?
        //for문을 delay있게 실행하기만 하면 됨

        Handler().postDelayed({
            Log.i("holly","delay")
            if(route.size != 0){
                for (i in 2..route.size - 4 step 2) {
                    Button[route[i]][route[i + 1]]!!.background = Button[0][0]!!.background
                }
            }

        }, 150)




    }//chanLine함수의 끝


    val timer = object: CountDownTimer(60000, 10){
        override fun onTick(millisUntilFinished: Long){
            updateCountDownText(millisUntilFinished)
        }

        override fun onFinish() {


            Toast.makeText(this@shisenActivity,"Lose!!", Toast.LENGTH_SHORT).show();
            if(exitSign != 8) {
                Handler().postDelayed({
                    val nextIntent = Intent(this@shisenActivity, LobbyActivity::class.java)
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
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when(intent?.getStringExtra("command")) {
            "endGame" -> endGame()
        }
    }
    fun endGame() {
        Toast.makeText(this,"Lose!!", Toast.LENGTH_SHORT).show();
        Handler().postDelayed({
            timer.cancel()
            val nextIntent = Intent(this, LobbyActivity::class.java)
            startActivity(nextIntent)
            finish()
        },1500)
    }

}