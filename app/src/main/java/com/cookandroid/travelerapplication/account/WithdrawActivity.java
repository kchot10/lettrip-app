package com.cookandroid.travelerapplication.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.CheckData_Pwd;
import com.cookandroid.travelerapplication.task.DeleteData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class WithdrawActivity extends AppCompatActivity {
    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.

    private static String TAG = "youn"; //phptest log 찍으려는 용도

    EditText email_edittext, password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        email_edittext = (EditText) findViewById(R.id.email_edittext);
        password_edittext = (EditText) findViewById(R.id.password_edittext);

        String email = email_edittext.getText().toString().trim();
        String pwd = password_edittext.getText().toString().trim();

        findViewById(R.id.join_button).setOnClickListener(v -> {


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
                    task.execute("http://"+IP_ADDRESS+"/0411/pwd_check.php",email,pwd);
                    new Handler().postDelayed(() -> {
                        String withdraw_result = task.get_return_string();
                        if (withdraw_result.equals("인증 성공")){
                            DeleteData dtask = new DeleteData(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                            dtask.execute("http://"+IP_ADDRESS+"/0411/withdraw.php",email);
                            Toast.makeText(WithdrawActivity.this, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT).show();




                        } else if (withdraw_result.equals("인증 실패")) {
                            Toast.makeText(WithdrawActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                        } else if (withdraw_result.equals("사용자 없음")) {
                            Toast.makeText(WithdrawActivity.this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, 500); // 0.5초 지연 시간
                }
            }

        });




        //---------------------소셜로그인 탈퇴
        
        //카카오 탈퇴
        Button socialWithdrawBtn = findViewById(R.id.withdraw_button2);
        socialWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AuthApiClient.getInstance().hasToken()) {
                    UserApiClient.getInstance().accessTokenInfo((accessTokenInfo, error) -> {
                        if (error != null) {
                            // 액세스 토큰 정보 가져오기 실패
                            Log.e(TAG, "액세스 토큰 정보 가져오기 실패", error);
                        } else if (accessTokenInfo != null) {
                            // 액세스 토큰 정보 가져오기 성공

                            //카카오 토큰 끊기
                            UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                                @Override
                                public Unit invoke(Throwable throwable) {
                                    DeleteData dtask = new DeleteData(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                                    dtask.execute("http://"+IP_ADDRESS+"/0411/withdraw.php",email);
                                    Toast.makeText(WithdrawActivity.this, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                    //액티비티 새로고침
                                    finish();//인텐트 종료
                                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                                    Intent intent = getIntent(); //인텐트
                                    startActivity(intent); //액티비티 열기
                                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                                    return null;
                                }
                            });

                        }
                        return null;
                    });
                }
            }
        });


        //구글 탈퇴
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,  GoogleSignInOptions.DEFAULT_SIGN_IN);
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DeleteData dtask = new DeleteData(); //PHP 통신을 위한 DeleteData 클래스의 task 객체 생성
                        dtask.execute("http://"+IP_ADDRESS+"/0411/withdraw.php",email);
                        Toast.makeText(WithdrawActivity.this, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        //액티비티 새로고침
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                    }
                });
    }
}