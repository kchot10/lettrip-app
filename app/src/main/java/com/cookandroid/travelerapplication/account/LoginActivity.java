package com.cookandroid.travelerapplication.account;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cookandroid.travelerapplication.chat.ChatRoomActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.main.MainActivityDesigned;
import com.cookandroid.travelerapplication.task.CheckData_Email;
import com.cookandroid.travelerapplication.task.CheckData_Pwd;
import com.cookandroid.travelerapplication.task.InsertData_SignUp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfile;
import com.navercorp.nid.profile.data.NidProfileResponse;

import org.mindrot.jbcrypt.BCrypt;


public class LoginActivity extends AppCompatActivity{


    private static String ec2_ADDRESS = "3.34.136.218";
    private static String IP_ADDRESS;
    public static Context mContext;
    FileHelper fileHelper;
    String email;
    String pwd;
    String provider_type;

    private ActivityResultLauncher<Intent> GoogleSignResultLauncher;
    private int ACCESS_FINE_LOCATION = 1000;     // Request Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fileHelper = new FileHelper(this);
        if(!getIntent().getStringExtra("inputIPAddress").equals("")){
            fileHelper.writeToFile("IP_ADDRESS", getIntent().getStringExtra("inputIPAddress"));
        }
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        SharedPreferences preference = getPreferences(MODE_PRIVATE);
        boolean isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true);
        if (isFirstCheck) {
            // 최초 권한 요청
            preference.edit().putBoolean("isFirstPermissionCheck", false).apply();
            String[] array = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, array, ACCESS_FINE_LOCATION);
        }

        findViewById(R.id.logoBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressSetActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.findPW_Btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, FindPwdActivity.class);
            startActivity(intent);
        });

        //회원가입 버튼을 눌렀을 때
        findViewById(R.id.joinBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        //로그인 버튼을 눌렀을 때
        EditText email_edittext = findViewById(R.id.id_plainText);
        EditText password_edittext = findViewById(R.id.pw_plainText);

        findViewById(R.id.loginBtn).setOnClickListener(v -> {
            email = email_edittext.getText().toString().trim();
            pwd = password_edittext.getText().toString().trim();
            provider_type = "LOCAL";
            checkData_Pwd_function(email, pwd, provider_type);
        });


        //-----------------------
        //소셜 로그인 구현 - 카카오
        //키 해쉬 발급
        Log.d("getKeyHash", "" + getKeyHash(LoginActivity.this));

        ImageButton button = findViewById(R.id.kakaoLogin_btn);

        //카카오 설치되어 있는지 확인하는 메서드 -> 카카오 제공 콜백 객체 이용
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                updateKakaoLoginUi(); //여기서 토큰 전달 안되면 로그인 실패
                return null;
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //카톡 설치 여부 확인
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){ //카톡이 설치 되어 있으면
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                } else{ //카카오톡이 설치 되어 있지 않으면
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                }
            }

        });


        //-----------------------
        //네이버 로그인 구현
        //네아로 객체 초기화
        NaverIdLoginSDK.INSTANCE.initialize(getApplicationContext(), "hSHzjzQDqmaiM8qbHVA4","DwmFlqg0t5", "Lettrip");

        NidOAuthLoginButton buttonOAuthLoginImg = findViewById(R.id.buttonOAuthLoginImg);
        buttonOAuthLoginImg.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onSuccess() { //로그인 성공시 처리할 내용
                String accessToken = NaverIdLoginSDK.INSTANCE.getAccessToken();
                NidOAuthLogin mOAuthLoginModule = new NidOAuthLogin();
                mOAuthLoginModule.callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                    @Override
                    public void onSuccess(NidProfileResponse nidProfileResponse) {
                        NidProfile profile = nidProfileResponse.getProfile();
                        email = profile.getEmail();
                        String name = profile.getName();
                        String profileUrl = profile.getProfileImage();

                        String social = "NAVER";
                        CheckData_Email checkData_email = new CheckData_Email();
                        checkData_email.execute("http://"+IP_ADDRESS+"/0411/email_check.php",email);
                        new Handler().postDelayed(() -> {
                            String result = checkData_email.get_return_string();
                            if (result.equals("실패")) {
                                checkData_Pwd_function(email, social, social);
                            } else if (result.equals("성공")) {
                                InsertData_SignUp task = new InsertData_SignUp(); //PHP 통신을 위한 InsertData 클래스의 task 객체 생성
                                task.execute("http://"+IP_ADDRESS+"/0411/android_log_inset_php.php",email,hashPassword(social),name, profileUrl, name, social);
                                new Handler().postDelayed(() -> {
                                    checkData_Pwd_function(email, social, social);
                                }, 500); // 0.5초 지연 시간
                            }
                        }, 500); // 0.5초 지연 시간

                    }

                    @Override
                    public void onFailure(int i, @NonNull String s) {

                    }

                    @Override
                    public void onError(int i, @NonNull String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, @NonNull String s) { //로그인 실패시 처리할 내용

            }

            @Override
            public void onError(int i, @NonNull String s) { //로그인 취소시 처리할 내용

            }
        });


        //-------------------------
        //구글 로그인 구현
        //사용자의 id와 기본 프로필 정보 요청
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //gso를 통해 가져올 클라이언트 정보를 담을 인스턴스
        GoogleSignInClient mGoogleSignIncClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        //로그인 되어 있는지 확인
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account == null) {
            Log.e("Google account", "로그인 안되있음");
        } else {
            Log.e("Google account", "로그인 완료된 상태");
            Intent intent = new Intent(getApplicationContext(), MainActivityDesigned.class);
            startActivity(intent);
        }

        //사용자 객체 가지고 오기
        GoogleSignResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                });

        SignInButton google_login_button = findViewById(R.id.googleLoginBtn);
        google_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIntent = mGoogleSignIncClient.getSignInIntent();
                GoogleSignResultLauncher.launch(signIntent);
            }
        });


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) { //사용자에 대한 정보 가져오기
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            email = account != null ? account.getEmail() : ""; //이메일
            String name = account != null ? account.getDisplayName() : ""; //전체 이름
            String profileUrl = account != null? String.valueOf(account.getPhotoUrl()) : ""; //프로필 사진 url

            Toast.makeText(this,
                    "email:"+email
                            +"name:"+name
                            +"profileUrl:"+profileUrl,
                    Toast.LENGTH_SHORT).show();
            Log.e("Google account", email);
            Log.e("Google account", name);
            Log.e("Google account", profileUrl);

            String social = "GOOGLE";
            CheckData_Email checkData_email = new CheckData_Email();
            checkData_email.execute("http://"+IP_ADDRESS+"/0411/email_check.php",email);
            new Handler().postDelayed(() -> {
                String result = checkData_email.get_return_string();
                if (result.equals("실패")) {
                    checkData_Pwd_function(email, social, social);
                } else if (result.equals("성공")) {
                    InsertData_SignUp task = new InsertData_SignUp(); //PHP 통신을 위한 InsertData 클래스의 task 객체 생성
                    task.execute("http://"+IP_ADDRESS+"/0411/android_log_inset_php.php",email,hashPassword(social),name, profileUrl, name, social);
                    new Handler().postDelayed(() -> {
                        checkData_Pwd_function(email, social, social);
                    }, 500); // 0.5초 지연 시간
                }
            }, 500); // 0.5초 지연 시간

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        } catch (ApiException e) {
            Log.e("Google account", "signInResult:failed Code = " + e.getStatusCode());
        }
    }



    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                //로그인이 되어있다면
                if(user != null){
                    //유저 정보 가져오기
                    email = user.getKakaoAccount().getEmail();
                    String name = user.getKakaoAccount().getProfile().getNickname();
                    String profileUrl = user.getKakaoAccount().getProfile().getProfileImageUrl();

                    String social = "KAKAO";
                    CheckData_Email checkData_email = new CheckData_Email();
                    checkData_email.execute("http://"+IP_ADDRESS+"/0411/email_check.php",email);
                    new Handler().postDelayed(() -> {
                        String result = checkData_email.get_return_string();
                        if (result.equals("실패")) {
                            checkData_Pwd_function(email, social, social);
                        } else if (result.equals("성공")) {
                            InsertData_SignUp task = new InsertData_SignUp(); //PHP 통신을 위한 InsertData 클래스의 task 객체 생성
                            task.execute("http://"+IP_ADDRESS+"/0411/android_log_inset_php.php",email,hashPassword(social),name, profileUrl, name, social);
                            new Handler().postDelayed(() -> {
                                checkData_Pwd_function(email, social, social);
                            }, 500); // 0.5초 지연 시간
                        }
                    }, 500); // 0.5초 지연 시간

                } else{
                    //로그인이 안되어 있을 때
                    Log.d("error", "로그인 안되어 있음");
                }
                return null;
            }
        });
    }



    //해쉬값 얻기
    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if(packageInfo == null){
                return null;
            }

            for(Signature signature : packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    private void checkData_Pwd_function(String email, String pwd, String provider_type) {

        CheckData_Pwd task = new CheckData_Pwd();
        task.execute("http://"+IP_ADDRESS+"/0411/pwd_check.php",email,pwd, provider_type);
        new Handler().postDelayed(() -> {
            String withdraw_result = task.get_return_string();
            if (withdraw_result.equals("인증 실패")) {
                Toast.makeText(this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
            } else if (withdraw_result.equals("사용자 없음")) {
                Toast.makeText(this, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
            } else if (withdraw_result.isEmpty()){
                Toast.makeText(this, "IP_ADDRESS를 확인하세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();

                // 로그인 성공 시 SharedPreferences에 이메일 저장
                String userEmail = email; // 이메일 정보 가져오기

                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", userEmail);
                editor.apply();

                Log.d("youn", withdraw_result);
                fileHelper.writeToFile("user_id", withdraw_result);
                fileHelper.writeToFile("email", email);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }, 1000); // 0.5초 지연 시간



    }

}