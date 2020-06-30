package com.example.firebasechatapp.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivities
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Activities.MessageChatActivity
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.Const
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_row_item.view.*



class SearchAdapter(val listUsers:List<User>, val isChat:Boolean):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(
             R.layout.search_row_item, parent, false
         )
         return ViewHolder(view, parent.context)
    }

    override fun getItemCount() = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFunction(listUsers[position])
    }

    class ViewHolder(itemView: View, val context:Context):RecyclerView.ViewHolder(itemView) {

        fun bindFunction(user: User) {
            itemView.user_name.text = user.username
            Picasso.get().load(user.profile)
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(itemView.settings_image)

            itemView.setOnClickListener{
                val string: SpannedString = buildSpannedString {
                    append("Send Message to - ")
                    bold {
                        append(user.username)
                    }
                }
                val options = arrayOf(string, "Visit his profile")
                AlertDialog.Builder(context)
                    .setItems(options, DialogInterface.OnClickListener{ dialog, which ->
                        if (which == 0) {
                            val intent = Intent(context, MessageChatActivity::class.java)
                            intent.putExtra(Const.VISIT_ID, user.uid)
                            context.startActivity(intent)
                        }

                        if (which == 1) {

                        }
                    })
                    .show()
            }
        }





    }
}