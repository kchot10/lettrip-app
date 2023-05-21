package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

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
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

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