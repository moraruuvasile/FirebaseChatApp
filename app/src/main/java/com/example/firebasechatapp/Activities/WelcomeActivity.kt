package com.example.firebasechatapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.firebasechatapp.MainActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        FireObj.userInit()?.let {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
    }

    fun registerStart_btn(view: View) {
        startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))

    }
    fun loginStart_btn(view: View) {
        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
    }
}