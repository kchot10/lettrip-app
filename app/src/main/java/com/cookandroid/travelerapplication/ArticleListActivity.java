package com.cookandroid.travelerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity {

    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        FileHelper fileHelper = new FileHelper(this);
        fileHelper.writeToFile("IP_ADDRESS", "54.180.24.243");
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.RecyclerView_board);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

        findViewById(R.id.button_refresh).setOnClickListener(v -> {
            Refresh();
        });

        findViewById(R.id.button_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            startActivity(intent);
        });

    }

    public void Refresh() {
        ArrayList<Article> articleArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(articleArrayList);
        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_article.php");
        try {
            new Handler().postDelayed(() -> {
                adapter = new ArticleAdapter(articleArrayList, this);
                recyclerView.setAdapter(adapter);
            }, 2000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}