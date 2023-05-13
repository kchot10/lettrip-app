package com.cookandroid.travelerapplication.account;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        //kakao SDK 초기화
        KakaoSdk.init(this, "eaea80fc7e3f8ac8fd4c887da34a2f08");
    }
}
