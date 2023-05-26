package com.cookandroid.travelerapplication.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.CheckData_Pwd;
import com.cookandroid.travelerapplication.task.DeleteData;

public class WithdrawActivity extends AppCompatActivity {
    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.

    private static String TAG = "youn"; //phptest log 찍으려는 용도

    EditText password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        password_edittext = (EditText) findViewById(R.id.password_edittext);


        findViewById(R.id.join_button).setOnClickListener(v -> {

            String email = fileHelper.readFromFile("email");
            String pwd = password_edittext.getText().toString().trim();
            String provider_type = "LOCAL";
            if (pwd.equals("NAVER")||pwd.equals("naver")){
                provider_type = "NAVER";
                pwd = pwd.toUpperCase();
            } else if (pwd.equals("GOOGLE")||pwd.equals("google")){
                provider_type = "GOOGLE";
                pwd = pwd.toUpperCase();
            } else if (pwd.equals("KAKAO")||pwd.equals("kakao")){
                provider_type = "KAKAO";
                pwd = pwd.toUpperCase();
            }


            if (email.equals("")  || pwd.equals(""))
                {
                    Toast.makeText(WithdrawActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            else{
                if(!email.contains("@") || !email.contains(".com")){
                    Toast.makeText(WithdrawActivity.this, "아이디에 @ 및 .com을 포함시키세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    CheckData_Pwd task = new CheckData_Pwd(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                    task.execute("http://"+IP_ADDRESS+"/0411/pwd_check.php",email,pwd, provider_type);
                    new Handler().postDelayed(() -> {
                        String withdraw_result = task.get_return_string();

                        if (withdraw_result.equals("인증 실패")) {
                            Toast.makeText(WithdrawActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (withdraw_result.equals("사용자 없음")) {
                            Toast.makeText(WithdrawActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (withdraw_result.isEmpty()){
                            return;
                        } else {
                            DeleteData dtask = new DeleteData(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                            dtask.execute("http://"+IP_ADDRESS+"/0411/withdraw.php",email);
                            Toast.makeText(WithdrawActivity.this, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 1000); // 0.5초 지연 시간
                }
            }

        });

    }
}