package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.record.CourseAdapter;
import com.cookandroid.travelerapplication.task.InsertData_Mission;
import com.cookandroid.travelerapplication.task.SelectData_Course;
import com.cookandroid.travelerapplication.task.SelectData_Mission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MissionMainActivity extends AppCompatActivity {

    int KINDNUM = 4;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    int sum = 0;

    LinearLayout missionQR, missionTrip;
    ArrayList<Mission> missionArrayList[];

    RecyclerView recyclerView_mission_QR, recyclerView_mission_TRIP, recyclerView_mission_FOOD, recyclerView_mission_CAFE;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        missionQR = findViewById(R.id.missionQR);
        missionTrip = findViewById(R.id.missionTrip);
        missionArrayList = new ArrayList[KINDNUM];
        LinearLayoutManager[] layoutManagers = new LinearLayoutManager[KINDNUM];
        for (int i = 0; i < KINDNUM; i++) {
            missionArrayList[i] = new ArrayList<>();
            layoutManagers[i] = new LinearLayoutManager(this);
        }
        recyclerView_mission_QR = findViewById(R.id.recyclerView_mission_QR);
        recyclerView_mission_QR.setHasFixedSize(true);
        recyclerView_mission_QR.setLayoutManager(layoutManagers[0]);
        Refresh(recyclerView_mission_QR, "QR", 0); // 세 파라미터 모두 QR을 뜻함
        recyclerView_mission_TRIP = findViewById(R.id.recyclerView_mission_TRIP);
        recyclerView_mission_TRIP.setHasFixedSize(true);
        recyclerView_mission_TRIP.setLayoutManager(layoutManagers[1]);
        Refresh(recyclerView_mission_TRIP, "TRIP", 1);
        recyclerView_mission_FOOD = findViewById(R.id.recyclerView_mission_FOOD);
        recyclerView_mission_FOOD.setHasFixedSize(true);
        recyclerView_mission_FOOD.setLayoutManager(layoutManagers[2]);
        Refresh(recyclerView_mission_FOOD, "FOOD", 2);
        recyclerView_mission_CAFE = findViewById(R.id.recyclerView_mission_CAFE);
        recyclerView_mission_CAFE.setHasFixedSize(true);
        recyclerView_mission_CAFE.setLayoutManager(layoutManagers[3]);
        Refresh(recyclerView_mission_CAFE, "CAFE", 3);

        missionQR.setOnClickListener(v -> {
            sum+=1;
            String accomplished_date = getCurrentTime();
            String mission_type = "QR";
            InsertData_Mission insertData_mission = new InsertData_Mission();
            insertData_mission.execute("http://"+IP_ADDRESS+"/0503/InsertData_Mission.php", accomplished_date, mission_type, user_id);
        });
        missionTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                startActivity(intent);
            }
        });

    }

    public void Refresh(RecyclerView recyclerView, String mission_type, int i) {
        // Record class, SelectData_Record task, RecordAdapter
        SelectData_Mission task = new SelectData_Mission(missionArrayList[i]);
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_mission.php", mission_type);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new MissionAdapter(missionArrayList[i], this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}
