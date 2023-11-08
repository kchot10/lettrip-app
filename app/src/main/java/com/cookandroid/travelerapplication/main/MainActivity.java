package com.cookandroid.travelerapplication.main;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.account.WithdrawActivity;
import com.cookandroid.travelerapplication.chat.ChatListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.MeetupPostMainAcitivty;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.mypage.MypageModifyActivity;
import com.cookandroid.travelerapplication.record.PlanningMain;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.search.SearchActivity;
import com.cookandroid.travelerapplication.search.SearchReviewActivity;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.


    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_designed2);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");



        findViewById(R.id.button_record).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecordMain.class);
            intent.putExtra("record/plan", "record");
            startActivity(intent);
        });

        findViewById(R.id.button_plan).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlanningMain.class);
            intent.putExtra("record/plan", "plan");
            startActivity(intent);
        });


        findViewById(R.id.button_search).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("search/mypage", "search");
            startActivity(intent);
        });

        findViewById(R.id.missionBtn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MissionMainActivity.class);
            intent.putExtra("search/mypage", "mypage");
            startActivity(intent);
        });


        findViewById(R.id.meetupBtn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MeetupPostMainAcitivty.class);
            startActivity(intent);
        });

        ImageButton chatBtn = findViewById(R.id.chatBtn);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mypage = findViewById(R.id.mypageBtn);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mission = findViewById(R.id.missionBtn2);
        mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton board = findViewById(R.id.boardBtn);
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton meetupBtn = findViewById(R.id.meetupBtn);
        meetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupPostMainAcitivty.class);
                startActivity(intent);
            }
        });

        ImageButton logoBtn = findViewById(R.id.logoBtn);
        logoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton record = findViewById(R.id.recordBtn);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 레이아웃 인플레이션
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_record_and_plan, null);

                // AlertDialog 생성
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);

                // 다이얼로그 버튼 설정
                Button buttonRecord = dialogView.findViewById(R.id.button_record);
                Button buttonPlan = dialogView.findViewById(R.id.button_plan);

                buttonRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 후기 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(MainActivity.this, RecordMain.class);
                        intent.putExtra("record/plan", "record");
                        startActivity(intent);
                    }
                });

                buttonPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 계획 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(MainActivity.this, PlanningMain.class);
                        intent.putExtra("record/plan", "plan");
                        startActivity(intent);
                    }
                });

                // AlertDialog 표시
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        ImageButton searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchReviewActivity.class);
                startActivity(intent);
            }
        });

    }



}
