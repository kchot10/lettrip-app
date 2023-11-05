package com.cookandroid.travelerapplication.meetup;

import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.mypage.MyTravelActivity;
import com.cookandroid.travelerapplication.record.CourseActivity;
import com.cookandroid.travelerapplication.record.Place;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.SelectData_Place;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MeetupAddPostActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 1;

    private String place_id;
    ImageButton backBtn;
    Spinner gpsSpinner;
    Spinner city1;
    Spinner city2;
    TextView dateTextView;
    EditText context;
    ImageButton addPlaceBtn;
    ImageButton addPlanBtn;
    Button addPostBtn;
    ArrayAdapter<String> city1Adapter; // 어댑터 선언
    String selectedDate;
    private String email; // 현재 로그인한 사용자의 이메일
    String is_gps_enabled;
    String selectedCity1;
    String selectedCity2;
    String userInputContext;
    //ActivityMeetupNewpostBinding binding;
    private String IP_ADDRESS;
    FileHelper fileHelper = new FileHelper(this);

    TextView placeName;
    TextView placeCategory;
    TextView placeAddress;
    TextView planTitleTextView;
    TextView planDate;
    TextView planInfo;
    TextView planCategory;
    LinearLayout placeLayout, planLayout;
    String request_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_newpost);
        //binding = ActivityMeetupNewpostBinding.inflate(getLayoutInflater());

        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        gpsSpinner = findViewById(R.id.gpsSpinner);
        city1 = findViewById(R.id.citySpinner1);
        city2 = findViewById(R.id.citySpinner2);
        dateTextView = findViewById(R.id.dateSelectTextView);
        context = findViewById(R.id.meetupPostContext);
        addPostBtn = findViewById(R.id.addPostBtn);
        addPlaceBtn = findViewById(R.id.addPlaceBtn);
        addPlanBtn = findViewById(R.id.addPlanBtn);

        placeName = findViewById(R.id.placeName);
        placeCategory = findViewById(R.id.placeCategory);
        placeAddress = findViewById(R.id.placeAddress);
        planTitleTextView = findViewById(R.id.planTitleTextView);
        planDate = findViewById(R.id.planDate);
        planInfo = findViewById(R.id.planInfo);
        planCategory = findViewById(R.id.planCategory);
        backBtn = findViewById(R.id.backBtn);
        placeLayout = findViewById(R.id.placeLayout);
        planLayout = findViewById(R.id.PlanLayout);


        //gpsSpinner
        String[] gpsStatus = {"GPS 미사용", "GPS 사용"};
        ArrayAdapter<String> GPSadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus);
        GPSadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSpinner.setAdapter(GPSadapter);

        gpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                switch (selectedStatus) {
                    case "GPS 사용":
                        is_gps_enabled = "1";
                        city1.setEnabled(false);
                        city2.setEnabled(false);

                        // GPS 정보 사용해서 현재 위치 확인
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Location loc_current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double cur_lat = loc_current.getLatitude(); //위도
                        double cur_lon = loc_current.getLongitude(); //경도

                        // 위도와 경도를 이용하여 주소 가져오기
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(cur_lat, cur_lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String fullAddress = address.getAddressLine(0); // 전체 주소

                            // TODO: fullAddress를 사용하여 UI 업데이트 또는 저장 등의 작업 수행

                        } else {
                            // 주소를 가져오지 못한 경우 처리
                        }
                        break;

                    case "GPS 미사용":
                        is_gps_enabled = "0";
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
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않았을 때의 동작 수행
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


        //날짜 선택
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialog 생성
                DatePickerDialog datePickerDialog = new DatePickerDialog(MeetupAddPostActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // 선택된 날짜 처리
                                selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                                dateTextView.setText("📆  " + selectedDate);
                            }
                        }, year, month, day);

                // DatePickerDialog 표시
                datePickerDialog.show();
            }
        });


        //본문 입력 받기
        userInputContext = context.getText().toString();


        //선택 옵션 처리 - 장소 & 계획 추가
        //OptionalBtnClickListener btnClickListener = new OptionalBtnClickListener(getApplicationContext(), placeName, placeCategory, placeAddress);
        addPlaceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), KotlinActivity.class);
            getKotlinActivityResult.launch(intent);

        });
        //addPlaceBtn.setOnClickListener(btnClickListener);

        addPlanBtn.setOnClickListener(v -> {
            String title = "서울 여행";
            String date = "2020.10.10 - 2020.10.20";
            int courseNum = 3;
            String course = "코스 " +  courseNum + "개";
            int moneyNum = 100000;
            String money = "비용 " + formatNumber(moneyNum) + "원";
            String categoryString = "문화여행";
            String category = "#" + categoryString;

            //todo:여행 계획 리스트에서 하나를 선택하면 그 여행에 대한 정보들을 받아와서 변수에 저장하기

            planLayout.setVisibility(VISIBLE);
            planTitleTextView.setText(title);
            planDate.setText(date);
            planCategory.setText(category);
            planInfo.setText(course + " / " + money);

        });

        String user_id = fileHelper.readFromFile("user_id");

        //데이터베이스 저장
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeetupPostData(user_id);
            }
        });

        //뒤로가기 버튼
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupPostMainAcitivty.class);
                startActivity(intent);
            }
        });


    }


