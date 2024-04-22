package com.cookandroid.travelerapplication.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.cookandroid.travelerapplication.R;

public class AddressSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_set);

        findViewById(R.id.ipAddressSetButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            EditText IPAddressSetPlainText = findViewById(R.id.IPAddressSetPlainText);
            intent.putExtra("inputIPAddress", IPAddressSetPlainText.getText().toString());
            startActivity(intent);
        });

        findViewById(R.id.notSetButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            EditText IPAddressSetPlainText = findViewById(R.id.IPAddressSetPlainText);
            intent.putExtra("inputIPAddress", "");
            startActivity(intent);
        });

    }
}