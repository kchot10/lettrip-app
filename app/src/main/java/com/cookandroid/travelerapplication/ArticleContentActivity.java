package com.cookandroid.travelerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleContentActivity extends AppCompatActivity {

    TextView textview_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        Intent intent = getIntent();

        textview_content = findViewById(R.id.textview_content);

        textview_content.setText(intent.getStringExtra("content"));


    }
}