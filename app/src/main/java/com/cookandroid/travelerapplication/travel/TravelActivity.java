package com.cookandroid.travelerapplication.travel;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.travelerapplication.MainActivity;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.task.InsertData_Article;
import com.cookandroid.travelerapplication.task.InsertData_Travel;

public class TravelActivity extends AppCompatActivity {

    private String IP_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        FileHelper fileHelper = new FileHelper(this);
        fileHelper.readFromFile("IP_ADDRESS");

        findViewById(R.id.button_travel_upload).setOnClickListener(v -> {
            String user_id = fileHelper.readFromFile("user_id");
            String visited = "0";
            EditText editText_departdate = findViewById(R.id.editText_departdate);
            EditText editText_lastdate = findViewById(R.id.editText_lastdate);
            String depart_date = editText_departdate.getText().toString().trim();
            String last_date = editText_lastdate.getText().toString().trim();
            String total_cost = "0";

            if (depart_date.equals("") || last_date.equals("")){
                Toast.makeText(this, "시작 또는 마지막 날짜를 입력하세요", Toast.LENGTH_SHORT).show();
            }else {
                InsertData_Travel insertData_travel = new InsertData_Travel();
                insertData_travel.execute("http://"+IP_ADDRESS+"/0503/InsertData_Travel.php",user_id,visited, depart_date,last_date, total_cost);
                Toast.makeText(this, "여행 추가 php 파일 실행", Toast.LENGTH_SHORT).show();
                finish();
            }

        });

            findViewById(R.id.button_add_place).setOnClickListener(v -> {
            Intent intent = new Intent(TravelActivity.this, PlaceActivity.class);
            startActivity(intent);
        });



    }
}