package com.cookandroid.travelerapplication.mission;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity2;
import com.cookandroid.travelerapplication.task.InsertData_Mission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MissionTripStartActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    int sum = 0;
    int sec;
    int min;
    private LocationManager locationManager;
    double current_latitude;
    double current_longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip_start);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        ImageButton backBtn = findViewById(R.id.leftArrowBtn);
        FrameLayout map_view = findViewById(R.id.map_FramLayout);
        TextView textView = findViewById(R.id.time_TextView);

        findViewById(R.id.missionVerificationBtn).setOnClickListener(v -> {
            sum+=1;
            String accomplished_date = getCurrentTime();
            String mission_type = "TRIP";
            InsertData_Mission insertData_mission = new InsertData_Mission();
            insertData_mission.execute("http://"+IP_ADDRESS+"/0503/InsertData_Mission.php", accomplished_date, mission_type, user_id);

            Intent intent = new Intent(this, KotlinActivity2.class);
            startActivity(intent);
        });




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                Toast.makeText(getApplicationContext(), "미션Trip 레코드를 추가했습니다. \n현재 추가한 레코드 수:"+sum,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        //1km 이내의 장소 리스트 불러오기

        timerRun();
    }

    private void timerRun() {

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
    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
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
