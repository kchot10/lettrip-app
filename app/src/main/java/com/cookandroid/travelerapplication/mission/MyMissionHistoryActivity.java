package com.cookandroid.travelerapplication.mission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_MyMission;

import java.util.ArrayList;

public class MyMissionHistoryActivity extends AppCompatActivity {
    ArrayList<MyMission> missionArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mission_history);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.my_mission_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

    }

    public void Refresh() {
        missionArrayList = new ArrayList<>();
        SelectData_MyMission task = new SelectData_MyMission(missionArrayList);
        task.execute("http://" + IP_ADDRESS + "/0601/SelectData_MyMission.php", user_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new MyMissionAdapter(missionArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}