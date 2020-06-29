package com.example.firebasechatapp.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebasechatapp.MainActivity
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register_btn(view: View) {
        registerUser()
    }

    private fun registerUser() {
        val name = name_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (name.trim() == "" || email.trim() == "" || password.trim() == "") {
            Toast.makeText(this@RegisterActivity, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        FireObj.firebaseInstance.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    FireObj.userIdInit()
                    FireObj.refUserInit()

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = FireObj.userId
                    userHashMap["username"] = name
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/whatsapp-f4d4c.appspot.com/o/prof.bmp?alt=media&token=13351809-fd00-4a5a-acb0-1e349696e96d"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/whatsapp-f4d4c.appspot.com/o/profile.bmp?alt=media&token=4b4f46be-e3c4-4755-8916-3043f22f8557"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = name.toLowerCase()


                    FireObj.refUser.updateChildren(userHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error " + task.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }

}