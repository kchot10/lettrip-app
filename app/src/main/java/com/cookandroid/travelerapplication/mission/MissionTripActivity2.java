package com.cookandroid.travelerapplication.mission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cookandroid.travelerapplication.Manifest;
import com.cookandroid.travelerapplication.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MissionTripActivity2 extends AppCompatActivity {
    int sec;
    int min;
    private LocationManager locationManager;
    double current_latitude;
    double current_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip_start);

        ImageButton backBtn = findViewById(R.id.leftArrowBtn);
        FrameLayout map_view = findViewById(R.id.map_FramLayout);
        TextView textView = findViewById(R.id.time_TextView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                startActivity(intent);
            }
        });


        //타이머 구현
        TextView min_TextView = findViewById(R.id.min);
        TextView sec_TextView = findViewById(R.id.sec);

        min_TextView.setText("0");
        sec_TextView.setText("0");

        min = Integer.parseInt(min_TextView.getText().toString());
        sec = Integer.parseInt(sec_TextView.getText().toString());

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(sec != 0){
                    sec--;
                } else if(min != 0){
                    sec = 60;
                    sec--;
                    min--;
                }

                if(min <= 9){
                    min_TextView.setText("0" + min);
                } else{
                    min_TextView.setText(min);
                }

                if(sec <= 9){
                    min_TextView.setText("0" + sec);
                } else{
                    min_TextView.setText(sec);
                }

                if(min == 0 && sec == 0){
                    timer.cancel();
                    showAlertDialog();
                }

                if(sec<=30){
                    sec_TextView.setTextColor(Color.parseColor("#E12E23")); //빨간색으로 바꾸기
                }
            }
        };


        //1km 이내의 장소 리스트 불러오기

    }


    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("미션에 실패하셨습니다.");
        builder.setMessage("다시 한 번 도전하시겠습니까?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //미션 액티비티 새로고침
                        finish();
                        overridePendingTransition(0,0);
                        Intent intent = getIntent();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //이전 화면으로 돌아가기
                        Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                        startActivity(intent);
                    }
                });
    }


}
