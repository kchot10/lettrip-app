package com.cookandroid.travelerapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;
import java.util.Random;

public class Signup_Php_Mysql extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // 수정사항
    private static String IP_ADDRESS = "3.34.179.249"; //본인 IP주소를 넣으세요.

    private static String TAG = "phptest"; //phptest log 찍으려는 용도

    // 수정사항
    // final 수정사항
    // 충열 변경사항
    private TextView signup_id;
    private TextView signup_pwd;
    private TextView signup_pwd2;
    private TextView signup_name;

    private TextView signup_image_url;
    private TextView signup_nickname;


    private Button signup_button;
    private ImageView back;

    private TextView mTextViewResult;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    String code;
    private Button sendButton;
    private Button code_check_button;
    private EditText emailEditText;
    private EditText code_edittext;

    String check = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up);

        signup_id = (EditText) findViewById(R.id.signup_id);
        signup_pwd = (EditText) findViewById(R.id.signup_pwd);
        signup_pwd2 = (EditText) findViewById(R.id.signup_pwd2);
        signup_name = (EditText) findViewById(R.id.signup_name);
        signup_image_url = (EditText) findViewById(R.id.signup_image_url);
        signup_nickname = (EditText) findViewById(R.id.signup_nickname);
        signup_button = (Button) findViewById(R.id.join_button);
        back = (ImageView) findViewById(R.id.back);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        timerTextView = findViewById(R.id.timerTextView);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
        sendButton = findViewById(R.id.send_button);
        emailEditText = findViewById(R.id.signup_id);

        code_check_button = findViewById(R.id.code_check_button);
        code_edittext = findViewById(R.id.code_edittext);

        Intent intent_get = getIntent();
        String email_address = intent_get.getStringExtra("email_address");
        signup_id.setText(email_address);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = signup_id.getText().toString().trim();
                String pwd = signup_pwd.getText().toString().trim();
                String pwdcheck = signup_pwd2.getText().toString().trim();
                String name = signup_name.getText().toString().trim();
                String image_url = signup_image_url.getText().toString().trim();
                String nickname = signup_nickname.getText().toString().trim();
                String provider_type = "0";


                //회원가입을 할 때 예외 처리를 해준 것이다.
                if (email.equals("")  || pwd.equals("") || pwdcheck.equals("") || name.equals("") || image_url.equals("") || nickname.equals("") || code.equals(""))
                {
                    Toast.makeText(Signup_Php_Mysql.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(pwd.equals(pwdcheck)) {
                        if(pwd.length()<=5){
                            Toast.makeText(Signup_Php_Mysql.this, "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if(!email.contains("@") || !email.contains(".com")){
                            Toast.makeText(Signup_Php_Mysql.this, "아이디에 @ 및 .com을 포함시키세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!check.equals("1")) {
                            Toast.makeText(Signup_Php_Mysql.this, "이메일 인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            InsertData task = new InsertData(); //PHP 통신을 위한 InsertData 클래스의 task 객체 생성
                            task.execute("http://"+IP_ADDRESS+"/0411/android_log_inset_php.php",email,hashPassword(pwd),name, image_url, nickname, provider_type);
                            Toast.makeText(Signup_Php_Mysql.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Signup_Php_Mysql.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString().trim();


                if (emailAddress.isEmpty()) {
                    Toast.makeText(Signup_Php_Mysql.this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                CheckData_Email task = new CheckData_Email();
                task.execute("http://"+IP_ADDRESS+"/0411/email_check.php",emailAddress);
                new Handler().postDelayed(() -> {
                    String result = task.get_return_string();
                    if (result.equals("성공")) {
                        // 6자리 랜덤 숫자 생성
                        code = CodeGenerator.generateCode();
                        code_edittext.setVisibility(View.VISIBLE);
                        timerTextView.setVisibility(View.VISIBLE);
                        code_check_button.setVisibility(View.VISIBLE);
                        // 인증 메일 전송
                        SendMailTask sendMailTask = new SendMailTask();
                        sendMailTask.execute(emailAddress, String.valueOf(code));
                        emailEditText.setFocusable(false);
                        sendButton.setVisibility(View.INVISIBLE);
                        startTimer();
                        Toast.makeText(Signup_Php_Mysql.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                    } else if (result.equals("실패")) {
                        emailEditText.setText("");
                        Toast.makeText(Signup_Php_Mysql.this, "중복된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 500); // 0.5초 지연 시간



            }
        });

        code_check_button.setOnClickListener(v -> {
            String code_string = code_edittext.getText().toString().trim();
            String emailAddress = emailEditText.getText().toString().trim();
            if ( code_string.equals(code)){

                code_edittext.setVisibility(View.INVISIBLE);
                timerTextView.setVisibility(View.INVISIBLE);
                code_check_button.setVisibility(View.INVISIBLE);
                Toast.makeText(Signup_Php_Mysql.this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                check="1";
            } else if (code.equals("0")) {
                Toast.makeText(Signup_Php_Mysql.this, "코드가 만료되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Signup_Php_Mysql.this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                code = "0";
            }
        };
        countDownTimer.start();
    }

    private class SendMailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String emailAddress = params[0];
                String code = params[1];

                MailSender sender = new MailSender("kchot10@gmail.com", "akojosbblxtcelur");
                sender.sendMail("Lettrip 이메일 인증 코드입니다.", "아래 코드를 Lettrip 이메일 인증 코드란에 입력해주세요. \n CODE : " + code, "kchot10@naver.com");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(Signup_Php_Mysql.this, "인증 메일을 발송했습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public static class CodeGenerator {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final int CODE_LENGTH = 6;

        public static String generateCode() {
            Random random = new Random();
            StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                codeBuilder.append(CHARACTERS.charAt(randomIndex));
            }
            return codeBuilder.toString();
        }
    }
}