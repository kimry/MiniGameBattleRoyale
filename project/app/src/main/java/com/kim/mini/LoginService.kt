package com.kim.mini
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @FormUrlEncoded
    @POST("login.php")
    fun requestLogin(
            @Field("userID") userID : String,
            @Field("userPW") userPW : String
    )   :   Call<code>
}