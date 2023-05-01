package com.cookandroid.travelerapplication.travel;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.cookandroid.travelerapplication.MainActivity;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;

public class TravelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        findViewById(R.id.button_add_place).setOnClickListener(v -> {
            Intent intent = new Intent(TravelActivity.this, PlaceActivity.class);
            startActivity(intent);
        });



    }
}