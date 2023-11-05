package com.cookandroid.travelerapplication.mission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity2;
import com.cookandroid.travelerapplication.record.Place;
import com.cookandroid.travelerapplication.task.InsertData_Mission;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.SelectData_Place;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MissionTripActivity extends AppCompatActivity {

    String IP_ADDRESS, user_id, place_id;
    FileHelper fileHelper;
    String[] mProvince = {"서울", "광주", "대구", "대전", "부산", "울산", "인천"};
    String[] mProvince2 = {"제주특별자치도, 세종특별자치시"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_trip);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        Button missionStartBtn = findViewById(R.id.missionStartBtn);
        ImageButton backBtn = findViewById(R.id.leftArrowBtn);

        missionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KotlinActivity2.class);
                getKotlinActivityResult.launch(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
                startActivity(intent);
            }
        });
    }
    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    String successOrFail = result.getData().getStringExtra("success/fail");
                    if (successOrFail.equals("success")){
                        String place_name = result.getData().getStringExtra("name");
                        String road = result.getData().getStringExtra("road");
                        String address = result.getData().getStringExtra("address");
                        String x = result.getData().getStringExtra("location_x");
                        String y = result.getData().getStringExtra("location_y");
                        String location_point = "POINT("+x+" "+y+")";
                        String category_code = result.getData().getStringExtra("category_group_code");
                        String category_name = result.getData().getStringExtra("category_group_name");
                        String total_rating = "0";
                        ArrayList<Place> arrayListPlace = new ArrayList<>();
                        SelectData_Place selectData_place = new SelectData_Place(arrayListPlace);
                        selectData_place.execute("http://"+IP_ADDRESS+"/0601/select_location_point.php",location_point);
                        new Handler().postDelayed(() -> {
                            place_id = "";
                            try {
                                place_id = arrayListPlace.get(0).getPlace_id();
                            }catch (Exception e){
                                Log.e("youn", "place_id 불러오기 실패");
                            }
                            if ( !place_id.equals("") ){ // place_id에 아무것도 저장되어있지 않지 않다면
                                fileHelper.writeToFile("place_id", place_id);
                                Toast.makeText(this, "기존에 저장되어있던 place_id 불러오기 성공! place_id:"+place_id, Toast.LENGTH_SHORT).show();
                            }else {
                                String provinceCity[] = findProvinceCity(address);
                                String province = provinceCity[0];
                                String city = provinceCity[1];
                                Toast.makeText(this, "province: "+province+"\ncity:"+city,Toast.LENGTH_SHORT).show();
                                InsertData_Place insertData_place = new InsertData_Place();
                                insertData_place.execute("http://"+IP_ADDRESS+"/0503/InsertData_Place.php",category_code,category_name, city, location_point, place_name, province, total_rating);
                                new Handler().postDelayed(() -> {
                                    String withdraw_result = insertData_place.getReturn_string();
                                    if (withdraw_result.equals("실패")) {
                                        Toast.makeText(this, "장소 추가는 완료되었으나 place_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                    } else if (withdraw_result.equals("에러")) {
                                        Toast.makeText(this, "장소 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "장소 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        place_id = withdraw_result;
                                    }
                                }, 1000); // 0.5초 지연 시간
                            }

                        },500);

                        Toast.makeText(this, "미션 성공을 축하드립니다!",Toast.LENGTH_SHORT).show();
                        String accomplished_date = getCurrentTime();
                        String mission_type = "RANDOM_MISSION";
                        InsertData_Mission insertData_mission = new InsertData_Mission();
                        insertData_mission.execute("http://"+IP_ADDRESS+"/0503/InsertData_Mission.php", accomplished_date, mission_type, user_id, place_id);
                    } else if (successOrFail.equals("fail")) {
                        Toast.makeText(this, "다음 도전을 기대합니다!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private String[] findProvinceCity(String address) {

        String[] mProvince = {"서울", "광주", "대구", "대전", "부산", "울산", "인천"};
        String[] mProvince2 = {"제주특별자치도, 세종특별자치시"};

        String[] stringsAddress = address.split(" ");
        if (Arrays.asList(mProvince).contains(stringsAddress[0])){
            stringsAddress[1] = stringsAddress[0];
            if (stringsAddress[0].equals("서울")){
                stringsAddress[0] = "서울특별시";
            }else {
                stringsAddress[0] += "광역시";
            }
        }else if (stringsAddress[0].equals("세종특별자치시")){
            stringsAddress[1] = stringsAddress[0].substring(0, 2);
        }else{
            switch (stringsAddress[0]) {
                case "강원":
                    stringsAddress[0] = "강원특별자치도";
                    break;
                case "경기":
                    stringsAddress[0] = "경기도";
                    break;
                case "경남":
                    stringsAddress[0] = "경상남도";
                    break;
                case "경북":
                    stringsAddress[0] = "경상북도";
                    break;
                case "전남":
                    stringsAddress[0] = "전라남도";
                    break;
                case "전북":
                    stringsAddress[0] = "전라북도";
                    break;
                case "충남":
                    stringsAddress[0] = "충청남도";
                    break;
                case "충북":
                    stringsAddress[0] = "충청북도";
                    break;
            }
        }
        return stringsAddress;
    }


    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }


}
