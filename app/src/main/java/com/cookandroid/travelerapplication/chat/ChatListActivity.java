package com.cookandroid.travelerapplication.chat;

import android.os.Bundle;
import android.util.Log;

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

        List<ChatRoom> itemList = new ArrayList<>();

        itemList.add(new ChatRoom(null, "닉네임", "2023-11-05", "최근 메시지입니다.")); //uri가 null이면 기본 프로필 적용

        //todo:db에서 최근 채팅에 대한 정보가져와서 리스트에 추가
        //todo:같은 날짜면 시간을, 다른 날짜면 날짜를 보여주도록 추가하기

        SelectData_ChatRoom selectData_chatRoom= new SelectData_ChatRoom(this);
        selectData_chatRoom.execute("http://" + IP_ADDRESS + "/0930/select_chatroom.php", user_id);

    }

    @Override
    public void onTaskComplete_SelectData_ChatRoom(ArrayList<ChatRoom> result) {
        format_result = result;


        for (ChatRoom chatRoom:result) {
            String other_user_id = (user_id.equals(chatRoom.getWrite_user_id()) ? chatRoom.getRequest_user_id():chatRoom.getWrite_user_id());
            SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(new ArrayList<ChatRoom>(),this);
            selectData_userInfo.execute("http://" + IP_ADDRESS + "/0601/selectData_userInfo.php", other_user_id);
        }


    }

    @Override
    public void onTaskComplete_SelectData_UserInfo(UserInfo result) {
        for (ChatRoom chatRoom : format_result) {
            String other_user_id = (user_id.equals(chatRoom.getWrite_user_id()) ? chatRoom.getRequest_user_id():chatRoom.getWrite_user_id());
            if (other_user_id.equals(result.getUser_id())) {
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

