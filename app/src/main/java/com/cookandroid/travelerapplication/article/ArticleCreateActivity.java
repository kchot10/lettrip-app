package com.cookandroid.travelerapplication.article;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.InsertData_Article;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.UpdateData_Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleCreateActivity extends AppCompatActivity {

    private final String BASEID = "-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_create);
        FileHelper fileHelper = new FileHelper(this);
        String IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        Log.d("youn", IP_ADDRESS);
        EditText edittext_title = findViewById(R.id.edittext_title);
        EditText edittext_content = findViewById(R.id.edittext_content);

        Intent intent = getIntent();
        if (intent.getStringExtra("sign") != null){
            findViewById(R.id.button_create).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);
            edittext_title.setText(intent.getStringExtra("title").trim());
            edittext_content.setText(intent.getStringExtra("content").trim());

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
                String hit = "0";
                String like_count = "0";
                String article_type = getIntent().getStringExtra("article_type");

                Log.d("youn", "user_id: "+user_id);
                InsertData_Article task = new InsertData_Article();
                task.execute("http://"+IP_ADDRESS+"/0422/InsertData_Article.php",BASEID,created_date, modified_date,content, hit, like_count, title, user_id, article_type);

                finish();
            }


        });

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent2 = new Intent(this, ArticleListActivity.class);
            startActivity(intent);
        });

    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}