package com.cookandroid.travelerapplication.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_ChatRoom;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements SelectData_ChatRoom.AsyncTaskCompleteListener, SelectData_UserInfo.AsyncTaskCompleteListener {
    String IP_ADDRESS, user_id;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    ArrayList<ChatRoom> format_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        format_result= new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView_chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SelectData_ChatRoom selectData_chatRoom= new SelectData_ChatRoom(this);
        selectData_chatRoom.execute("http://" + IP_ADDRESS + "/0930/select_chatroom.php", user_id);

    }

    @Override
    public void onTaskComplete_SelectData_ChatRoom(ArrayList<ChatRoom> result) {
        format_result = result;

        for (ChatRoom chatRoom:result) {
            SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(new ArrayList<ChatRoom>(),this);
            selectData_userInfo.execute("http://" + IP_ADDRESS + "/0601/selectData_userInfo.php", chatRoom.getWrite_user_id());
        }


    }
    @Override
    public void onTaskComplete(UserInfo result) {
        for (ChatRoom chatRoom : format_result) {
            if (chatRoom.getWrite_user_id().equals(result.getUser_id())) {
                chatRoom.setUserName(result.getNickname());
                chatRoom.setProfileURI(result.getStored_file_url());
            }
        }
        runOnUiThread(()->{
            chatAdapter = new ChatAdapter(format_result, getApplicationContext());
            recyclerView.setAdapter(chatAdapter);
        });

    }
}

