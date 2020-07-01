package com.example.firebasechatapp.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapp.Adapters.ChatsAdapter
import com.example.firebasechatapp.MainActivity
import com.example.firebasechatapp.Model.Chat
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.Const
import com.example.firebasechatapp.Util.FireObj
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity : AppCompatActivity() {
    private val REQUEST_CODE: Int = 1990
    lateinit var receiverId: String
    lateinit var chatsAdapter: ChatsAdapter
    private var listMessage = ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        setSupportActionBar(toolbar_main)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_main.setNavigationOnClickListener{
            val intent = Intent(this@MessageChatActivity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        receiverId = intent.getStringExtra(Const.VISIT_ID)!!

        send_btn.bringToFront()

        setUserNameAndAvatarAndRecyclerView()
        send_btn.setOnClickListener {
            sendMessageToUser()
        }
        attach_btn.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(Intent.createChooser(intent, "Pick Image"), REQUEST_CODE)
        }

        receiveMessages()

    }



    private fun setUserNameAndAvatarAndRecyclerView() {
        FireObj.refXUser(receiverId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            profile_name.text = it.username
                            Picasso.get().load(it.profile)
                                .placeholder(R.drawable.ic_baseline_face_24)
                                .into(settings_image)

                            setRecyclerView(user.profile)

                        }
                    }
                }
            }
            )
    }

    private fun setRecyclerView(profile: String) {
        chatsAdapter = ChatsAdapter(listMessage, profile)

        message_recycler.apply {
            val layout = LinearLayoutManager(context)
            layout.stackFromEnd = true
            layoutManager = layout
            adapter = chatsAdapter
        }

    }

    private fun receiveMessages() {
        FireObj.refToChats().addValueEventListener(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                  listMessage.clear()
                    for (chatRaw in snapshot.children){
                        val chat = chatRaw.getValue(Chat::class.java)

                        if (chat?.receiver == FireObj.userId && chat.sender == receiverId
                            || chat?.receiver == receiverId && chat.sender == FireObj.userId ){
                            listMessage.add(chat)
                        }
                    }
                    chatsAdapter.notifyDataSetChanged()
                    message_recycler.scrollToPosition(listMessage.size-1)
                }

            })
    }


    private fun sendMessageToUser() {
        val messageHashMap = HashMap<String, Any>()
        messageHashMap["sender"] = FireObj.userId
        messageHashMap["message"] = edit_mesage.text.toString()
        messageHashMap["receiver"] = receiverId
        messageHashMap["isSeen"] = false
        messageHashMap["url"] = ""
        edit_mesage.setText("")

        FireObj.refSendMessage(messageHashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FireObj.refChatList().child(receiverId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                Log.d("adasdas", "onDataChange:snapshot ")
                                FireObj.refChatList().child(receiverId).child("id").setValue(receiverId)
                                FireObj.refChatListReceiver(receiverId)
                            }
                        }
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data!!.data != null
        ) {
            val filePath = FireObj.storageRef("Chat Images").child("${FireObj.key()}.jpg")
            val uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(data.data!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val messageHashMap = HashMap<String, Any>()
                    messageHashMap["sender"] = FireObj.userId
                    messageHashMap["message"] = "vaSea"
                    messageHashMap["receiver"] = receiverId
                    messageHashMap["isSeen"] = false
                    messageHashMap["url"] = task.result.toString()
                    FireObj.refSendMessage(messageHashMap)
                }
            }
        }
    }
}
