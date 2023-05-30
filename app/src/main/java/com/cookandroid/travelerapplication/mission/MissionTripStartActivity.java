package com.cookandroid.travelerapplication.mission;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.InsertData_Mission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MissionTripStartActivity extends AppCompatActivity {
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    int sum = 0;
    int sec;
    int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip_start);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        ImageButton backBtn = findViewById(R.id.leftArrowBtn);

        findViewById(R.id.missionVerificationBtn).setOnClickListener(v -> {
            sum+=1;
            String accomplished_date = getCurrentTime();
            String mission_type = "TRIP";
            InsertData_Mission insertData_mission = new InsertData_Mission();
            insertData_mission.execute("http://"+IP_ADDRESS+"/0503/InsertData_Mission.php", accomplished_date, mission_type, user_id);

        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                Toast.makeText(getApplicationContext(), "미션Trip 레코드를 추가했습니다. \n현재 추가한 레코드 수:"+sum,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //1km 이내의 장소 리스트 불러오기
        timerRun();

        String keyword="음식점";
        double latitude = 37.5665; // 중심 좌표의 위도
        double longitude = 126.9780; // 중심 좌표의 경도 -> 현재 좌표로 바꾸기
        int radius = 1000; //1000m 내의 위치 검색
        String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword + "&y=" + latitude + "&x=" + longitude + "&radius=" + radius;

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> placeList = new ArrayList<>();

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "KakaoAK 43a9d1617d8fb89af04db23790b3dd2");


                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // JSON 응답을 처리하는 로직 구현
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        // documents 배열에서 장소 정보를 가져옵니다.
                        JSONArray documents = jsonResponse.getJSONArray("documents");

                        for (int i = 0; i < documents.length(); i++) {
                            JSONObject placeObject = documents.getJSONObject(i);
                            String placeName = placeObject.getString("place_name");
                            placeList.add(placeName);
                        }


                    } else {
                        Log.d("kakaoMap", "http요청 실패");
                    }
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!placeList.isEmpty()) {
                            // placeList 리스트에서 랜덤으로 한 장소 선택
                            Random random = new Random();
                            int randomIndex = random.nextInt(placeList.size());
                            String selectedPlace = placeList.get(randomIndex);

                            // 선택된 장소 출력
                            TextView placeName = findViewById(R.id.place_explanation);
                            placeName.setText(selectedPlace);
                        }
                    }
                });
            }
        }).start();


    }

    private void timerRun() {

        //타이머 구현
        TextView min_TextView = findViewById(R.id.min);
        TextView sec_TextView = findViewById(R.id.sec);

        min_TextView.setText("30");
        sec_TextView.setText("0");

        min = Integer.parseInt(min_TextView.getText().toString());
        sec = Integer.parseInt(sec_TextView.getText().toString());

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(sec != 0){
                            sec--;
                            sec_TextView.setText(Integer.toString(sec));
                        } else if(min != 0){
                            sec = 60;
                            sec--;
                            min--;
                            sec_TextView.setText(Integer.toString(sec));
                            min_TextView.setText(Integer.toString(min));
                        }

                        if(min <= 9){
                            min_TextView.setText("0" + Integer.toString(min));
                        } else{
                            min_TextView.setText(Integer.toString(min));
                        }

                        if(sec <= 9){
                            sec_TextView.setText("0" + Integer.toString(sec));
                        } else{
                            sec_TextView.setText(Integer.toString(sec));
                        }

                        if(min == 0 && sec == 0){
                            timer.cancel();
                            showAlertDialog();
                        }

                        if(sec<=30){
                            sec_TextView.setTextColor(Color.parseColor("#E12E23")); //빨간색으로 바꾸기
                            min_TextView.setTextColor(Color.parseColor("#E12E23")); //빨간색으로 바꾸기
                        }
                    }
                });
            }

        };

        timer.schedule(timerTask, 0, 1000); // 타이머 스케줄링 추가
    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }


    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("미션에 실패하셨습니다.");
        builder.setMessage("다시 한 번 도전하시겠습니까?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //미션 액티비티 새로고침
                        finish();
                        overridePendingTransition(0,0);
                        Intent intent = getIntent();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //이전 화면으로 돌아가기
                        Intent intent = new Intent(getApplicationContext(), MissionTripActivity.class);
                        startActivity(intent);
                    }
                });
    }


}
