package com.example.registerloginexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.activity_initial.btn_register
import kotlinx.android.synthetic.main.activity_initial.et_id
import kotlinx.android.synthetic.main.activity_initial.et_pass
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://api.odyssea-ogc.com:8080/server/User_management/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var registerService: RegisterService = retrofit.create(RegisterService::class.java)



        btn_register.setOnClickListener {
            var text1 = et_id.text.toString()
            var text2 = et_pass.text.toString()
            var text3 = et_pass2.text.toString()


            if (text3 == text2){

                registerService.requestRegister(text1, text2).enqueue(object : Callback<Login> {
                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        var login = response.body()


                        if(Integer.parseInt(login?.userID) == 1) {
                            var dialog = AlertDialog.Builder(this@RegisterActivity)
                            dialog.setMessage("회원가입에 성공했습니다.")
                            dialog.show()

                            Handler().postDelayed({
                                val nextIntent = Intent(this@RegisterActivity, InitialActivity::class.java)
                                startActivity(nextIntent)
                            }, 1500)
                        }
                        else if(Integer.parseInt(login?.userID) == 0) {
                            var dialog = AlertDialog.Builder(this@RegisterActivity)
                            dialog.setMessage("아이디가 중복되어 회원가입에 실패했습니다")
                            dialog.show()
                        }
                    }

                })
        }
            if(text3 != text2){
                var dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("에러")
                dialog.setMessage("비밀번호를 다시 확인해주세요.")
                dialog.show()
            }
        }//


    }
}

