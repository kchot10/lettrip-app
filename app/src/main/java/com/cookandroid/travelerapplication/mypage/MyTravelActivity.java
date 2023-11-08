package com.cookandroid.travelerapplication.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.chat.ChatRoom;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.pokeInfo.PokeInfoMainActivity;
import com.cookandroid.travelerapplication.search.Travel;
import com.cookandroid.travelerapplication.search.TravelAdapter;
import com.cookandroid.travelerapplication.task.SelectData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mine;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mylike;

import java.util.ArrayList;

public class MyTravelActivity extends AppCompatActivity implements TravelAdapter.AsyncTaskCompleteListener{
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageButton backBtn;

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
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Refresh();
    }

    public void Refresh() {
        if ("PokeInfoMainActivity".equals(getIntent().getStringExtra("previousActivity"))) {
            user_id = getIntent().getStringExtra("request_user_id");
        }

        TextView titleText = findViewById(R.id.titleText);
        travelArrayList = new ArrayList<>();
        String is_visited = "0";
        if (getIntent().getStringExtra("visited/not").equals("visited")) {
            is_visited = "1";
            titleText.setText("RECORD");
        }
        SelectData_Travel_Mine selectData_travel_mine = new SelectData_Travel_Mine(travelArrayList);
        selectData_travel_mine.execute("http://" + IP_ADDRESS + "/0601/selectData_travel_mine.php", user_id, is_visited);
        try {
            new Handler().postDelayed(() -> {
                if (travelArrayList.isEmpty()) {
                    Toast.makeText(this, "기록된 여행이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView_adapter = new TravelAdapter(travelArrayList, this, getIntent().getBooleanExtra("select", false), this);
                    recyclerView.setAdapter(recyclerView_adapter);
                }
            }, 1000); // 0.5초 지연 시간
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTaskComplete(Travel result) {
        Intent intent = new Intent();
        intent.putExtra("travel", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh();
    }
}