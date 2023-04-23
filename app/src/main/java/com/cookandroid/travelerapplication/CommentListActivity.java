package com.cookandroid.travelerapplication;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {

    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        FileHelper fileHelper = new FileHelper(this);
        fileHelper.writeToFile("IP_ADDRESS", "54.180.24.243");
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.RecyclerView_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

        findViewById(R.id.button_refresh).setOnClickListener(v -> {
            Refresh();
        });

        findViewById(R.id.button_add).setOnClickListener(v -> {
            // comment Add task 만들기
        });

    }

    public void Refresh() {
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(commentArrayList);

        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_comment.php");
        try {
            new Handler().postDelayed(() -> {
                adapter = new CommentAdapter(commentArrayList, this);
                recyclerView.setAdapter(adapter);
            }, 2000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}