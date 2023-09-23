package com.cookandroid.travelerapplication.planning;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.recommend.PlaceScore;
import com.cookandroid.travelerapplication.recommend.PlaceScoreAdapter;
import com.cookandroid.travelerapplication.record.CourseActivity;
import com.cookandroid.travelerapplication.task.Recommend_Place;

import java.util.ArrayList;

public class PlanningMainActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id, city_name;
    FileHelper fileHelper;
    private RecyclerView recyclerView_recommend;
    private RecyclerView.Adapter recyclerView_adapter_recommend;
    private RecyclerView.LayoutManager layoutManager_recommend;
    EditText edittext_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_main);

        setContentView(R.layout.activity_record_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        edittext_title = findViewById(R.id.edittext_title);

        city_name = "서울"; //Todo: 지금은 city_name인데 기존은 city임. 나중에 바꿔야됨
        //플로팅액션버튼 클릭리스너
//        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
//            View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_input, null);
//            builder.setView(dialogView);
//
//            // 다이얼로그 생성
//            AlertDialog dialog = builder.create();
//
//            // 다이얼로그의 위치 설정 (오른쪽 아래 마진)
//            Window window = dialog.getWindow();
//            window.setGravity(Gravity.BOTTOM | Gravity.END);
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.y = 230; // 아래 마진
//            params.width = Math.round(185 * getResources().getDisplayMetrics().density);
//
//            params.horizontalWeight = 0;
//            window.setAttributes(params);
//
//            // 팝업 창 내부의 뷰들에 접근하여 처리
//            TextView placeAddTextView = dialogView.findViewById(R.id.addPlaceBtn);
//            placeAddTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
//                    intent.putExtra("total_day_count", subtractDates(dateBtn_end.getText().toString(), dateBtn_start.getText().toString())+1);
//                    intent.putExtra("province", spinner.getSelectedItem().toString().trim());
//                    intent.putExtra("city", spinner2.getSelectedItem().toString().trim());
//                    intent.putExtra("depart_date", dateBtn_start.getText().toString().trim());
//                    intent.putExtra("record/plan", getIntent().getStringExtra("record/plan"));
//                    startActivity(intent);
//                }
//            });
//            //리뷰기반추천
//            TextView recommendingTextView = dialogView.findViewById(R.id.addPlaceBtn);
//            recommendingTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
//                    View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_recommending_item, null);
//                    builder.setView(dialogView);
//
//                    recyclerView_recommend = dialogView.findViewById(R.id.recommendRecyclerView);
//                    recyclerView_recommend.setHasFixedSize(true);
//                    layoutManager_recommend = new LinearLayoutManager(dialogView.getContext());
//                    recyclerView_recommend.setLayoutManager(layoutManager_recommend);
//
//                    //추천리스트 불러오기
//                    ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
//                    Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
//                    recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_item/", "item", user_id, city_name, "1");
//                    try {
//                        new Handler().postDelayed(() -> {
//                            recyclerView_adapter_recommend = new PlaceScoreAdapter(arrayListPlaceScore, dialogView.getContext());
//                            recyclerView_recommend.setAdapter(recyclerView_adapter_recommend);
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }, 4000); // 0.5초 지연 시간
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    dialogView.findViewById(R.id.refreshButton).setOnClickListener(v1 -> {
//                        Refresh(dialogView.getContext(), "item");
//
//                    });
//                    //팝업닫기
//                    dialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//
//
//                }
//            });
//
//            //장소기반추천
//            TextView recommendingTextView2 = dialogView.findViewById(R.id.recommendingTextView2);
//            recommendingTextView2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
//                    View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_recommending_place, null);
//                    builder.setView(dialogView);
//
//                    recyclerView_recommend = dialogView.findViewById(R.id.recommendRecyclerView);
//                    recyclerView_recommend.setHasFixedSize(true);
//                    layoutManager_recommend = new LinearLayoutManager(dialogView.getContext());
//                    recyclerView_recommend.setLayoutManager(layoutManager_recommend);
//
//                    ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
//                    Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
//                    recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_place/", "place", user_id, city_name, "1", "서울숲공원");
//                    try {
//                        new Handler().postDelayed(() -> {
//                            recyclerView_adapter_recommend = new PlaceScoreAdapter(arrayListPlaceScore, dialogView.getContext());
//                            recyclerView_recommend.setAdapter(recyclerView_adapter_recommend);
//                            recyclerView_recommend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }, 3000); // 0.5초 지연 시간
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    dialogView.findViewById(R.id.refreshButton).setOnClickListener(v1 -> {
//                        Refresh(dialogView.getContext(), "place");
//
//                    });
//                    //팝업닫기
//                    dialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            });
//
//            dialog.show();
//        });


        //-------------서비스 버튼 클릭 리스너들
//        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        findViewById(R.id.missionBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        findViewById(R.id.boardBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        ImageButton planningAndRecordBtn = findViewById(R.id.planningAndRecordBtn);
//        planningAndRecordBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //팝업 다이얼로그 생성
//                final Dialog dialog = new Dialog(PlanningMainActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_record_and_plan);
//
//                // 다이얼로그의 위치 설정 (오른쪽 아래 마진)
//                Window window = dialog.getWindow();
//                WindowManager.LayoutParams params = window.getAttributes();
//                params.y = 120; // 아래 마진
//                window.setGravity(Gravity.BOTTOM);
//
//                TextView planTextView = dialog.findViewById(R.id.planTextView);
//                planTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getApplicationContext(), PlanningMainActivity.class);
//                        startActivity(intent);
//
//                    }
//                });
//
//                TextView recordTextView = dialog.findViewById(R.id.recordTextView);
//                recordTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getApplicationContext(), RecordMain.class);
//                        startActivity(intent);
//
//                    }
//                });
//
//                dialog.show();
//            }
//        });
//
//        findViewById(R.id.chattingBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //추후 추가
//            }
//        });
//
//        findViewById(R.id.mypageBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }

    private void Refresh_recommend(Context context, String recommend_type){
        ArrayList<PlaceScore> arrayListPlaceScore = new ArrayList<>();
        Recommend_Place recommend_place = new Recommend_Place(arrayListPlaceScore);
        recommend_place.execute("http://"+IP_ADDRESS+":5001/recommend_request_item/", recommend_type, user_id, city_name, "1");
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter_recommend = new PlaceScoreAdapter(arrayListPlaceScore, context);
                recyclerView_recommend.setAdapter(recyclerView_adapter_recommend);
            }, 3000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
