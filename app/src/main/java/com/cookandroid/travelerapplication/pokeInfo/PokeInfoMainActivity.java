package com.cookandroid.travelerapplication.pokeInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.PokeListActivity;
import com.cookandroid.travelerapplication.meetup.PokeListAdapter;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.mypage.MyTravelActivity;
import com.cookandroid.travelerapplication.record.PlanningMain;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PokeInfoMainActivity extends AppCompatActivity implements SelectData_UserInfo.AsyncTaskCompleteListener{
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
    SimpleDateFormat inputFormat, outputFormat;
    String request_user_id;

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

        inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        outputFormat = new SimpleDateFormat("yyyy.MM.dd");

        backBtn = findViewById(R.id.backBtn2);
        profilePhoto = findViewById(R.id.profilePhoto);
        profilePhoto.setClipToOutline(true);
        userName = findViewById(R.id.uesrName);
        userNickname = findViewById(R.id.userNickName);
        userSex = findViewById(R.id.userSex);
        userBirth = findViewById(R.id.userBirth);
        meetupSuccessNum = findViewById(R.id.meetupSuccessNum);
        meetupFailNum = findViewById(R.id.meetupFailNum);
        layout1 = findViewById(R.id.LinearLayout1);
        layout2 = findViewById(R.id.LinearLayout2);
        layout3 = findViewById(R.id.LinearLayout3);

        ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(userInfoArrayList, this);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", getIntent().getStringExtra("request_user_id"));
        fileHelper.writeToFile("partner_user_id", getIntent().getStringExtra("request_user_id"));
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
                intent.putExtra("previousActivity", "PokeInfoMainActivity");
                intent.putExtra("request_user_id", request_user_id);
                intent.putExtra("visited/not", "not");
                startActivity(intent);
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() { //기록 글 보기로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyTravelActivity.class);
                intent.putExtra("previousActivity", "PokeInfoMainActivity");
                intent.putExtra("request_user_id", request_user_id);
                intent.putExtra("visited/not", "visited");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onTaskComplete_SelectData_UserInfo(UserInfo result) {
        runOnUiThread(() -> {
            request_user_id = result.getUser_id();
            //불러온 정보로 ui 세팅
            userNickname.setText(result.getNickname());
            userName.setText(result.getName());
            if(result.getSex() == "MALE"){
                userSex.setImageResource(R.drawable.man_icon);
            } else if(result.getSex() == "FEMALE"){
                userSex.setImageResource(R.drawable.woman_icon);
            }
            userBirth.setText("");
            try {
                if (result.getBirth_date() != "null") {
                    Date date = inputFormat.parse(result.getBirth_date());
                    String formattedDate = outputFormat.format(date);
                    userBirth.setText(formattedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            meetupSuccessNum.setText("매칭 완료 " + result.getMeet_up_completed_count() + "회");
            meetupFailNum.setText("매칭 실패 " + result.getMeet_up_cancelled_count() + "회");

            if(result.getStored_file_url() != "null") {
                Glide.with(this)
                        .load(result.getStored_file_url())
                        .into(profilePhoto);
            }
        });

    }
}
