package com.cookandroid.travelerapplication.article;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.account.LoginActivity;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.SelectData_Article;
import com.cookandroid.travelerapplication.task.SelectData_Article2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArticleListActivity extends AppCompatActivity {


    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private Spinner category_spinner, sorting_spinner;
    private ArrayList<Article> articleArrayList;
    private final String FREE = "FREE_ARTICLE", INFO = "INFO_ARTICLE", QUESTION = "QUESTION_ARTICLE";
    private String article_type;
    private final int NEW = 1, OLD = 2, HIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.RecyclerView_board);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        category_spinner = findViewById(R.id.category_spinner);
        sorting_spinner = findViewById(R.id.sorting_spinner);

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        article_type = FREE;
                        Refresh(NEW);
                        break;
                    case 1:
                        article_type = QUESTION;
                        Refresh(NEW);
                        break;
                    case 2:
                        article_type = INFO;
                        Refresh(NEW);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sorting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Collections.sort(articleArrayList, (a1, a2) -> a2.getCreated_date().compareTo(a1.getCreated_date()));
                        Refresh(NEW);
                        break;
                    case 1:
                        Collections.sort(articleArrayList, (a1, a2) -> a1.getCreated_date().compareTo(a2.getCreated_date()));
                        Refresh(OLD);
                        break;
                    case 2:
                        Collections.sort(articleArrayList, (a1, a2) -> Integer.compare(Integer.parseInt(a2.getHit()), Integer.parseInt(a1.getHit())));
                        Refresh(HIT);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Refresh(NEW);


        findViewById(R.id.button_refresh).setOnClickListener(v -> {
            Refresh(NEW);
        });

        findViewById(R.id.button_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            intent.putExtra("article_type", article_type);
            startActivity(intent);
        });

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    public void Refresh(int TYPE) {
        if (TYPE == NEW) {
            articleArrayList = new ArrayList<>();
            SelectData_Article2 task = new SelectData_Article2(articleArrayList);
            task.execute("http://" + IP_ADDRESS + "/0422/selectdata_article2.php", article_type);
        }

        try {
            new Handler().postDelayed(() -> {
                adapter = new ArticleAdapter(articleArrayList, this);
                recyclerView.setAdapter(adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh(NEW);
    }
}