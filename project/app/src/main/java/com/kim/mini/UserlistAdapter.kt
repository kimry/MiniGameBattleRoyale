package com.kim.mini

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserlistAdapter(val userList : ArrayList<UserlistModel>) : RecyclerView.Adapter<UserlistAdapter.UserlistViewHolder>() {

    private lateinit var serviceIntent : Intent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserlistAdapter.UserlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_itme2,parent,false)

        //서비스인텐트 연결
        serviceIntent = Intent(parent.context, ConnectionService::class.java)

        return UserlistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserlistAdapter.UserlistViewHolder, position: Int) {
        holder.userid_text.text = userList.get(position).userid_text
    }

    class UserlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userid_text = itemView.findViewById<TextView>(R.id.userId_text)
        val imageView2 = itemView.findViewById<ImageView>(R.id.imageView2)
    }
}