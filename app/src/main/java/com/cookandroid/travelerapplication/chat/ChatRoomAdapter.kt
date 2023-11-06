/**
 * @author Joyce Hong
 * @email soja0524@gmail.com
 * @created 2019-09-03
 * @desc
 */

package com.cookandroid.travelerapplication.chat;

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.travelerapplication.R

class ChatRoomAdapter(val context : Context, val chatList : ArrayList<Message>) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>(){

    val CHAT_MINE = 0
    val CHAT_PARTNER = 1
    val USER_JOIN = 2
    val USER_LEAVE = 3
    val IMAGE_MINE = 4
    val IMAGE_PARTNER = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view : View? = null
        when(viewType){

            0 ->{
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_user,parent,false)
                Log.d("user inflating","viewType : ${viewType}")
            }

            1 ->
            {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_partner,parent,false)
                Log.d("partner inflating","viewType : ${viewType}")
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
            4 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_user,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
            5 -> {
                view = LayoutInflater.from(context).inflate(R.layout.row_chat_partner,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
        }

        return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageData  = chatList[position]
        val userName = messageData.userName;
        val content = messageData.messageContent;
        val viewType = messageData.viewType;

        when(viewType) {

            CHAT_MINE -> {
                holder.image_message.visibility = View.INVISIBLE;
                holder.message.setText(content)
            }
            CHAT_PARTNER ->{
                holder.image_message.visibility = View.INVISIBLE;
                holder.userName.setText(userName)
                holder.message.setText(content)
            }
            USER_JOIN -> {
                val text = "${userName}님이 방에 입장했습니다."
                holder.text.setText(text)
            }
            USER_LEAVE -> {
                val text = "${userName}님이 방에 나갔습니다."
                holder.text.setText(text)
            }
            IMAGE_MINE -> {
                holder.message.visibility = View.INVISIBLE;
                Glide.with(context)
                    .load(content)
                    .placeholder(R.drawable.image_icon)
                    .into(holder.image_message)
            }
            IMAGE_PARTNER -> {
                holder.message.visibility = View.INVISIBLE;
                holder.userName.setText(userName)
                Glide.with(context)
                    .load(content)
                    .placeholder(R.drawable.image_icon)
                    .into(holder.image_message)
            }
        }

    }
    inner class ViewHolder(itemView : View):  RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.username)
        val message = itemView.findViewById<TextView>(R.id.message)
        val image_message = itemView.findViewById<ImageView>(R.id.image_message)
        val text = itemView.findViewById<TextView>(R.id.text)
    }

}