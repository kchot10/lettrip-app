package com.cookandroid.travelerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_create);
        FileHelper fileHelper = new FileHelper(this);
        String IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        EditText edittext_title = findViewById(R.id.edittext_title);
        EditText edittext_content = findViewById(R.id.edittext_content);

        Intent intent = getIntent();
        if (intent.getStringExtra("sign") != null){
            findViewById(R.id.button_create).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);

            edittext_title.setText(intent.getStringExtra("title").trim());
            edittext_content.setText(intent.getStringExtra("content").trim());
            String modified_date = getCurrentTime();

            // Update 하는 task 만들기
        }

        findViewById(R.id.button_update).setOnClickListener(v -> {
            String title = edittext_title.getText().toString().trim();
            String content = edittext_content.getText().toString().trim();

            if (title.equals("") || content.equals("")){
                Toast.makeText(this, "제목 또는 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String currentTime = getCurrentTime();
                String modified_date = currentTime;
                String article_id = intent.getStringExtra("article_id").trim();

                UpdateData_Article task = new UpdateData_Article();
                task.execute("http://"+IP_ADDRESS+"/0422/UpdateData_Article.php", article_id, modified_date, content, title);

                finish();
            }
        });

        findViewById(R.id.button_create).setOnClickListener(v -> {
            String title = edittext_title.getText().toString().trim();
            String content = edittext_content.getText().toString().trim();

            if (title.equals("") || content.equals("")){
                Toast.makeText(this, "제목 또는 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String currentTime = getCurrentTime();
                String created_date = currentTime;
                String modified_date = currentTime;
                String user_id = fileHelper.readFromFile("user_id");

                InsertData_Article task = new InsertData_Article();
                task.execute("http://"+IP_ADDRESS+"/0422/InsertData_Article.php","0",created_date, modified_date,content, "0", "0", title, user_id);

                finish();
            }


        });



    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}