package com.cookandroid.travelerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        fileHelper.writeToFile("user_id", "6");

        EditText editText_title = findViewById(R.id.edittext_title);
        EditText edittext_content = findViewById(R.id.edittext_content);

        findViewById(R.id.button_create).setOnClickListener(v -> {
            String title = editText_title.getText().toString().trim();
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