package com.cookandroid.travelerapplication.chat;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.recyclerView_chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ChatRoom> itemList = new ArrayList<>();

        itemList.add(new ChatRoom(null, "닉네임", "2023-11-05", "최근 메시지입니다.")); //uri가 null이면 기본 프로필 적용
        //todo:db에서 최근 채팅에 대한 정보가져와서 리스트에 추가
        //todo:같은 날짜면 시간을, 다른 날짜면 날짜를 보여주도록 추가하기

        chatAdapter = new ChatAdapter(itemList);
        recyclerView.setAdapter(chatAdapter);
    }
}
