package com.cookandroid.travelerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class WithdrawActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "43.201.18.229"; //본인 IP주소를 넣으세요.

    private static String TAG = "youn"; //phptest log 찍으려는 용도

    EditText email_edittext, password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        email_edittext = (EditText) findViewById(R.id.email_edittext);
        password_edittext = (EditText) findViewById(R.id.password_edittext);

        findViewById(R.id.check_button).setOnClickListener(v -> {

            String email = email_edittext.getText().toString().trim();
            String pwd = password_edittext.getText().toString().trim();

            if (email.equals("")  || pwd.equals(""))
                {
                    Toast.makeText(WithdrawActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            else{
                if(!email.contains("@") || !email.contains(".com")){
                    Toast.makeText(WithdrawActivity.this, "아이디에 @ 및 .com을 포함시키세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    DeleteData task = new DeleteData(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                    task.execute("http://"+IP_ADDRESS+"/0410/withdraw.php",email,pwd);
                    Toast.makeText(WithdrawActivity.this, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}