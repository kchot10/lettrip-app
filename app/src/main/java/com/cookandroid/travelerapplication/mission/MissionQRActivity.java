package com.cookandroid.travelerapplication.mission;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

public class MissionQRActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_qr);

        webView = findViewById(R.id.QR_webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.naver.com/"); //웹사이트 링크로 바꾸기
    }
}
