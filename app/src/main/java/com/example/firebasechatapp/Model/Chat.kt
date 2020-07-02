package com.example.firebasechatapp.Model

data class Chat (val sender:String = "",
            val message:String = "",
            val receiver:String = "",
            val isSeen:Boolean = false,
            val url:String = "",
            val messageId:String = ""){

    fun getIsSeen() = isSeen

}