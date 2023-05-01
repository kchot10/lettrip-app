package com.cookandroid.travelerapplication.travel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        findViewById(R.id.button_serach_place).setOnClickListener(v -> {
            Intent intent = new Intent(PlaceActivity.this, KotlinActivity.class);
            getKotlinActivityResult.launch(intent);
        });

    }

    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    TextView textView = findViewById(R.id.textView_place);
                    textView.setText(result.getData().getStringExtra("name"));
                }
            }
    );
}