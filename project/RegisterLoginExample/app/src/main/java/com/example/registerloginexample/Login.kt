package com.example.registerloginexample

data class Login(
    //var success: String
    var userID: String,
    var userPassword: String
)

data class Success(
    var success: Int
)