package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

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
    String travel_id, city, total_cost, number_of_courses;

    TextView textView_total_cost_search, textView_number_of_courses_search, textView_city_search, textView_theme_search;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main_search);
        textView_total_cost_search =findViewById(R.id.textView_total_cost_search);
        textView_number_of_courses_search =findViewById(R.id.textView_number_of_courses_search);
        textView_city_search =findViewById(R.id.textView_city_search);
        textView_theme_search =findViewById(R.id.textView_theme_search);
        textView_theme_search.setText(getIntent().getStringExtra("travel_theme"));
        textView_number_of_courses_search.setText(getIntent().getStringExtra("number_of_courses"));
        textView_total_cost_search.setText(getIntent().getStringExtra("total_cost"));
        textView_city_search.setText(getIntent().getStringExtra("city"));

        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        recyclerView = findViewById(R.id.recyclerView_course);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        city = getIntent().getStringExtra("city");
        total_cost = getIntent().getStringExtra("total_cost");
        number_of_courses = getIntent().getStringExtra("number_of_courses");
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