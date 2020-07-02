package com.example.firebasechatapp.Notification

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    val retrofit = Retrofit.Builder()
        .baseUrl("vasea")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
