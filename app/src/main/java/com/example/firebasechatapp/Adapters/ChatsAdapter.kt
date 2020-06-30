package com.example.firebasechatapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Model.Chat
import com.example.firebasechatapp.R
import com.example.firebasechatapp.Util.FireObj
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_message_left.view.*


class ChatsAdapter(val chatList: List<Chat>, val resPhoto: String): RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view0 = LayoutInflater.from(parent.context).inflate(
            R.layout.row_message_left, parent, false)
        val view1 = LayoutInflater.from(parent.context).inflate(
            R.layout.row_message_right, parent, false)

       return if(viewType == 0) ViewHolder(view0) else ViewHolder(view1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        if (chat.sender != FireObj.userId) {
            Picasso.get().load(resPhoto).into(holder.itemView.profile_image)
        }

        // View for sent photos
        if (chat.message == "vaSea"&& chat.url != ""){
            holder.itemView.text_message.visibility = View.GONE
            holder.itemView.imageV.visibility = View.VISIBLE
            Picasso.get().load(chat.url).into(holder.itemView.imageV)
        } else {
              holder.itemView.text_message.text = chat.message
        }


    }

    override fun getItemCount() = chatList.size

    override fun getItemViewType(position: Int): Int {
        return if(chatList[position].sender == FireObj.userId) 1 else 0
    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }
}