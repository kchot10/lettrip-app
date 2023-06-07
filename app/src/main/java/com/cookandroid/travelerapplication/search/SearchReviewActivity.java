package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Review;

import java.util.ArrayList;

public class SearchReviewActivity extends AppCompatActivity {
    ArrayList<Review> reviewArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_review);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.search_review_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

        // selectData_review.php
    }

    public void Refresh() {
        reviewArrayList = new ArrayList<>();
        String place_id = "2";
        SelectData_Review task = new SelectData_Review(reviewArrayList);
        task.execute("http://" + IP_ADDRESS + "/0601/selectData_review.php", place_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new ReviewAdapter(reviewArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}