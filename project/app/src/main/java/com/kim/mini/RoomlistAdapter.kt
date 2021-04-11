package com.kim.mini

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomlistAdapter(val roomList : ArrayList<RoomlistModel>) : RecyclerView.Adapter<RoomlistAdapter.RoomlistViewHolder>() {

    private lateinit var serviceIntent : Intent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomlistAdapter.RoomlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_item,parent,false)

        //서비스인텐트 연결
        serviceIntent = Intent(parent.context, ConnectionService::class.java)

        return RoomlistViewHolder(view).apply{
            itemView.setOnClickListener {
                val curPos : Int =adapterPosition
                val room : RoomlistModel = roomList.get(curPos)
                serviceIntent.putExtra("roomNumber", "${room.roomNumber}")
                serviceIntent.action = ConnectionService.ACTION_ENTERROOM
                parent.context.startService(serviceIntent)

                val nextIntent = Intent(parent.context, RoomActivity::class.java)
                nextIntent.putExtra("state","nomal")
                parent.context.startActivity(nextIntent)

                serviceIntent.action = ConnectionService.ACTION_LOBBYFINISH
                parent.context.startService(serviceIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomlistAdapter.RoomlistViewHolder, position: Int) {
        holder.roomNumber.text = roomList.get(position).roomNumber
    }

    class RoomlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val roomNumber = itemView.findViewById<TextView>(R.id.roomNumber)
    }
}