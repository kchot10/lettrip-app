package com.cookandroid.travelerapplication.mypage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.account.LoginActivity;

public class MypageMainActivity extends AppCompatActivity {
    TextView tripLikeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main);

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
}
