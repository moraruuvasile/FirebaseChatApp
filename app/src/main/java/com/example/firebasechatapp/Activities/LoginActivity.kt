package com.example.firebasechatapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebasechatapp.MainActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginStart_btn(view: View) {
        loginUser()
    }

    private fun loginUser() {
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (email.trim() == "" || password.trim() == "") {
            Toast.makeText(this@LoginActivity, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        FireObj.firebaseInstance.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error " + task.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }
}