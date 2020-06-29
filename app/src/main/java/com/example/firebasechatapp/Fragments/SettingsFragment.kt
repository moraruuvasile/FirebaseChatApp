package com.example.firebasechatapp.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val REQUEST_CODE = 1989
    lateinit var imageURI: Uri


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.INVISIBLE

        fillPhotoUsername()
        settings_image.setOnClickListener {
            pickImage()
        }
        settings_profileName.setOnClickListener {
            changeProfileName()
        }

    }


    private fun fillPhotoUsername() {
        FireObj.refUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        settings_profileName.text = it.username
                        Picasso.get().load(it.profile)
                            .placeholder(R.drawable.ic_baseline_face_24)
                            .into(settings_image)
                    }
                }
            }
        })
    }

    private fun pickImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (!Debug.isDebuggerConnected()) {
//            Debug.waitForDebugger();
//            Log.d("debug", "started"); // Insert a breakpoint at this line!!
//        }

        if (requestCode == REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data!!.data != null
        ) {
            imageURI = data.data!!
            Toast.makeText(context, "uploading......", Toast.LENGTH_LONG).show()
            uploadeImageToDatabase()
        }
    }

    private fun uploadeImageToDatabase() {
        progressBar.visibility = View.VISIBLE

        val fileRef = FirebaseStorage.getInstance().reference.child("User Images")
            .child(System.currentTimeMillis().toString() + ".jpg")

        val uploadTask: StorageTask<*>
        uploadTask = fileRef.putFile(imageURI!!)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            return@Continuation fileRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val hashMap = HashMap<String, Any>()
                hashMap["profile"] = task.result.toString()
                FireObj.refUser.updateChildren(hashMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressBar.visibility = View.INVISIBLE

                    }
                }
            }
        }
    }

    private fun changeProfileName() {
        val nameEditText = EditText(context)
        nameEditText.hint = "your new amazing name"
        AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
            .setTitle("Change your name")
            .setView(nameEditText)
            .setPositiveButton("Change", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(context, "uploading......", Toast.LENGTH_LONG).show()
                updateusername(nameEditText.text.toString())
            })
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun updateusername(str: String) {
        progressBar.visibility = View.VISIBLE
        val hashMap = HashMap<String, Any>()
        hashMap["username"] = str
        FireObj.refUser.updateChildren(hashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

}