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

        Intent intent_article = getIntent();
        textview_content = findViewById(R.id.textview_content);
        textview_content.setText(intent_article.getStringExtra("content"));

        FileHelper fileHelper = new FileHelper(this);
        String user_id = fileHelper.readFromFile("user_id").trim();
        if (user_id.equals(intent_article.getStringExtra("user_id"))) {
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.button_update).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            intent.putExtra("sign", "1");
            startActivity(intent);
        });

        findViewById(R.id.button_comment).setOnClickListener(v -> {
            Intent intent = new Intent(this, CommentListActivity.class);
            intent.putExtra("article_id", intent_article.getStringExtra("article_id"));
            startActivity(intent);
        });
    }
}