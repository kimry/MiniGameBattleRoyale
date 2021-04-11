package com.kim.mini

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kim.mini.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    //뷰 바인딩 함수
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //뷰 바인딩
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //레트로핏
        var retrofit = Retrofit.Builder()
                .baseUrl("http://api.odyssea-ogc.com:8080/server/User_management/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var signupService = retrofit.create(SignupService::class.java)
        var dialog = AlertDialog.Builder(this@SignupActivity)


        //회원가입 버튼 클릭
        binding.btnSignup.setOnClickListener{
            var textID = binding.editID.text.toString()
            var textPW1 = binding.editPW1.text.toString()
            var textPW2 = binding.editPW2.text.toString()

            if(textPW1==textPW2) {
                //PW가 일치할 경우
                signupService.requestSingnup(textID,textPW1).enqueue(object:Callback<code>{
                    override fun onResponse(call: Call<code>, response: Response<code>) {
                        // 웹통신 성공
                        var code = response.body()
                        if(code?.code==1) {
                            //아이디 생성
                            val nextIntent = Intent(this@SignupActivity, MainActivity::class.java)
                            startActivity(nextIntent)

                            finish()
                        }
                        else {
                            // 동일 아이디 존재
                            binding.editID.setText("")
                            binding.editPW1.setText("")
                            binding.editPW2.setText("")
                            dialog.setTitle("동일 아이디 존재")
                            dialog.setMessage("이미 동일한 아이디가 존재합니다.")
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<code>, t: Throwable) {
                        //웹통신 실패
                        binding.editID.setText("")
                        binding.editPW1.setText("")
                        binding.editPW2.setText("")
                        dialog.setTitle("서버 연결 실패")
                        dialog.setMessage("서버와의 연결에 실패하였습니다.")
                        dialog.show()
                    }

                })
            }
            else
            {
                //PW가 일치하지 않을 경우
                binding.editID.setText("")
                binding.editPW1.setText("")
                binding.editPW2.setText("")
                dialog.setTitle("PW 불일치")
                dialog.setMessage("PW가 일치하지 않습니다.")
                dialog.show()
            }
        }
        binding.btnComeback.setOnClickListener {
            //돌아가기 버튼 클릭
            val nextIntent = Intent(this@SignupActivity, MainActivity::class.java)
            startActivity(nextIntent)

            finish()
        }
    }
}