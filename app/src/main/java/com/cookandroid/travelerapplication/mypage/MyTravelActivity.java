package com.cookandroid.travelerapplication.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.search.Travel;
import com.cookandroid.travelerapplication.search.TravelAdapter;
import com.cookandroid.travelerapplication.task.SelectData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mine;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mylike;

import java.util.ArrayList;

public class MyTravelActivity extends AppCompatActivity {
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travel);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.like_trip_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();
    }

    public void Refresh() {
        TextView titleText = findViewById(R.id.titleText);
        travelArrayList = new ArrayList<>();
        String is_visited = "0";
        if (getIntent().getStringExtra("visited/not").equals("visited")){
            is_visited = "1";
            titleText.setText("RECORD");
        }
        SelectData_Travel_Mine selectData_travel_mine = new SelectData_Travel_Mine(travelArrayList);
        selectData_travel_mine.execute("http://" + IP_ADDRESS + "/0601/selectData_travel_mine.php", user_id, is_visited);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new TravelAdapter(travelArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Refresh();
    }
}