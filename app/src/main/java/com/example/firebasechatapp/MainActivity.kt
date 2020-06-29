package com.example.firebasechatapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.firebasechatapp.Activities.WelcomeActivity
import com.example.firebasechatapp.Adapters.ViewPagerAdapter
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.Util.FireObj

import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        supportActionBar?.title = ""

        //initiate user&reference
        FireObj.userIdInit()
        FireObj.refUserInit()

        view_pager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tab_layout, view_pager) { tab, i ->
            when (i) {
                0 -> tab.text = "Chats"
                1 -> tab.text = "Search"
                else -> tab.text = "Settings"
            }

        }.attach()
        FireObj.refUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        profile_name.text = it.username
                        Picasso.get().load(it.profile)
                        //.placeholder(R.drawable.ic_baseline_face_24)
                            .into(settings_image)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logOut -> {
                FireObj.firebaseInstance.signOut()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}