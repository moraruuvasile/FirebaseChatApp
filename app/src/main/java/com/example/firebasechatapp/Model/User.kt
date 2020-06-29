package com.example.firebasechatapp.Model


data class User(val uid:String = "",
                val username:String= "",
                val profile:String="",
                val cover:String="",
                val status:String="",
                val search: String = username.toLowerCase()
) {

}




