package com.cookandroid.travelerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleContentActivity extends AppCompatActivity {

    TextView textview_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        Intent intent_comment = getIntent();
        textview_content = findViewById(R.id.textview_content);
        textview_content.setText(intent_comment.getStringExtra("content"));

        FileHelper fileHelper = new FileHelper(this);
        String user_id = fileHelper.readFromFile("user_id").trim();
        if (user_id.equals(intent_comment.getStringExtra("user_id"))) {
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.button_update).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            intent.putExtra("sign", "1");
            startActivity(intent);
        });
    }
}