package com.example.registerloginexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_initial.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class InitialActivity : AppCompatActivity() {

    var login:Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        //"http://api.odyssea-ogc.com:8080/server/User_management/signup.php"
        //"http://whqkek2.dothome.co.kr/"
        var retrofit = Retrofit.Builder()
            .baseUrl("http://api.odyssea-ogc.com:8080/server/User_management/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService: LoginService = retrofit.create(LoginService::class.java)



        btn_login.setOnClickListener{
            var text1 = et_id.text.toString()
            var text2 = et_pass.text.toString()



            loginService.requestLogin(text1, text2).enqueue(object:Callback<Login>{
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    var dialog = AlertDialog.Builder(this@InitialActivity)
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    var login = response.body()

                    if(Integer.parseInt(login?.userID) == 1) {
                        //val nextIntent = Intent(this@InitialActivity, MatchingCard::class.java)
                        val nextIntent = Intent(this@InitialActivity, shi::class.java)
                        startActivity(nextIntent)
                    }
                    else if(Integer.parseInt(login?.userID) == 0) {
                         var dialog = AlertDialog.Builder(this@InitialActivity)
                         dialog.setMessage("로그인에 실패했습니다")
                        dialog.show()
                    }
                    }


            })

        }//btn_login의 끝

        btn_register.setOnClickListener{
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)

        }

    }
}

