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

import java.text.ParseException;
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

        Intent intent = getIntent();
        MeetupPost meetupPost = (MeetupPost) intent.getSerializableExtra("meetup_post");

        //db에서 데이터 받아와서 화면 구성
        String city1Text = meetupPost.getProvince();//"서울특별시";
        String city2Text= meetupPost.getCity();//"서울";
        String dateString = meetupPost.getCreated_date();//dateFormat.format(date);
        String userNameText = meetupPost.getNickname();
        String userSexText = meetupPost.getSex();
        String contentsText = meetupPost.getContent();

        //- 데이터 불러오는 코드 추가**

        //불러온 데이터로 화면 업데이트
        if(meetupPost.getIs_gps_enabled().equals("1")){
            gpsInfo.setImageResource(R.drawable.meetup_post_gps_icon);
        } else{
            gpsInfo.setImageResource(R.drawable.meetup_post_gps_icon);
        }

        city1.setText(city1Text);
        city2.setText(city1Text);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        meetupDate.setText("📆 " + reformatDate(dateString));

        userName.setText(userNameText);
        if(userSexText == "FEMALE"){
            userSex.setImageResource(R.drawable.woman_icon);
        } else if(userSexText == "MALE"){
            userSex.setImageResource(R.drawable.man_icon);
        }

        String dateString2 = (meetupPost.getBirth_date().equals("null") ? "":
                reformatDate(meetupPost.getBirth_date()));
        userBirth.setText(dateString2);

        contents.setText(contentsText);

        //수정, 삭제 버튼 추가
    }

    public static String reformatDate(String originalDate) {
        try {
            String originalFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
            String targetFormat = "yyyy.MM.dd";
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat(originalFormat);
            SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);

            Date date = sourceDateFormat.parse(originalDate);
            return targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
