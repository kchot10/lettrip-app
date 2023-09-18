package com.cookandroid.travelerapplication.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.SendMail;
import com.cookandroid.travelerapplication.helper.S3Uploader;
import com.cookandroid.travelerapplication.task.CheckData_Email;
import com.cookandroid.travelerapplication.task.InsertData_SignUp;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements S3Uploader.OnUploadListener {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_IMAGE = 200;

    // 수정사항
    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.

    private static String TAG = "phptest"; //phptest log 찍으려는 용도

    private String mImageUrl = "";
    private TextView signup_id;
    private TextView signup_pwd;
    private TextView signup_pwd2;
    private TextView signup_name;
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
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        setContentView(R.layout.activity_sign_up);

        signup_id = (EditText) findViewById(R.id.signup_id);
        signup_pwd = (EditText) findViewById(R.id.signup_pwd);
        signup_pwd2 = (EditText) findViewById(R.id.signup_pwd2);
        signup_name = (EditText) findViewById(R.id.signup_name);
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
                String nickname = signup_nickname.getText().toString().trim();


                //회원가입을 할 때 예외 처리를 해준 것이다.
                if (email.equals("")  || pwd.equals("") || pwdcheck.equals("") || name.equals("") || mImageUrl.equals("") || nickname.equals("") || code.equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(pwd.equals(pwdcheck)) {
                        if(pwd.length()<=5){
                            Toast.makeText(SignUpActivity.this, "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if(!email.contains("@") || !email.contains(".com")){
                            Toast.makeText(SignUpActivity.this, "아이디에 @ 및 .com을 포함시키세요.", Toast.LENGTH_SHORT).show();
                        }
                        else if (!check.equals("1")) {
                            Toast.makeText(SignUpActivity.this, "이메일 인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            InsertData_SignUp task = new InsertData_SignUp(); //PHP 통신을 위한 InsertData 클래스의 task 객체 생성
                            task.execute("http://"+IP_ADDRESS+"/0411/android_log_inset_php.php",email,hashPassword(pwd),name, mImageUrl, nickname, "LOCAL");
                            Toast.makeText(SignUpActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString().trim();


                if (emailAddress.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                CheckData_Email task = new CheckData_Email();
                task.execute("http://"+IP_ADDRESS+"/0411/email_check.php",emailAddress);
                new Handler().postDelayed(() -> {
                    String result = task.get_return_string();
                    if (result.equals("성공")) {
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
                    } else if (result.equals("실패")) {
                        emailEditText.setText("");
                        Toast.makeText(SignUpActivity.this, "중복된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 1000); // 0.5초 지연 시간
            }
        });

        code_check_button.setOnClickListener(v -> {
            String code_string = code_edittext.getText().toString().trim();
            String emailAddress = emailEditText.getText().toString().trim();
            if ( code_string.equals(code)){

                code_edittext.setVisibility(View.INVISIBLE);
                timerTextView.setVisibility(View.INVISIBLE);
                code_check_button.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, "인증 완료되었습니다.", Toast.LENGTH_SHORT).show();
                check="1";
            } else if (code.equals("0")) {
                Toast.makeText(SignUpActivity.this, "코드가 만료되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(SignUpActivity.this, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.add_image_url).setOnClickListener(v -> {
            requestImageUpload();
        });

        requestPermissions();
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }
    private void requestImageUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    uploadImage(imageUri);
                }
            }
        }
    }
    private void uploadImage(Uri imageUri) {
        S3Uploader s3Uploader = new S3Uploader(this);
        s3Uploader.uploadImage(imageUri, this);
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
                code = "0";
            }
        };
        countDownTimer.start();
    }

    private void Refresh() {
        ImageView profile_sign_up = findViewById(R.id.profile_sign_up);
        Glide.with(this)
                .load(mImageUrl)
                .into(profile_sign_up);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onSuccess(String imageUrl, String fileSize, String originalFileName, String storedFileName) {
        mImageUrl = imageUrl;
        Toast.makeText(this, "이미지 업로드 성공: " + imageUrl, Toast.LENGTH_SHORT).show();
        Refresh();
    }

    @Override
    public void onFailure() {

    }
}