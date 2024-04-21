package com.cookandroid.travelerapplication.meetup;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
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
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.chat.ChatListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KakaoAPI3;
import com.cookandroid.travelerapplication.kotlin.Place;
import com.cookandroid.travelerapplication.kotlin.ResultSearchKeyword;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.meetup.model.GpsType;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.record.PlanningMain;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.search.SearchReviewActivity;
import com.cookandroid.travelerapplication.task.SelectData_MeetUpPost;
import com.cookandroid.travelerapplication.task.SelectData_Poke;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MeetupPostMainAcitivty extends AppCompatActivity implements SelectData_MeetUpPost.AsyncTaskCompleteListener, SelectData_UserInfo.AsyncTaskCompleteListener {
    String IP_ADDRESS = "3.34.136.218", user_id; // 여기가 2번 또는 25번
    FileHelper fileHelper;
    ImageButton chatBtn;
    Spinner gpsSelected;
    Spinner city1;
    Spinner city2;
    Button addPost;
    String is_gps;
    String selectedCity1 = "서울특별시";
    String selectedCity2 = "서울";
    String[] parts;
    ArrayAdapter<String> city1Adapter; // 어댑터 선언
    ArrayAdapter<String> adapter2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final int ACCESS_FINE_LOCATION = 1000;
    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static final String API_KEY = "KakaoAK 43a9d1617d8fb89af04db23790b3dd22";
    private double latitude = 1.0;
    private double longitude = 1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_main);
        is_gps = GpsType.GPS_DISABLE.toString();

        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");

//        fileHelper.writeToFile("IP_ADDRESS", IP_ADDRESS);//Todo: 나중에 쓰는 부분은 지울듯
//        fileHelper.writeToFile("user_id", user_id);//Todo: 나중에 쓰는 부분은 지울듯
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(new ArrayList(), this);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", user_id);
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
        String[] gpsStatus = {"GPS 미사용", "GPS 사용"};
        ArrayAdapter<String> adapterGpsSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus); //추후 스피너 레이아웃 커스텀하기
        adapterGpsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSelected.setAdapter(adapterGpsSpinner);

        Refresh(GpsType.GPS_DISABLE.toString(), selectedCity1, selectedCity2);
        gpsSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedGpsStatus = gpsStatus[position];
                switch (selectedGpsStatus) {
                    case "GPS 사용":
                        searchKeyword();
                        is_gps = GpsType.GPS_ENABLE.toString();
                        break;
                    case "GPS 미사용":
                        Refresh(GpsType.GPS_DISABLE.toString(), selectedCity1, selectedCity2);
                        city1.setEnabled(true);
                        city2.setEnabled(true);
                        is_gps = GpsType.GPS_DISABLE.toString();
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
                String selectedCity = (String) parent.getItemAtPosition(position);
                List<String> cityList = getCityList2(selectedCity);
                adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city2.setAdapter(adapter2);
                
                //selectedCity에 해당하는 리스트만 불러오는 코드 추가
                selectedCity1 = selectedCity;
//                Refresh(is_gps, selectedCity1, cityList.get(0)); // 선택된 province의 city 중 제일 첫번째 city
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
                Refresh(is_gps, selectedCity1, selectedCity2);
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
                // 레이아웃 인플레이션
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_record_and_plan, null);

                // AlertDialog 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetupPostMainAcitivty.this);
                builder.setView(dialogView);

                // 다이얼로그 버튼 설정
                Button buttonRecord = dialogView.findViewById(R.id.button_record);
                Button buttonPlan = dialogView.findViewById(R.id.button_plan);

                buttonRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 후기 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(MeetupPostMainAcitivty.this, RecordMain.class);
                        intent.putExtra("record/plan", "record");
                        startActivity(intent);
                    }
                });

                buttonPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 계획 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(MeetupPostMainAcitivty.this, PlanningMain.class);
                        intent.putExtra("record/plan", "plan");
                        startActivity(intent);
                    }
                });

                // AlertDialog 표시
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        ImageButton searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeetupPostMainAcitivty.this, SearchReviewActivity.class);
                startActivity(intent);
            }
        });

        ImageButton logoBtn = findViewById(R.id.logoBtn);
        logoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }




    private void Refresh() {
        SelectData_MeetUpPost selectData_meetUpPost = new SelectData_MeetUpPost(this);
        selectData_meetUpPost.execute("http://" + IP_ADDRESS + "/1028/SelectData_MeetUpPost.php", is_gps, selectedCity1, selectedCity2);
    }

    private void Refresh(String gpsType, String selectedCity, String 서울) {
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


    private void searchKeyword() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location userNowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (userNowLocation != null) {
                latitude = userNowLocation.getLatitude();
                longitude = userNowLocation.getLongitude();
                Log.e("errors", "Successfully retrieved location: " + latitude + ", " + longitude);
            } else {
                Log.e("errors", "Unable to retrieve location information");
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            Log.e("errors", "Request location permission from the user");
        }

        KakaoAPI3 api = retrofit.create(KakaoAPI3.class);
        Call<ResultSearchKeyword> call = api.getSearchKeyword(API_KEY, String.valueOf(longitude), String.valueOf(latitude));

        call.enqueue(new Callback<ResultSearchKeyword>() {
            @Override
            public void onResponse(@NonNull Call<ResultSearchKeyword> call, @NonNull Response<ResultSearchKeyword> response) {
                if (response.body() != null && !response.body().getDocuments().isEmpty()) {
                    for (Place place: response.body().getDocuments()) {
                        city1.setEnabled(false);
                        city2.setEnabled(false);
                        String addressName = place.getAddress_name();
                        parts = findProvinceCity(addressName);
                        Log.e("errors", addressName);
                        Log.e("errors", parts[0]+parts[1]);
                        int position = city1Adapter.getPosition(parts[0]);
                        city1.setSelection(position);
//
                        List<String> cityList = getCityList2(parts[0]);
                        adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        int position2 = adapter2.getPosition(parts[1]);
                        new Handler().postDelayed(()->{
                            city2.setSelection(position2);
                        },300);
                    }
//                    Refresh(GpsType.GPS_ENABLE.toString(), selectedCity1, selectedCity2);
                }else{
                    gpsSelected.setSelection(0);
                    Toast.makeText(getApplicationContext(),"사용자의 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultSearchKeyword> call, @NonNull Throwable t) {
                Log.w("LocalSearch", "Communication failed: " + t.getMessage());
            }
        });
    }
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

    @Override
    public void onTaskComplete_SelectData_UserInfo(UserInfo result) {

        fileHelper.writeToFile("my_nickname", result.getNickname());
        Log.e("errors", result.getNickname()+", user_id:"+fileHelper.readFromFile("user_id"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh(is_gps, selectedCity1, selectedCity2);
    }
}
