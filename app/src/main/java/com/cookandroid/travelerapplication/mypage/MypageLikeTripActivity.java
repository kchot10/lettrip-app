package com.cookandroid.travelerapplication.mypage;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.search.Travel;
import com.cookandroid.travelerapplication.search.TravelAdapter;
import com.cookandroid.travelerapplication.task.SelectData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mylike;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mypage;

import java.util.ArrayList;

public class MypageLikeTripActivity extends AppCompatActivity {
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_like_trip);
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
        travelArrayList = new ArrayList<>();
        String liked_type = "1";
        SelectData_Travel_Mylike task = new SelectData_Travel_Mylike(travelArrayList);
        task.execute("http://" + IP_ADDRESS + "/0601/selectdata_travel_mylike.php", user_id, liked_type);
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
    protected void onRestart() {
        super.onRestart();
        Refresh();
    }
}
