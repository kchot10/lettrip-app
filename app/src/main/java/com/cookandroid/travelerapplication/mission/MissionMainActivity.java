package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.task.InsertData_Mission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MissionMainActivity extends AppCompatActivity {
    int sum = 0;

    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");

        LinearLayout missionQR = findViewById(R.id.missionQR);
        LinearLayout missionTrip = findViewById(R.id.missionTrip);
        ImageButton backBtn = findViewById(R.id.leftArrowBtn);
        ImageButton homeBtn = findViewById(R.id.homeBtn);


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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}
