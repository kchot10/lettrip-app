package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.record.ImageAdapter;
import com.cookandroid.travelerapplication.record.ImageReview;
import com.cookandroid.travelerapplication.task.SelectData_ImageFile;
import com.cookandroid.travelerapplication.task.UpdateData_Review;

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
        textView_category_search.setText(" "+getIntent().getStringExtra("category_name")+" ");
        textView_place_name_search.setText(getIntent().getStringExtra("place_name"));
        textView_cost_search.setText(getIntent().getStringExtra("cost")+"원");
        textView_arrived_time_search.setText(getIntent().getStringExtra("arrived_time"));
        textView_detailed_review.setText(getIntent().getStringExtra("detailed_review"));
        textView_detailed_review.setEnabled(false);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        String mUser_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.recyclerView_imageFile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        review_id = getIntent().getStringExtra("review_id");
        RatingBar ratingBar = findViewById(R.id.ratingBar);


        if (getIntent().getStringExtra("user_id").equals(mUser_id)){
            if (Float.parseFloat(getIntent().getStringExtra("rating")) == 0){
                ratingBar.setRating(5);
            }else {
                ratingBar.setRating(Float.parseFloat(getIntent().getStringExtra("rating")));
            }
            Button button_add_image = findViewById(R.id.button_add_image);
            ViewGroup.LayoutParams layoutParams = button_add_image.getLayoutParams();
            layoutParams.width = 250;  // 원하는 가로 크기를 픽셀 단위로 지정
            button_add_image.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams layoutParams2 = ratingBar.getLayoutParams();
            layoutParams2.height = 100;
            ratingBar.setLayoutParams(layoutParams2);

            textView_detailed_review.setEnabled(true);
            findViewById(R.id.button_update_course).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.button_add_image).setOnClickListener(v -> {

        });

        findViewById(R.id.button_update_course).setOnClickListener(v -> {
            String detailed_review = textView_detailed_review.getText().toString();
            String rating = Float.toString(ratingBar.getRating());
            UpdateData_Review updateData_review = new UpdateData_Review();
            updateData_review.execute("http://" + IP_ADDRESS + "/0503/updatedata_review.php", review_id, detailed_review, rating);
            finish();
        });

        Refresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
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