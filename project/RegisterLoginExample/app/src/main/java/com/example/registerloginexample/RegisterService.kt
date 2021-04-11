package com.example.registerloginexample

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface RegisterService{

    //login.php , signup.php
    @FormUrlEncoded
    @POST("signup.php")
    fun requestRegister(
        @Field("userID") userID:String,
        @Field("userPassword") userPassword:String
    ): Call<Login>
}