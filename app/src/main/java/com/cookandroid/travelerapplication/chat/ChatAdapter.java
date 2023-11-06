package com.cookandroid.travelerapplication.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleContentActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;
import com.sun.mail.imap.protocol.Item;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements SelectData_UserInfo.AsyncTaskCompleteListener {
    private List<ChatRoom> itemList;
    private Context context;
    ArrayList<UserInfo> userInfoArrayList;
    String IP_ADDRESS, user_id;
    String my_nickname;

    public ChatAdapter(List<ChatRoom> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

        FileHelper fileHelper = new FileHelper(context);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        userInfoArrayList = new ArrayList<>();
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(userInfoArrayList, this);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", user_id);
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onTaskComplete_SelectData_UserInfo(UserInfo result) {
        my_nickname = result.getNickname();
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

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("my_nickname", my_nickname);
                intent.putExtra("room_id", itemList.get(curpos).getRoom_id());
                intent.putExtra("image_url", itemList.get(curpos).getProfileURI());
                intent.putExtra("nickname", itemList.get(curpos).getUserName());
                intent.putExtra("meet_up_id",itemList.get(curpos).getMeet_up_id());
                intent.putExtra("meet_up_post_id",itemList.get(curpos).getMeet_up_post_id());
                intent.putExtra("request_user_id",itemList.get(curpos).getRequest_user_id());
                intent.putExtra("write_user_id",itemList.get(curpos).getWrite_user_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }

        public void bind(ChatRoom ChatRoom) {
            userNickName.setText(ChatRoom.getUserName());

            if(!ChatRoom.getChatContent().equals("null")){
                chatContent.setText(ChatRoom.getChatContent());
            }else{
                chatContent.setText("(채팅 내용이 없습니다)");
            }

            String image_url = ChatRoom.getProfileURI();
            if(!(image_url == null || image_url.equals("null") || image_url.isEmpty() || image_url.equals(""))) {
                Glide.with(context)
                        .load(image_url)
                        .into(profilePhoto);
            }else{
                profilePhoto.setImageResource(R.drawable.profile_photo_mypage);
            }
            if(!ChatRoom.getTime().equals("null")){
                time.setText(ChatRoom.getTime());
            }else{
                time.setText("");
            }
        }
    }
}