//---------------------------------------------------------------------------------

    //비용 천 단위로 쉼표 붙이기
    public static String formatNumber(int number) {
        Locale locale = new Locale("ko", "KR"); // 한국 로케일
        NumberFormat nf = NumberFormat.getNumberInstance(locale);

        return nf.format(number);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 여기에 결과를 처리하는 코드를 작성합니다.
                String name = data.getStringExtra("name");
                String category = data.getStringExtra("category");
                String address = data.getStringExtra("address");

                placeName.setText(name);
                placeCategory.setText(category);
                placeAddress.setText(address);
                placeLayout.setVisibility(VISIBLE);

            }
        }
    }

    public void saveMeetupPostData(String user_id) {

        EditText meetupPostContext = findViewById(R.id.meetupPostContext);
        EditText Title = findViewById(R.id.Title);

        String content = meetupPostContext.getText().toString();// binding.meetupPostContext.getText().toString();
        String meet_up_date = selectedDate;
        String meet_up_post_status = getMeetUpPostStatus();
        String province = getSelectedCity1();
        String city = getSelectedCity2();
        String title = (Title.getText().toString().isEmpty() || Title.getText().toString().equals("")) ? Title.getHint().toString() : Title.getText().toString();
        String travel_id = "";
        String created_date = getCurrentTime();
        String modified_date = getCurrentTime();

        Map<String, Boolean> errors = new HashMap<>();
        if(true) {
            ArrayList<String> request = new ArrayList<>();
            request.add(city);
            request.add(content);
            request.add(is_gps_enabled);
            request.add(meet_up_date);
            request.add(meet_up_post_status);
            request.add(province);
            request.add(title);
            request.add(created_date);
            request.add(modified_date);

            for (String str:request) {
                if(isEmptyOrNullOrNot(str)){
                    errors.put(str, true);
                }
            }
        }
        if(errors.isEmpty()) {
            InsertData_MeetupPost task = new InsertData_MeetupPost();
            task.execute(
                    "http://" + IP_ADDRESS + "/1028/InsertData_MeetupPost.php",
                    city, content, is_gps_enabled, meet_up_date, meet_up_post_status,
                    province, title, place_id, travel_id, user_id, created_date, modified_date
            );
            finish();
        }else{
            Log.e("errors", "saveMeetupPostData 에러 발생");
            for (Map.Entry<String, Boolean> entry : errors.entrySet()) {
                String key = entry.getKey();
                Boolean value = entry.getValue();

                System.out.println("Key: " + key + ", Value: " + value);
            }
        }
    }
    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public String getEmail(){
        return email;
    }

    public String getTitlePost(){
        return "";
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

    public Date getSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = sdf.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getSelectedCity1() {
        return selectedCity1;
    }


    public String getSelectedCity2() {
        return selectedCity2;
    }

    public String getUserInputContext() {
        return userInputContext;
    }

    public String getPlace() {
        return null;
    }

    public String getMeetUpPostStatus() { //추가 수정 필요
        return "UNSCHEDULED";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setSelectedCity1(String selectedCity1) {
        this.selectedCity1 = selectedCity1;
    }

    public void setSelectedCity2(String selectedCity2) {
        this.selectedCity2 = selectedCity2;
    }

    public void setUserInputContext(String userInputContext) {
        this.userInputContext = userInputContext;
    }

    public boolean isEmptyOrNullOrNot(String str) {
        return str == null || str.isEmpty() || str.equals("");
    }
    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String place_name = result.getData().getStringExtra("name");
                    String road = result.getData().getStringExtra("road");
                    String address = result.getData().getStringExtra("address");
                    String x = result.getData().getStringExtra("location_x");
                    String y = result.getData().getStringExtra("location_y");
                    String location_point = "POINT("+x+" "+y+")";
                    String category_code = result.getData().getStringExtra("category_group_code");
                    String category_name = result.getData().getStringExtra("category_group_name");
                    String total_rating = "0";

                    String[] addressParts = findProvinceCity(address);
                    String province = addressParts[0];
                    String city = addressParts[1];

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
//                            Toast.makeText(this, "기존에 저장되어있던 place_id 불러오기 성공! place_id:"+place_id, Toast.LENGTH_SHORT).show();
                        }else {
                            InsertData_Place insertData_place = new InsertData_Place();
                            insertData_place.execute("http://"+IP_ADDRESS+"/0503/InsertData_Place.php",category_code,category_name, city, location_point, place_name, province, total_rating, address);
                            new Handler().postDelayed(() -> {
                                String withdraw_result = insertData_place.getReturn_string();
                                if (withdraw_result.equals("실패")) {
                                    Toast.makeText(this, "장소 추가는 완료되었으나 place_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                } else if (withdraw_result.equals("에러")) {
                                    Toast.makeText(this, "장소 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "장소 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    place_id = withdraw_result;
                                    fileHelper.writeToFile("place_id", withdraw_result);
                                }
                            }, 1000); // 0.5초 지연 시간
                        }

                    },500);

                    placeName.setText("📍 " + place_name);
                    placeCategory.setText(category_name);
                    placeAddress.setText(address);

                    placeLayout.setVisibility(VISIBLE);
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


}
