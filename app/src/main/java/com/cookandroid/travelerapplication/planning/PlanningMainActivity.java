package com.cookandroid.travelerapplication.planning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.Request;
import com.amazonaws.Response;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.recommend.PlaceScore;
import com.cookandroid.travelerapplication.recommend.PlaceScoreAdapter;
import com.cookandroid.travelerapplication.record.CourseActivity;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.search.RecordMainSearch;
import com.cookandroid.travelerapplication.search.SearchActivity;
import com.cookandroid.travelerapplication.task.Recommend_Place;

import java.util.ArrayList;

public class PlanningMainActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id, city_name;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    Spinner spinner;Spinner spinner2;Spinner spinner3;
    EditText edittext_title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_main);
        IP_ADDRESS = "15.164.213.88";
        user_id = "1";
        city_name = "서울";

        spinner = findViewById(R.id.cityBtn1);
        spinner2 = findViewById(R.id.cityBtn2);
        spinner3 = findViewById(R.id.dateBtn);
        edittext_title = findViewById(R.id.planningTitleEditText);

        //플로팅액션버튼 클릭리스너
        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlanningMainActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.popup_planning_input, null);
            builder.setView(dialogView);

            // 다이얼로그 생성
            AlertDialog dialog = builder.create();

            // 다이얼로그의 위치 설정 (오른쪽 아래 마진)
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM | Gravity.END);
            WindowManager.LayoutParams params = window.getAttributes();
            params.y = 230; // 아래 마진
            params.width = dpToPx(185); //적용이 안됨... 이유 모르겠음
            params.horizontalWeight = 0;
            window.setAttributes(params);

            // 팝업 창 내부의 뷰들에 접근하여 처리
            TextView placeAddTextView = dialogView.findViewById(R.id.planTextView);
            placeAddTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
                    startActivity(intent);
                }
            });
            //리뷰기반추천
            TextView recommendingTextView = dialogView.findViewById(R.id.recordTextView);
            recommendingTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    //팝업닫기
                    dialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                }
            });

            //장소기반추천
            TextView recommendingTextView2 = dialogView.findViewById(R.id.recommendingTextView2);
            recommendingTextView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    //팝업닫기
                    dialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });

            dialog.show();
        });


        //-------------서비스 버튼 클릭 리스너들
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.missionBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.boardBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
                startActivity(intent);

            }
        });

        ImageButton planningAndRecordBtn = findViewById(R.id.planningAndRecordBtn);
        planningAndRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //팝업 다이얼로그 생성
                final Dialog dialog = new Dialog(PlanningMainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_record_and_plan);

                // 다이얼로그의 위치 설정 (오른쪽 아래 마진)
                Window window = dialog.getWindow();
                WindowManager.LayoutParams params = window.getAttributes();
                params.y = 120; // 아래 마진
                window.setGravity(Gravity.BOTTOM);

                TextView planTextView = dialog.findViewById(R.id.planTextView);
                planTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), PlanningMainActivity.class);
                        startActivity(intent);

                    }
                });

                TextView recordTextView = dialog.findViewById(R.id.recordTextView);
                recordTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RecordMain.class);
                        startActivity(intent);

                    }
                });

                dialog.show();
            }
        });

        findViewById(R.id.chattingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추후 추가
            }
        });

        findViewById(R.id.mypageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
                startActivity(intent);

            }
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

    // dp 값을 px로 변환하는 함수
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // RecordMain.java
    /*
    private void TravelUpload(){
        String province = spinner.getSelectedItem().toString().trim();
        String city = spinner2.getSelectedItem().toString().trim();
        String date = spinner3.getSelectedItem().toString().trim(); //010101-010101 형식 반환 예상
        String[] dateParts = date.split("-");
        String depart_date = dateParts[0];
        String last_date = dateParts[1];
        String total_cost = "0";
        String title = edittext_title.getText().toString().trim();
        String theme = spinner3.getSelectedItem().toString().trim();

        String url = "http://" + IP_ADDRESS + "/0503/InsertData_Travel.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답을 처리하는 코드
                        if (response.equals("success")) {
                            Toast.makeText(PlanningMainActivity.this, "여행 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            // 여행 ID를 받아온다면, fileHelper를 사용해 travel_id를 저장하는 코드 추가 필요
                        } else {
                            Toast.makeText(PlanningMainActivity.this, "여행 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러 처리 코드
                        Toast.makeText(RecordMain.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("province", province);
                params.put("city", city);
                params.put("depart_date", depart_date);
                params.put("last_date", last_date);
                params.put("total_cost", total_cost);
                params.put("title", title);
                params.put("theme", theme);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } */


}
