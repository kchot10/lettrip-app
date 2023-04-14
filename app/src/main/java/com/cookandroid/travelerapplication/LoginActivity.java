package com.cookandroid.travelerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "3.34.179.249"; //본인 IP주소를 넣으세요.

    EditText email_edittext, password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);

        findViewById(R.id.login_button).setOnClickListener(v -> {
            String email = email_edittext.getText().toString().trim();
            String pwd = password_edittext.getText().toString().trim();

            CheckData_Pwd task = new CheckData_Pwd();
            task.execute("http://"+IP_ADDRESS+"/0411/pwd_check.php",email,pwd);
            new Handler().postDelayed(() -> {
                String withdraw_result = task.get_return_string();
                if (withdraw_result.equals("인증 성공")){
                    Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (withdraw_result.equals("인증 실패")) {
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                } else if (withdraw_result.equals("사용자 없음")) {
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                }
            }, 500); // 0.5초 지연 시간
        });


        findViewById(R.id.signup_button).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Signup_Php_Mysql.class);
            startActivity(intent);
        });

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, login.class);
                startActivity(intent);
            }
        });


    }
}