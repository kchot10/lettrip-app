package com.cookandroid.travelerapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mission.MissionTripActivity;
import com.cookandroid.travelerapplication.mypage.MyTravelActivity;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.search.SearchActivity;
import com.cookandroid.travelerapplication.search.SearchReviewActivity;

public class MainActivityDesigned extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_designed2);
//
//        ConstraintLayout trip_review_ConstraintLayout = findViewById(R.id.trip_review_ConstraintLayout);
//        Button recordBtn1 = findViewById(R.id.recordBtn1);
//        Button recordBtn2 = findViewById(R.id.recordBtn2);
//        LinearLayout TripMissionBtn= findViewById(R.id.TripMissionBtn);
//        TextView trip_mission_title2 = findViewById(R.id.trip_mission_title2);
//        Button boardBtn = findViewById(R.id.boardBtn);
//        Button PalceSearchBtn = findViewById(R.id.PalceSearchBtn);
//        Button MypageBtn = findViewById(R.id.MypageBtn);
//
//
//        trip_review_ConstraintLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        recordBtn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), RecordMain.class);
//                intent.putExtra("record/plan", "record");
//                startActivity(intent);
//            }
//        });
//
//        recordBtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), RecordMain.class);
//                intent.putExtra("record/plan", "plan");
//                startActivity(intent);
//            }
//        });
//
//        TripMissionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
//                intent.putExtra("record/plan", "plan");
//                startActivity(intent);
//            }
//        });
//
//        trip_mission_title2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
//                intent.putExtra("search/mypage", "mypage");
//                startActivity(intent);
//            }
//        });
//
//        boardBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        PalceSearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SearchReviewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        MypageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
