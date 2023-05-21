package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.Article;
import com.cookandroid.travelerapplication.article.ArticleAdapter;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.record.Course;
import com.cookandroid.travelerapplication.record.CourseAdapter;
import com.cookandroid.travelerapplication.task.SelectData_Article;
import com.cookandroid.travelerapplication.task.SelectData_Course;

import java.util.ArrayList;

public class RecordMainSearch extends AppCompatActivity {


    ArrayList<Course> courseArrayList;
    String IP_ADDRESS;
    String travel_id;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main_search);

        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        recyclerView = findViewById(R.id.recyclerView_course);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        travel_id = getIntent().getStringExtra("travel_id");
        Refresh();

    }

    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        courseArrayList = new ArrayList<>();
        SelectData_Course task = new SelectData_Course(courseArrayList);
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_course.php", travel_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new CourseAdapter(courseArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();
        Refresh();
    }
}