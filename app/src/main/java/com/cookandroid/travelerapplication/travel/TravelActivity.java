package com.cookandroid.travelerapplication.travel;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.travelerapplication.MainActivity;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.task.InsertData_Article;
import com.cookandroid.travelerapplication.task.InsertData_Travel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TravelActivity extends AppCompatActivity {

    private String IP_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        findViewById(R.id.button_travel_upload).setOnClickListener(v -> {
            String user_id = fileHelper.readFromFile("user_id");
            String created_date = getCurrentTime();
            String visited = "0";
            EditText editText_departdate = findViewById(R.id.editText_departdate);
            EditText editText_lastdate = findViewById(R.id.editText_lastdate);
            String depart_date = editText_departdate.getText().toString().trim();
            String last_date = editText_lastdate.getText().toString().trim();
            String total_cost = "0";

            if (depart_date.equals("") || last_date.equals("")){
                Toast.makeText(this, "시작 또는 마지막 날짜를 입력하세요", Toast.LENGTH_SHORT).show();
            }else {
                Log.d("lettrip", user_id+visited+ depart_date+last_date+ total_cost);
                InsertData_Travel insertData_travel = new InsertData_Travel();
                insertData_travel.execute("http://"+IP_ADDRESS+"/0503/InsertData_Travel.php",user_id,created_date,visited, depart_date,last_date, total_cost);

                new Handler().postDelayed(() -> {
                    String withdraw_result = insertData_travel.getReturn_string();
                    if (withdraw_result.equals("실패")) {
                        Toast.makeText(this, "여행 추가는 완료되었으나 travel_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    } else if (withdraw_result.equals("에러")) {
                        Toast.makeText(this, "여행 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "여행 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        fileHelper.writeToFile("travel_id", withdraw_result);
                        findViewById(R.id.button_travel_upload).setVisibility(View.INVISIBLE);
                        findViewById(R.id.button_add_place).setVisibility(View.VISIBLE);
                        editText_departdate.setFocusable(false);
                        editText_lastdate.setFocusable(false);

                    }
                }, 500); // 0.5초 지연 시간
//                InsertData_Travel_Region insertData_travel_region = new InsertData_Travel_Region();
//                insertData_travel_region.execute("http://"+IP_ADDRESS+"/0503/InsertData_Travel_Region.php",user_id,visited, depart_date,last_date, total_cost);
//                Toast.makeText(this, "여행 추가 php 파일 실행", Toast.LENGTH_SHORT).show();
            }

        });

            findViewById(R.id.button_add_place).setOnClickListener(v -> {
            Intent intent = new Intent(TravelActivity.this, PlaceActivity.class);
            startActivity(intent);
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