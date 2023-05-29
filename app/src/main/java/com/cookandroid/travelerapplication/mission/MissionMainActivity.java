package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

public class MissionMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_main);

        LinearLayout missionLayout = findViewById(R.id.missionTrip);
        missionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                startActivity(intent);
            }
        });
    }
}
