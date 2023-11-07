package com.cookandroid.travelerapplication.mypage;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.meetup.MeetupPost;
import com.cookandroid.travelerapplication.meetup.MeetupPostAdapter;

import java.util.ArrayList;
import java.util.List;

public class MypageMeetUpPostList extends AppCompatActivity {
    ImageButton backBtn;
    RecyclerView recyclerView;
    private MeetupPostAdapter adapter;
    private ArrayList<MeetupPost> meetupPostList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_my_meetup_post);

        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.recyclerView);

        //현재는 모든 매칭 글을 불러옴
        //todo:유저 아이디가 현재 로그인된 유저 아이디와 일치하는 포스트들만 불러오는 코드 추가


        // 어댑터 설정
        adapter = new MeetupPostAdapter(meetupPostList, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



}
