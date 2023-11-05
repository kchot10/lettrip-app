package com.cookandroid.travelerapplication.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.sun.mail.imap.protocol.Item;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private List<ChatRoom> itemList;

    public ChatAdapter(List<ChatRoom> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatRoom chatRoom = itemList.get(position);
        holder.bind(chatRoom);

        if (chatRoom.getProfileURI() != null) {
            holder.profilePhoto.setImageURI(chatRoom.getProfileURI());
        } else {
            holder.profilePhoto.setImageResource(R.drawable.profile_photo_mypage);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNickName;
        private TextView chatContent;
        private ImageView profilePhoto;
        private TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNickName = itemView.findViewById(R.id.userNickName);
            chatContent = itemView.findViewById(R.id.chatCurrentText);
            profilePhoto = itemView.findViewById(R.id.profilePhoto);
            time = itemView.findViewById(R.id.chatCurrentTime);
        }

        public void bind(ChatRoom ChatRoom) {
            userNickName.setText(ChatRoom.getUserName());
            chatContent.setText(ChatRoom.getChatContent());

            profilePhoto.setImageURI(ChatRoom.getProfileURI());
            time.setText(ChatRoom.getTime());
        }
    }
}
