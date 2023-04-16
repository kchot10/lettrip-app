package com.cookandroid.travelerapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfile;
import com.navercorp.nid.profile.data.NidProfileResponse;


public class login extends AppCompatActivity{


    public static Context mContext;

    private ActivityResultLauncher<Intent> GoogleSignResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_designed);

        //-----------------------
        //소셜 로그인 구현 - 카카오
        //키 해쉬 발급
        Log.d("getKeyHash", "" + getKeyHash(login.this));

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
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(login.this)){ //카톡이 설치 되어 있으면
                    UserApiClient.getInstance().loginWithKakaoTalk(login.this, callback);
                } else{ //카카오톡이 설치 되어 있지 않으면
                    UserApiClient.getInstance().loginWithKakaoAccount(login.this, callback);
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
                        String email = profile.getEmail();
                        String name = profile.getName();
                        String profileUrl = profile.getProfileImage();

                        Log.e("naver account", email);
                        Log.e("naver account", name);
                        Log.e("naver account", profileUrl);

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
            String email = account != null ? account.getEmail() : ""; //이메일
            String name = account != null ? account.getDisplayName() : ""; //전체 이름
            String photoUrl = account != null? String.valueOf(account.getPhotoUrl()) : ""; //프로필 사진 url


            Log.e("Google account", email);
            Log.e("Google account", name);
            Log.e("Google account", photoUrl);
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
                    String kakao_email = user.getKakaoAccount().getEmail();
                    String kakao_name = user.getKakaoAccount().getProfile().getNickname();
                    String kakao_profilePhoto = user.getKakaoAccount().getProfile().getProfileImageUrl();

                    Toast.makeText(getApplicationContext(), kakao_email + ", " + kakao_name + ", " + kakao_profilePhoto, Toast.LENGTH_LONG).show();
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

}