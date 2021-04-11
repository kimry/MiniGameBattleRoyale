package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kim.mini.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //뷰 바인딩 함수
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //레트로핏
        var retrofit = Retrofit.Builder()
                .baseUrl("http://api.odyssea-ogc.com:8080/server/User_management/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var loginService = retrofit.create(LoginService::class.java)



        //로그인 버튼 클릭
        binding.btnLogin.setOnClickListener{
            var textID = binding.editID.text.toString()
            var textPW = binding.editPW.text.toString()
            loginService.requestLogin(textID,textPW).enqueue(object:Callback<code>{
                override fun onResponse(call: Call<code>, response: Response<code>) {
                    //웹통신 성공
                    binding.editID.setText("")
                    binding.editPW.setText("")
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    var code = response.body()


                    if(code?.code==1) { //아이디와 비밀번호 일치

                        val serviceIntent = Intent(this@MainActivity,ConnectionService::class.java)
                        serviceIntent.action = ConnectionService.ACTION_CONNECTION
                        serviceIntent.putExtra("userID",textID)
                        startService(serviceIntent)

                        val nextIntent = Intent(this@MainActivity, LobbyActivity::class.java)
                        startActivity(nextIntent)

                        finish()
                    }
                    else {//아이디와 비밀번호 불일치
                        dialog.setTitle("아이디 비밀번호 불일치")
                        dialog.setMessage("아이디와 패스워드를 다시한번 확인해 주세요")
                        dialog.show()
                    }


                }
                override fun onFailure(call: Call<code>, t: Throwable) {
                    //웹통신 실패
                    binding.editID.setText("")
                    binding.editPW.setText("")
                    var dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("알람!")
                    dialog.setMessage("웹통신에 실패하였습니다.")
                    dialog.show()
                }

            })
        }
        binding.btnSignup.setOnClickListener{ // 회원가입 버튼 클릭
            val nextIntent = Intent(this,SignupActivity::class.java)
            startActivity(nextIntent)

            finish()
        }
    }
}