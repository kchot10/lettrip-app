package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity2;
import com.cookandroid.travelerapplication.task.InsertData_Mission;
import com.cookandroid.travelerapplication.task.InsertData_Place;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MissionTripActivity extends AppCompatActivity {

    String IP_ADDRESS, user_id;
    FileHelper fileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        Button missionStartBtn = findViewById(R.id.missionStartBtn);
        ImageButton backBtn = findViewById(R.id.leftArrowBtn);

        missionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KotlinActivity2.class);
                getKotlinActivityResult.launch(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
                startActivity(intent);
            }
        });
    }
    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    String successOrFail = result.getData().getStringExtra("success/fail");
                    if (successOrFail.equals("success")){
                        Toast.makeText(this, "미션 성공을 축하드립니다!",Toast.LENGTH_SHORT).show();
                        String accomplished_date = getCurrentTime();
                        String mission_type = "RANDOM_MISSION";
                        InsertData_Mission insertData_mission = new InsertData_Mission();
                        insertData_mission.execute("http://"+IP_ADDRESS+"/0503/InsertData_Mission.php", accomplished_date, mission_type, user_id);
                    } else if (successOrFail.equals("fail")) {
                        Toast.makeText(this, "다음 도전을 기대합니다!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }


}
