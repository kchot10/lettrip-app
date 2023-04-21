package com.cookandroid.travelerapplication;

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
        setContentView(R.layout.activity_board_list);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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