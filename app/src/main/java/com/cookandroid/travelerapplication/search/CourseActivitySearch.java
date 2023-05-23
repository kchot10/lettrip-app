package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.record.ImageAdapter;
import com.cookandroid.travelerapplication.record.ImageReview;
import com.cookandroid.travelerapplication.task.SelectData_ImageFile;

import java.util.ArrayList;

public class CourseActivitySearch extends AppCompatActivity {


    ArrayList<ImageReview> arrayList_image_review;
    String IP_ADDRESS;
    String review_id;
    String travel_id;
    TextView textView_category_search, textView_place_name_search, textView_cost_search, textView_arrived_time_search, textView_detailed_review;

    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);
        textView_category_search = findViewById(R.id.textView_category_search);
        textView_place_name_search = findViewById(R.id.textView_place_name_search);
        textView_cost_search = findViewById(R.id.textView_cost_search);
        textView_arrived_time_search = findViewById(R.id.textView_arrived_time_search);
        textView_detailed_review = findViewById(R.id.textView_detailed_review);
        textView_category_search.setText(getIntent().getStringExtra("category_name"));
        textView_place_name_search.setText(getIntent().getStringExtra("place_name"));
        textView_cost_search.setText(getIntent().getStringExtra("cost")+"원");
        textView_arrived_time_search.setText(getIntent().getStringExtra("arrived_time"));
        textView_detailed_review.setText(getIntent().getStringExtra("detailed_review"));



        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        recyclerView = findViewById(R.id.recyclerView_imageFile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        review_id = getIntent().getStringExtra("review_id");
        Refresh();

    }

    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        arrayList_image_review = new ArrayList<>();
        SelectData_ImageFile task = new SelectData_ImageFile(arrayList_image_review);
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_imagefile.php", review_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new ImageAdapter(arrayList_image_review, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}