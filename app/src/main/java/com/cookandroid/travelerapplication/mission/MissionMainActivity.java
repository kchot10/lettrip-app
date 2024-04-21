package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Mission;
import com.cookandroid.travelerapplication.task.SelectData_MyPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MissionMainActivity extends AppCompatActivity {

    int KINDNUM = 5;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    int sum = 0;

    LinearLayout missionQR, missionTrip;
    ArrayList<Mission> missionArrayList[];

    RecyclerView recyclerView_mission_QR, recyclerView_mission_TRIP, recyclerView_mission_FOOD, recyclerView_mission_CAFE, recyclerView_mission_KINDCITY;
    private RecyclerView.Adapter recyclerView_adapter;
    TextView myPointText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        myPointText = findViewById(R.id.myPointText);
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
        Refresh(recyclerView_mission_QR, "QR_MISSION", 0); // 세 파라미터 모두 QR을 뜻함
        recyclerView_mission_TRIP = findViewById(R.id.recyclerView_mission_TRIP);
        recyclerView_mission_TRIP.setHasFixedSize(true);
        recyclerView_mission_TRIP.setLayoutManager(layoutManagers[1]);
        Refresh(recyclerView_mission_TRIP, "RANDOM_MISSION", 1);
        recyclerView_mission_FOOD = findViewById(R.id.recyclerView_mission_FOOD);
        recyclerView_mission_FOOD.setHasFixedSize(true);
        recyclerView_mission_FOOD.setLayoutManager(layoutManagers[2]);
        Refresh(recyclerView_mission_FOOD, "FD6", 2);
        recyclerView_mission_CAFE = findViewById(R.id.recyclerView_mission_CAFE);
        recyclerView_mission_CAFE.setHasFixedSize(true);
        recyclerView_mission_CAFE.setLayoutManager(layoutManagers[3]);
        Refresh(recyclerView_mission_CAFE, "CE7", 3);
        recyclerView_mission_KINDCITY = findViewById(R.id.recyclerView_mission_KINDCITY);
        recyclerView_mission_KINDCITY.setHasFixedSize(true);
        recyclerView_mission_KINDCITY.setLayoutManager(layoutManagers[4]);
        Refresh(recyclerView_mission_KINDCITY, "KINDCITY", 4);

        Refresh_MyPoint();


        //QR 코드 미션
        missionQR.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MissionQRActivity.class);
            startActivity(intent);
        });
        missionTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_my_mission_history).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyMissionHistoryActivity.class);
            startActivity(intent);
        });

    }

    private void Refresh_MyPoint() {
        ArrayList<UserInfo> arrayListUserInfo = new ArrayList();
        SelectData_MyPoint selectData_myPoint = new SelectData_MyPoint(arrayListUserInfo);
        selectData_myPoint.execute("http://" + IP_ADDRESS + "/0601/select_my_point.php", user_id);
        new Handler().postDelayed(() -> {
            try {
                String myPoint = arrayListUserInfo.get(0).getPoint()+" P";
                myPointText.setText(myPoint);
            }catch (Exception e){
                Log.e("youn", "point 불러오기 실패");
            }
        }, 1000); // 0.5초 지연 시간
    }


    public void Refresh(RecyclerView recyclerView, String mission_type, int i) {
        // Record class, SelectData_Record task, RecordAdapter
        SelectData_Mission task = new SelectData_Mission(missionArrayList[i]);
        if (i==2 || i == 3){
            task.execute("http://" + IP_ADDRESS + "/0503/selectdata_foodcafe.php", mission_type);
        } else if ( i == 4) {
            task.execute("http://" + IP_ADDRESS + "/0503/selectdata_kindcity.php", mission_type);
        } else{
            task.execute("http://" + IP_ADDRESS + "/0503/selectdata_mission.php", mission_type);
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh_MyPoint();
    }
}
