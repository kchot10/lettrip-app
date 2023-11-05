package com.cookandroid.travelerapplication.meetup;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.chat.ChatListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.model.GpsType;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.task.SelectData_MeetUpPost;
import com.cookandroid.travelerapplication.task.SelectData_Poke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MeetupPostMainAcitivty extends AppCompatActivity implements SelectData_MeetUpPost.AsyncTaskCompleteListener {
    String IP_ADDRESS = "3.34.136.218", user_id="25";
    FileHelper fileHelper;
    ImageButton chatBtn;
    Spinner gpsSelected;
    Spinner city1;
    Spinner city2;
    Button addPost;
    String is_gps_enabled = "all";
    String selectedCity1;
    String selectedCity2;
    ArrayAdapter<String> city1Adapter; // 어댑터 선언
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_main);

        fileHelper = new FileHelper(this);
        fileHelper.writeToFile("IP_ADDRESS", IP_ADDRESS);//Todo: 나중에 쓰는 부분은 지울듯
        fileHelper.writeToFile("user_id", user_id);//Todo: 나중에 쓰는 부분은 지울듯
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.RecyclerView_MeetUpPost);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        chatBtn = findViewById(R.id.chatBtn);
        gpsSelected = findViewById(R.id.GpsSpinner);
        city1 = findViewById(R.id.citySpinner);
        city2 = findViewById(R.id.citySpinner2);
        addPost = findViewById(R.id.writeBtn);

        //gps 스피너
        String[] gpsStatus = {"GPS 정보 사용", "GPS 정보 미사용"};
        ArrayAdapter<String> adapterGpsSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus); //추후 스피너 레이아웃 커스텀하기
        adapterGpsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSelected.setAdapter(adapterGpsSpinner);

        gpsSelected.setSelection(1);

        Refresh(GpsType.GPS_DISABLE.toString());
        gpsSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedGpsStatus = gpsStatus[position];
                switch (selectedGpsStatus) {
                    case "GPS 정보 사용":
                        Refresh(GpsType.GPS_ENABLE.toString());
                        city1.setEnabled(false);
                        city2.setEnabled(false);

                        //gps 정보 사용해서 지역 알아내기

                        break;
                    case "GPS 정보 미사용":
                        Refresh(GpsType.GPS_DISABLE.toString());
                        city1.setEnabled(true);
                        city2.setEnabled(true);
                        break;
                    // 다른 GPS 상태에 대한 case 문 추가
                    default:
                        // 기본적으로 처리할 내용 (예: 아무 동작 안 함)
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //citySpinner
        city1Adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, getCityList1());
        city1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city1.setAdapter(city1Adapter);

        city1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity1 = (String) parent.getItemAtPosition(position); // city1의 선택된 값 저장

                String selectedCity = (String) parent.getItemAtPosition(position);
                List<String> cityList = getCityList2(selectedCity);
                //if (locality != null) {
                    //cityList.add(locality);
                //}

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city2.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않았을 때의 동작 수행
            }
        });

        city2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity2 = (String) parent.getItemAtPosition(position); // city2의 선택된 값 저장
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않았을 때의 동작 수행
            }
        });

        //글쓰기 수행
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupAddPostActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.addItemDecoration(new RecyclerViewDecoration(30));

        //채팅 목록 보기
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mypage = findViewById(R.id.mypageBtn);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mission = findViewById(R.id.missionBtn);
        mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MissionMainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton board = findViewById(R.id.boardBtn);
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleListActivity.class);
            }
        });

        ImageButton record = findViewById(R.id.recordBtn);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecordMain.class);
                //TODO:POPUP 띄워서 후기 기록과 계획 기록으로 나눠서 들어가도록 설정
            }
        });

    }





    private void Refresh(String gpsType) {
        SelectData_MeetUpPost selectData_meetUpPost = new SelectData_MeetUpPost(this);
        selectData_meetUpPost.execute("http://" + IP_ADDRESS + "/1028/SelectData_MeetUpPost.php", gpsType, selectedCity1, selectedCity2);
    }

    @Override
    public void onTaskComplete(ArrayList<MeetupPost> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result.size() == 0){
                    Toast.makeText(getApplicationContext(),"관련 친구매칭 글이 없습니다",Toast.LENGTH_SHORT).show();
                }
                adapter = new MeetupPostAdapter(result, getApplicationContext());
                recyclerView.setAdapter(adapter);

            }
        });
    }

    private List<String> getCityList1() {
        return Arrays.asList(
                "서울특별시", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "울산광역시", "인천광역시",
                "경기도", "강원특별자치도", "충청북도", "충청남도", "전라북도",
                "전라남도", "경상북도", "경상남도", "제주특별자치도", "세종특별자치시"
        );
    }

    private List<String> getCityList2(String selectedCity) {
        switch (selectedCity) {
            case "서울특별시":
                return City.SEOUL;
            case "부산광역시":
                return City.BUSAN;
            case "대구광역시":
                return City.DAEGU;
            case "인천광역시":
                return City.INCHEON;
            case "광주광역시":
                return City.GWANGJU;
            case "대전광역시":
                return City.DAEJEON;
            case "울산광역시":
                return City.ULSAN;
            case "세종특별자치시":
                return City.SEJONG;
            case "경기도":
                return City.GYEONGGI_CITY;
            case "강원특별자치도":
                return City.GANGWON_CITY;
            case "충청북도":
                return City.CHUNGCHEONG_BUKDO_CITY;
            case "충청남도":
                return City.CHUNGCHEONG_NAMDO_CITY;
            case "전라북도":
                return City.JEOLLA_BUKDO_CITY;
            case "전라남도":
                return City.JEOLLA_NAMDO_CITY;
            case "경상북도":
                return City.GYEONGSANG_BUKDO_CITY;
            case "경상남도":
                return City.GYEONGSANG_NAMDO_CITY;
            case "제주특별자치도":
                return City.JEJU_CITY;
            default:
                return Collections.emptyList();
        }
    }

    public String getSelectedCity1() {
        return selectedCity1;
    }


    public String getSelectedCity2() {
        return selectedCity2;
    }
}
