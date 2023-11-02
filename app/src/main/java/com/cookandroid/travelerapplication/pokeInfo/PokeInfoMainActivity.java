package com.cookandroid.travelerapplication.pokeInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.PokeListActivity;
import com.cookandroid.travelerapplication.mypage.MyTravelActivity;
import com.cookandroid.travelerapplication.record.PlanningMain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PokeInfoMainActivity extends AppCompatActivity {
    ImageButton backBtn;
    ImageView profilePhoto;
    TextView userName;
    TextView userNickname;
    ImageView userSex;
    TextView userBirth;
    TextView meetupSuccessNum;
    TextView meetupFailNum;
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;

    FileHelper fileHelper;
    String IP_ADDRESS, user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");

        //상단에 표시될 유저 정보들 받아와서 저장할 변수들
        String nickname = "떵";
        String name="박성화";
        String sex="MALE";
        String birth="1998-04-03";
        int successNum=2;
        int failNum=0;
        String imageURL="https://ibb.co/f2tmMDG";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd");

        backBtn = findViewById(R.id.backBtn2);
        profilePhoto = findViewById(R.id.profilePhoto);
        userName = findViewById(R.id.uesrName);
        userNickname = findViewById(R.id.userNickName);
        userSex = findViewById(R.id.userSex);
        userBirth = findViewById(R.id.userBirth);
        meetupSuccessNum = findViewById(R.id.meetupSuccessNum);
        meetupFailNum = findViewById(R.id.meetupFailNum);
        layout1 = findViewById(R.id.LinearLayout1);
        layout2 = findViewById(R.id.LinearLayout2);
        layout3 = findViewById(R.id.LinearLayout3);

        //todo: db에서 사용자 정보 받아오기 코드 추가
        //닉네임, 이름, 성별, 생년월일, 매칭 완료 횟수, 매칭 실패 횟수, 프로필url



        //불러온 정보로 ui 세팅
        userNickname.setText(nickname);
        userName.setText(name);
        if(sex == "MALE"){
            userSex.setImageResource(R.drawable.man_icon);
        } else{
            userSex.setImageResource(R.drawable.woman_icon);
        }
        userBirth.setText("");
        try {
            Date date = inputFormat.parse(birth);
            String formattedDate = outputFormat.format(date);
            userBirth.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        meetupSuccessNum.setText("매칭 완료 " + successNum + "회");
        meetupFailNum.setText("매칭 실패 " + successNum + "회");


        //페이지 이동
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PokeListActivity.class);
                startActivity(intent);
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() { //매칭 한줄평으로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OneLineReviewActivity.class);
                startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() { //계획 글 보기로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyTravelActivity.class);
                intent.putExtra("visited/not", "not");
                startActivity(intent);
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() { //기록 글 보기로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyTravelActivity.class);
                intent.putExtra("visited/not", "visited");
                startActivity(intent);
            }
        });
    }
}