package com.cookandroid.travelerapplication.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.helper.SendMail;
import com.cookandroid.travelerapplication.task.CheckData_Email;
import com.cookandroid.travelerapplication.task.UpdateData_Pwd;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;

public class FindPwdActivity extends AppCompatActivity {

    String IP_ADDRESS;
    FileHelper fileHelper;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    String code, check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        EditText emailEditText = findViewById(R.id.signup_id);
        EditText code_edittext = findViewById(R.id.code_edittext);
        EditText signup_pwd = findViewById(R.id.signup_pwd);
        EditText signup_pwd2 = findViewById(R.id.signup_pwd2);
        Button code_check_button = findViewById(R.id.code_check_button);
        Button sendButton = findViewById(R.id.send_button);
        timerTextView = findViewById(R.id.timerTextView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString().trim();

                if (emailAddress.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                CheckData_Email task = new CheckData_Email();
                task.execute("http://"+IP_ADDRESS+"/0411/email_check.php",emailAddress);
                new Handler().postDelayed(() -> {
                    String result = task.get_return_string();
                    if (result.equals("성공")) {
                        emailEditText.setText("");
                        Toast.makeText(getApplicationContext(), "등록되지 않은 계정입니다. 회원가입 해주세요!", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("실패")) {
                        // 6자리 랜덤 숫자 생성
                        code_edittext.setVisibility(View.VISIBLE);
                        timerTextView.setVisibility(View.VISIBLE);
                        code_check_button.setVisibility(View.VISIBLE);
                        // 인증 메일 전송
                        SendMail mailServer = new SendMail(getApplicationContext());
                        mailServer.execute(emailAddress);
                        new Handler().postDelayed(() -> {
                            code = mailServer.getEmailCode();
                        }, 1000);
                        emailEditText.setFocusable(false);
                        sendButton.setVisibility(View.INVISIBLE);
                        startTimer();
                    }
                }, 1000); // 0.5초 지연 시간
            }
        });

        code_check_button.setOnClickListener(v -> {
            String code_string = code_edittext.getText().toString().trim();
            String emailAddress = emailEditText.getText().toString().trim();
            if (code_string.equals(code)){
                if (check.equals("0")) {
                    Toast.makeText(this, "코드가 만료되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    code_edittext.setVisibility(View.INVISIBLE);
                    timerTextView.setVisibility(View.INVISIBLE);
                    code_check_button.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    check = "1";
                    findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView8).setVisibility(View.VISIBLE);
                    findViewById(R.id.signup_pwd).setVisibility(View.VISIBLE);
                    findViewById(R.id.signup_pwd2).setVisibility(View.VISIBLE);
                    findViewById(R.id.join_button).setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.join_button).setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String pwd = signup_pwd.getText().toString().trim();
            String pwdcheck = signup_pwd2.getText().toString().trim();

            if (email.equals("")  || pwd.equals(""))
            {
                Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else {
                if (pwd.equals(pwdcheck)) {
                    if(pwd.length()<=5){
                        Toast.makeText(this, "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (check.equals("0")) {
                        Toast.makeText(this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        UpdateData_Pwd updateData_pwd = new UpdateData_Pwd();
                        updateData_pwd.execute("http://"+IP_ADDRESS+"/0411/updatedata_pwd.php", email, hashPassword(pwd));
                        finish();
                    }
                }
            }
        });
    }


    public String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(3 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds %= 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeLeftFormatted);
            }
            public void onFinish() {
                check = "0";
            }
        };
        countDownTimer.start();
    }
}