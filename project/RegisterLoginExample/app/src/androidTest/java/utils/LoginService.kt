package utils

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService{

    @FormUrlEncoded
    @POST("")
    fun requestLogin(
        @Field("userid") userid:String,
        @Field("userpass") userpass:String
    ) : Call<Login>
}