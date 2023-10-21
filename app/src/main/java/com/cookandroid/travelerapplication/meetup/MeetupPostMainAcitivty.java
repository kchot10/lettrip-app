package com.cookandroid.travelerapplication.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

import java.util.List;

public class MeetupPostMainAcitivty extends AppCompatActivity {
    ImageButton chatBtn;
    Spinner gpsSelected;
    Spinner city1;
    Spinner city2;
    Button addPost;
    boolean isGPSEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_main);

        chatBtn = findViewById(R.id.chatBtn);
        gpsSelected = findViewById(R.id.GpsSpinner);
        city1 = findViewById(R.id.citySpinner);
        city2 = findViewById(R.id.citySpinner2);
        addPost = findViewById(R.id.writeBtn);

        //gps 스피너
        String[] gpsStatus = {"전체", "GPS 정보 사용", "GPS 정보 미사용"};
        ArrayAdapter<String> adapterGpsSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus); //추후 스피너 레이아웃 커스텀하기
        adapterGpsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSelected.setAdapter(adapterGpsSpinner);
        gpsSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedGpsStatus = gpsStatus[position];
                //gps선택 여부에 따라 보여주는 화면 설계 추가
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //목록 불러오기
        List<MeetupPost> meetupPostList = DatabaseHandler.getMeetupPosts();
        MeetupPostAdapter adapter = new MeetupPostAdapter(meetupPostList);


        //글쓰기 수행
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupAddPostActivity.class);
                startActivity(intent);
            }
        });
    }
}
