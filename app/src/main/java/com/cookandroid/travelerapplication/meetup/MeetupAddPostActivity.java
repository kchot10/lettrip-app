package com.cookandroid.travelerapplication.meetup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;

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
    private String IP_ADDRESS;
    FileHelper fileHelper = new FileHelper(this);

    TextView placeName; TextView placeCategory; TextView placeAddress;
    TextView planTitleTextView; TextView planDate; TextView planInfo; TextView planCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_newpost);

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



        //gpsSpinner
        String[] gpsStatus = {"GPS 미사용", "GPS 사용"};
        ArrayAdapter<String> GPSadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus);
        GPSadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSpinner.setAdapter(GPSadapter);

        gpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                switch (selectedStatus){
                    case "GPS 사용":
                        is_gps_enabled =  "1";
                        break;
                    case "GPS 미사용":
                        is_gps_enabled =  "0";
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
        OptionalBtnClickListener btnClickListener = new OptionalBtnClickListener(getApplicationContext(), placeName, placeCategory, placeAddress);
        addPlaceBtn.setOnClickListener(btnClickListener);
        addPlanBtn.setOnClickListener(btnClickListener);



        String user_id = fileHelper.readFromFile("user_id");

        //데이터베이스 저장
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeetupPostData(user_id);
            }
        });

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
        String place_id = "";
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
//            for (Map.Entry<String, Boolean> entry : errors.entrySet()) {
//                String key = entry.getKey();
//                Boolean value = entry.getValue();
//
//                System.out.println("Key: " + key + ", Value: " + value);
//            }
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
                "경기도", "강원도", "충청북도", "충청남도", "전라북도",
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
            case "강원도":
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

}
