package com.cookandroid.travelerapplication;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

public class MainActivity extends AppCompatActivity {

    private Button sendButton;
    private Button check_button;
    private EditText emailEditText;
    private EditText code_edittext;


    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("youn", "hellow");

        sendButton = findViewById(R.id.send_button);
        emailEditText = findViewById(R.id.email_edittext);

        check_button = findViewById(R.id.code_check_button);
        code_edittext = findViewById(R.id.code_edittext);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString().trim();
                if (emailAddress.isEmpty()) {
                    Toast.makeText(MainActivity.this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 6자리 랜덤 숫자 생성
                Random random = new Random();
                code = random.nextInt(900000) + 100000;

                // 인증 메일 전송
                SendMailTask sendMailTask = new SendMailTask();
                sendMailTask.execute(emailAddress, String.valueOf(code));

            }
        });

        check_button.setOnClickListener(v -> {
            String code_string = code_edittext.getText().toString().trim();

            if ( Integer.parseInt(code_string) == code){
                Intent intent = new Intent(MainActivity.this, Signup_Php_Mysql.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

        });

        findViewById(R.id.withdraw_button).setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, WithdrawActivity.class);
            startActivity(intent);

        });



    }

    private class SendMailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String emailAddress = params[0];
                String code = params[1];

                MailSender sender = new MailSender("kchot10@gmail.com", "akojosbblxtcelur");
                sender.sendMail("인증번호 메일입니다.", "인증번호는 " + code + " 입니다.", "kchot10@naver.com");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "인증 메일을 발송했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
