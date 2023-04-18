package com.cookandroid.travelerapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "3.34.179.249"; //본인 IP주소를 넣으세요.


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
            Intent intent = new Intent(MainActivity.this, BoardListActivity.class);
            startActivity(intent);
        });


    }

}
