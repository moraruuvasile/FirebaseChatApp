package com.example.firebasechatapp.Util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FireObj {
    var firebaseInstance = FirebaseAuth.getInstance()
    var user: FirebaseUser? = null
    lateinit var userId: String
    lateinit var refUser: DatabaseReference
    lateinit var refAllUsers: DatabaseReference



    fun userInit(): FirebaseUser? {
         this.user = firebaseInstance.currentUser
        return this.user
    }


    fun userIdInit() {
        userId = firebaseInstance.currentUser!!.uid
    }

    fun refUserInit() {
        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

    }

    fun refAllfUsersInit() {
        refAllUsers = FirebaseDatabase.getInstance().reference.child("Users")

    }


}