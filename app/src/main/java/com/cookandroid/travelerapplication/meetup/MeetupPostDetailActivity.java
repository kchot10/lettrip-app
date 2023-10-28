package com.cookandroid.travelerapplication.meetup;

import static org.jetbrains.anko.Sdk27PropertiesKt.setImageResource;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetupPostDetailActivity extends AppCompatActivity {
    ImageButton backBtn;
    ImageView chatBtn;
    ImageView gpsInfo;
    TextView city1;
    TextView city2;
    Button edit;
    Button delete;
    TextView meetupDate;
    ImageView profilePhoto;
    TextView userName;
    ImageView userSex;
    TextView userBirth;
    TextView contents;
    ImageView pokeBtn;
    TextView pokeNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_post_detail);

        backBtn = findViewById(R.id.backBtn);
        chatBtn = findViewById(R.id.chatBtn);
        gpsInfo = findViewById(R.id.gpsInfo);
        city1 = findViewById(R.id.city1);
        city2 = findViewById(R.id.city2);
        edit = findViewById(R.id.editBtn);
        delete = findViewById(R.id.trashBtn);
        meetupDate = findViewById(R.id.dateSelectTextView);
        profilePhoto = findViewById(R.id.profilePhoto);
        userName = findViewById(R.id.userName);
        userSex = findViewById(R.id.userSex);
        userBirth = findViewById(R.id.userBirth);
        contents = findViewById(R.id.meetupPostContext);
        pokeBtn = findViewById(R.id.pokeBtn);
        pokeNumTextView = findViewById(R.id.pokeNumTextView);

        //포크 리스트 불러오기
        pokeNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PokeListActivity.class);
                startActivity(intent);
            }
        });

        //뒤로가기
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupPostMainAcitivty.class);
                startActivity(intent);
            }
        });

        //db에서 데이터 받아와서 화면 구성
        String city1Text = "서울특별시", city2Text="서울";
        Date date = new Date();
        String userNameText = "서정후";
        String userSexText = "female";
        Date userBirthText = new Date();
        String contentsText = "테스트";
        boolean isGPSenabled = true;

        //- 데이터 불러오는 코드 추가**

        //불러온 데이터로 화면 업데이트
        if(isGPSenabled == true){
            gpsInfo.setImageResource(R.drawable.meetup_post_gps_icon);
        } else{
            gpsInfo.setImageResource(R.drawable.meetup_post_gps_icon);
        }

        city1.setText(city1Text);
        city2.setText(city1Text);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        meetupDate.setText("📆 " + dateString);

        userName.setText(userNameText);
        if(userSexText == "female"){
            userSex.setImageResource(R.drawable.woman_icon);
        } else{
            userSex.setImageResource(R.drawable.man_icon);
        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString2 = dateFormat.format(userBirthText);
        userBirth.setText(dateString2);

        contents.setText(contentsText);

        //수정, 삭제 버튼 추가
    }
}
