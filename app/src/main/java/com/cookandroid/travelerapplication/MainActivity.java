package com.cookandroid.travelerapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.account.WithdrawActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.


    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        findViewById(R.id.withdraw_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WithdrawActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_board).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
            startActivity(intent);
        });


    }

}
