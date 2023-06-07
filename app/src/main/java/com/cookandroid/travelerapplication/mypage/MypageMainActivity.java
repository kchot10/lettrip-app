package com.cookandroid.travelerapplication.mypage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.account.LoginActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.util.ArrayList;

public class MypageMainActivity extends AppCompatActivity {
    TextView tripLikeBtn;
    FileHelper fileHelper;
    String IP_ADDRESS, user_id;
    ArrayList<UserInfo> userInfoArrayList;
    TextView userName, userEmail, pointNum;
    ImageView profile_imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        pointNum = findViewById(R.id.pointNum);
        profile_imageView = findViewById(R.id.profile_imageView);

        Refresh();

        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        //회원 수정 팝업 열기
        TextView user_ModifyBtn = findViewById(R.id.user_ModifyBtn);
        user_ModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageModifyActivity.class);
                startActivity(intent);
            }
        });

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
               startActivity(intent);
            }
        });

        tripLikeBtn = findViewById(R.id.tripLikeBtn);
        tripLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageLikeTripActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Refresh() {
        userInfoArrayList = new ArrayList<>();
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(userInfoArrayList);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", user_id);
        new Handler().postDelayed(() -> {
            try {
                userName.setText(userInfoArrayList.get(0).getNickname());
                try {
                    Glide.with(this)
                            .load(userInfoArrayList.get(0).getStored_file_url())
                            .placeholder(R.drawable.user)
                            .into(profile_imageView);
                }catch (Exception e){
                    Log.e("nullException", "이미지 없음");
                }
                userEmail.setText(userInfoArrayList.get(0).getEmail());
                pointNum.setText(userInfoArrayList.get(0).getPoint()+"P");
            }catch (Exception e){
                Log.e("nullException", "사용자 정보 불러오기 실패");
            }
        }, 300); // 0.5초 지연 시간
    }
}
