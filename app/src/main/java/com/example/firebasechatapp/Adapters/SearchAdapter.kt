package com.example.firebasechatapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Model.User
import com.example.firebasechatapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_row_item.view.*



class SearchAdapter(val listUsers:List<User>, val isChat:Boolean):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(
             R.layout.search_row_item, parent, false
         )
         return ViewHolder(view)
    }

    override fun getItemCount() = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFunction(listUsers[position])
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        fun bindFunction(user: User) {
            itemView.user_name.text = user.username
            Picasso.get().load(user.profile)
                .placeholder(R.drawable.ic_baseline_face_24)
                .into(itemView.settings_image)
        }

    }
}