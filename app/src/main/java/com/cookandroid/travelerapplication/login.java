package com.cookandroid.travelerapplication;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class login extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_designed);

        //소셜 로그인 구현 - 카카오
        //키 해쉬 발급
        Log.d("getKeyHash", "" + getKeyHash(login.this));

        ImageButton button = findViewById(R.id.naverLogin_btn);

        //카카오 설치되어 있는지 확인하는 메서드 -> 카카오 제공 콜백 객체 이용
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                updateKakaoLoginUi();
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