package com.cookandroid.travelerapplication.recommend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.record.Place;
import com.cookandroid.travelerapplication.search.TravelAdapter;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.Recommend_Place;
import com.cookandroid.travelerapplication.task.SelectData_Place;

import java.util.ArrayList;

public class RecommendPlaceActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_place);
        IP_ADDRESS = "15.164.93.235";
        user_id = "10425035876";

        recyclerView = findViewById(R.id.recyclerView_place_score);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.button_recommend_place).setOnClickListener(v -> {
            ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
            Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
            recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request/", user_id);
            try {
                new Handler().postDelayed(() -> {
                    recyclerView_adapter = new PlaceScoreAdapter(arrayListPlaceScore, this);
                    recyclerView.setAdapter(recyclerView_adapter);
                }, 3000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}