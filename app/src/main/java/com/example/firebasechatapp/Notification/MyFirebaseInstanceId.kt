package com.example.firebasechatapp.Notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceId :FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        val fireBaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token

        fireBaseUser?.let { updateToken(refreshToken) }
    }

    private fun updateToken(refreshToken: String?) {
        val fireBaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference().child("Tokens")
        val token = Token(refreshToken)
        ref.child(fireBaseUser).setValue(token)
    }


    class Token(val token: String?){}
}