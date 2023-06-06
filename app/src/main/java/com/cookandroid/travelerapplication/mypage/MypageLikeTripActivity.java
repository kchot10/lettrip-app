package com.cookandroid.travelerapplication.mypage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

public class MypageLikeTripActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_like_trip);

        recyclerView = findViewById(R.id.like_trip_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MypageLikeTripAdapter myadapter = new MypageLikeTripAdapter();
        myadapter.addItem(new LikeTrip("식도락 여행", "200,000원", "서울")); //test data
        //위의 test data로 넣어둔 데이터 지우고 db에서 받아와서 출력하기 ++
        recyclerView.setAdapter(myadapter);
    }
}
