package com.example.firebasechatapp.Util

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
    fun refXUser(str: String): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("Users").child(str)

    }

    fun refChatList(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("ChatList")
            .child(userId)

    }

    fun refChatListReceiver(userIdVisit: String): Task<Void> {
        return FirebaseDatabase.getInstance().reference.child("ChatList")
            .child(userIdVisit)
            .child(userId)
            .child("id")
            .setValue(userId)
    }


    fun refAllfUsersInit() {
        refAllUsers = FirebaseDatabase.getInstance().reference.child("Users")
    }

    fun refSendMessage(hashMap: HashMap<String, Any>): Task<Void> {

        hashMap["messageId"] = key()
        return FirebaseDatabase.getInstance().reference.child("Chats")
            .child(key())
            .updateChildren(hashMap)
        //           .setValue(hashMap)
    }

    fun key():String = FirebaseDatabase.getInstance().reference.push().key!!

    fun storageRef(str: String): StorageReference {
       return FirebaseStorage.getInstance().reference.child(str)
    }

    fun refToChats(): DatabaseReference {
        return Firebase.database.reference.child("Chats")
    }

}