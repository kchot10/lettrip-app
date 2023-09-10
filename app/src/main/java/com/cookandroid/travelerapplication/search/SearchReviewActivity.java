package com.cookandroid.travelerapplication.search;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.record.Place;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.SelectData_Place;
import com.cookandroid.travelerapplication.task.SelectData_Review;

import java.util.ArrayList;

public class SearchReviewActivity extends AppCompatActivity {
    ArrayList<Review> reviewArrayList;
    String IP_ADDRESS, user_id, place_id = "";
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView titleText;

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
        titleText = findViewById(R.id.titleText);
        Button button_place_search = findViewById(R.id.button_place_search);

        button_place_search.setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinActivity.class);
            getKotlinActivityResult.launch(intent);
        });

        button_place_search.performClick();
        // selectData_review.php
    }

    public void Refresh() {
        reviewArrayList = new ArrayList<>();
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


    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    titleText.setText(result.getData().getStringExtra("name"));
                    String x = result.getData().getStringExtra("location_x");
                    String y = result.getData().getStringExtra("location_y");
                    String location_point = "POINT("+x+" "+y+")";

                    ArrayList<Place> arrayListPlace = new ArrayList<>();
                    SelectData_Place selectData_place = new SelectData_Place(arrayListPlace);
                    selectData_place.execute("http://"+IP_ADDRESS+"/0601/select_location_point.php",location_point);

                    new Handler().postDelayed(() -> {
                        try {
                            place_id = arrayListPlace.get(0).getPlace_id();
                        }catch (Exception e){
                            Log.e("youn", "place_id 불러오기 실패");
                        }
                        if ( !place_id.equals("") ){ // place_id에 아무것도 저장되어있지 않지 않다면 (뭐라도 있다면)
                            Refresh();
                        }else {
                            Toast.makeText(this, "해당 장소에 등록된 리뷰가 없습니다. 리뷰를 등록해보세요!", Toast.LENGTH_SHORT).show();
                        }
                    },500);
                }
            }
    );

}