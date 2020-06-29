package com.example.firebasechatapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapp.Adapters.SearchAdapter
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    private lateinit var searchAdapter: SearchAdapter
    private var listUsers = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchAdapter(listUsers, false)
        search_recycler.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }

        retriveAllUsers()

        search_users.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(s.toString().toLowerCase())
            }
        })

    }

    private fun searchForUsers(str: String) {
        FireObj.refAllUsers.orderByChild("search")
            .startAt(str)
            .endAt(str+"\uf8ff")
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    listUsers.clear()
                    for (userRaw in snapshot.children) {
                        val user = userRaw.getValue(User::class.java)
                        if (user?.uid != FireObj.userId) { //userId initialized in MainActivity
                            user?.let { listUsers.add(it) }
                        }
                    }
                    searchAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun retriveAllUsers() {
        FireObj.refAllfUsersInit()
        FireObj.refAllUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUsers.clear()
                for (userRaw in snapshot.children) {
                    val user = userRaw.getValue(User::class.java)
                    if (user?.uid != FireObj.userId) { //userId initialized in MainActivity
                        user?.let { listUsers.add(it) }
                    }
                }
                searchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }




}