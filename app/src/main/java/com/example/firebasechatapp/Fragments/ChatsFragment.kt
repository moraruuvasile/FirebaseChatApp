package com.example.firebasechatapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapp.Adapters.SearchAdapter
import com.example.firebasechatapp.Model.ChatList
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment() {
    lateinit var userAdapter: SearchAdapter
    val listUsers = ArrayList<User>()
    val usersChatList = ArrayList<ChatList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = SearchAdapter(listUsers, true)
        recycler_chatFrag.apply {
            adapter = userAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        getUserChatListAndListUsers()

    }

    private fun getUserChatListAndListUsers() {
        FireObj.refChatList().addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                usersChatList.clear()
                for (chats in snapshot.children){
                    usersChatList.add(chats.getValue(ChatList::class.java)!!)
                }
                getListUsers()
            }
        })
    }

    private fun getListUsers() {
       FireObj.refAllUsers.addValueEventListener(object : ValueEventListener{
           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

           override fun onDataChange(snapshot: DataSnapshot) {
              listUsers.clear()
               for (users in snapshot.children){
                   val user = users.getValue(User::class.java)
                   for (eachChat in usersChatList){
                       if (user?.uid == eachChat.id){
                           listUsers.add(user)
                       }
                   }
               }
               userAdapter.notifyDataSetChanged()
           }
       })
    }
}