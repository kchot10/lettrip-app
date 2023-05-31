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
import com.cookandroid.travelerapplication.kotlin.KotlinActivity2;
import com.cookandroid.travelerapplication.task.InsertData_Place;

public class MissionTripActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip);

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
                    } else if (successOrFail.equals("fail")) {
                        Toast.makeText(this, "다음 도전을 기대합니다!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}
