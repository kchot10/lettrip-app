package com.cookandroid.travelerapplication.planning;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.recommend.PlaceScore;
import com.cookandroid.travelerapplication.recommend.PlaceScoreAdapter;
import com.cookandroid.travelerapplication.task.Recommend_Place;

import java.util.ArrayList;

public class PlanningMainActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id, city_name;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_main);
        IP_ADDRESS = "15.164.213.88";
        user_id = "1";
        city_name = "서울";

        //장소기반추천
        findViewById(R.id.floatingActionButton).setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_recommending_place, null);
            builder.setView(dialogView);

            recyclerView = dialogView.findViewById(R.id.recommendRecyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(dialogView.getContext());
            recyclerView.setLayoutManager(layoutManager);

            ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
            Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
            recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_place/", "place", user_id, city_name, "1", "서울숲공원");
            try {
                new Handler().postDelayed(() -> {
                    recyclerView_adapter = new PlaceScoreAdapter(arrayListPlaceScore, dialogView.getContext());
                    recyclerView.setAdapter(recyclerView_adapter);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }, 3000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }
            dialogView.findViewById(R.id.refreshButton).setOnClickListener(v1 -> {
                Refresh(dialogView.getContext(), "place");

            });
            return true;
        });


        //리뷰기반추천
        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_recommending_item, null);
            builder.setView(dialogView);

            recyclerView = dialogView.findViewById(R.id.recommendRecyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(dialogView.getContext());
            recyclerView.setLayoutManager(layoutManager);

            ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
            Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
            recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_item/", "item", user_id, city_name, "1");
            try {
                new Handler().postDelayed(() -> {
                    recyclerView_adapter = new PlaceScoreAdapter(arrayListPlaceScore, dialogView.getContext());
                    recyclerView.setAdapter(recyclerView_adapter);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }, 4000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }
            dialogView.findViewById(R.id.refreshButton).setOnClickListener(v1 -> {
                Refresh(dialogView.getContext(), "item");

            });
        });

    }

    private void Refresh(Context context, String recommend_type){
        ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
        Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
        recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_item/", recommend_type, user_id, city_name, "1");
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new PlaceScoreAdapter(arrayListPlaceScore, context);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 3000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
