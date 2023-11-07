package com.cookandroid.travelerapplication.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.account.LoginActivity;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.chat.ChatListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.meetup.MeetupPostMainAcitivty;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.search.SearchReviewActivity;
import com.cookandroid.travelerapplication.task.InsertData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_Course;
import com.cookandroid.travelerapplication.task.UpdateData_Travel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordMain extends AppCompatActivity{

    String IP_ADDRESS, user_id, travel_id;
    private EditText edittext_title;
    ArrayList<Course> courseArrayList;
    int total_cost = 0, number_of_courses = 0;

    Button dateBtn_start, dateBtn_end;
    Spinner spinner;
    Spinner spinner2;

    Spinner spinner3;
    FileHelper fileHelper;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");

        edittext_title = findViewById(R.id.edittext_title);
        dateBtn_start = findViewById(R.id.dateBtn_start);
        dateBtn_end = findViewById(R.id.dateBtn_end);
        recyclerView = findViewById(R.id.RecyclerView_Record);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        travel_id = "0";
        courseArrayList = new ArrayList<>();





        //도시 선택
        adapter = ArrayAdapter.createFromResource(this, R.array.my_array_state, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.cityDropdown);
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.city_array_default, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2 = findViewById(R.id.cityDropdown_detail);
        spinner2.setAdapter(adapter2);
        spinner3 = findViewById(R.id.themeDropdown);

        TextView recordTitle = findViewById(R.id.recordTitle);
        if (getIntent().getStringExtra("record/plan").equals("plan")) {
            recordTitle.setText("코스 계획하기");
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position) {
                    case 0:
                        setCitySpinnerAdapterItem(R.array.city_array_default);
                        break;
                    case 1:
                        setCitySpinnerAdapterItem(R.array.서울특별시);
                        break;
                    case 2:
                        setCitySpinnerAdapterItem(R.array.광주광역시);
                        break;
                    case 3:
                        setCitySpinnerAdapterItem(R.array.대구광역시);
                        break;
                    case 4:
                        setCitySpinnerAdapterItem(R.array.대전광역시);
                        break;
                    case 5:
                        setCitySpinnerAdapterItem(R.array.부산광역시);
                        break;
                    case 6:
                        setCitySpinnerAdapterItem(R.array.울산광역시);
                        break;
                    case 7:
                        setCitySpinnerAdapterItem(R.array.인천광역시);
                        break;
                    case 8:
                        setCitySpinnerAdapterItem(R.array.강원특별자치도);
                        break;
                    case 9:
                        setCitySpinnerAdapterItem(R.array.경기도);
                        break;
                    case 10:
                        setCitySpinnerAdapterItem(R.array.경상북도);
                        break;
                    case 11:
                        setCitySpinnerAdapterItem(R.array.경상남도);
                        break;
                    case 12:
                        setCitySpinnerAdapterItem(R.array.전라북도);
                        break;
                    case 13:
                        setCitySpinnerAdapterItem(R.array.전라남도);
                        break;
                    case 14:
                        setCitySpinnerAdapterItem(R.array.충청북도);
                        break;
                    case 15:
                        setCitySpinnerAdapterItem(R.array.충청남도);
                        break;
                    case 16:
                        setCitySpinnerAdapterItem(R.array.제주특별자치도);
                        break;
                    case 17:
                        setCitySpinnerAdapterItem(R.array.세종특별자치시);
                        break;
                    // 다른 case문들을 추가하여 필요한 도시 목록을 처리합니다.
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //날짜 지정
        dateBtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RecordMain.this);
                dialog.setContentView(R.layout.activity_record_date);
                TextView titlebar = dialog.findViewById(R.id.titlebar);
                Button close = dialog.findViewById(R.id.cancelBtn);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                CalendarView calendarView = dialog.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        String dayOfWeekString = getDayOfWeekString(year, month, dayOfMonth);
                        String titleDate = year+"년 "+(month+1)+"월 "+dayOfMonth+"일 "+ dayOfWeekString;
                        titlebar.setText(titleDate);
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        dateBtn_start.setText(selectedDate);
                        //날짜 db 저장하기
                    }
                });


                Button ok = dialog.findViewById(R.id.okBtn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCurrentTime();
                        //받아온 데이터를 db에 저장
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        dateBtn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RecordMain.this);
                dialog.setContentView(R.layout.activity_record_date);
                TextView titlebar = dialog.findViewById(R.id.titlebar);

                Button close = dialog.findViewById(R.id.cancelBtn);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                CalendarView calendarView = dialog.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        String dayOfWeekString = getDayOfWeekString(year, month, dayOfMonth);
                        String titleDate = year+"년 "+(month+1)+"월 "+dayOfMonth+"일 "+ dayOfWeekString;
                        titlebar.setText(titleDate);
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                        dateBtn_end.setText(selectedDate);
                    }
                });


                Button ok = dialog.findViewById(R.id.okBtn);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //받아온 데이터를 db에 저장
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        Button button_travel_upload = findViewById(R.id.button_travel_upload);
        button_travel_upload.setOnClickListener(v -> {

            if (dateBtn_start.getText().toString().trim().equals("") || dateBtn_end.getText().toString().trim().equals("")){
                Toast.makeText(this,"시작 날짜 또는 마지막 날짜를 입력하세요",Toast.LENGTH_SHORT).show();
            } else if (edittext_title.getText().toString().trim().equals("")) {
                Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (spinner.getSelectedItem().toString().trim().equals("도 선택") || spinner.getSelectedItem().toString().trim().equals("시 선택")) {
                Toast.makeText(this,"도/시를 입력하세요",Toast.LENGTH_SHORT).show();
            }else if (spinner3.getSelectedItem().toString().trim().equals("테마 선택")) {
                Toast.makeText(this,"여행 테마를 선택하세요",Toast.LENGTH_SHORT).show();
            } else if (subtractDates(dateBtn_start.getText().toString(), dateBtn_end.getText().toString()) > 0) {
                Toast.makeText(this,"마지막 날짜가 시작 날짜보다 앞에 있습니다. 날짜를 다시 입력하세요",Toast.LENGTH_SHORT).show();
                dateBtn_start.setText("");
                dateBtn_end.setText("");
            } else if (subtractDates(getCurrentTime(), dateBtn_end.getText().toString()) < 0 && getIntent().getStringExtra("record/plan").equals("record")) {
                Toast.makeText(this,"오늘 이후의 후기 기록은 할 수 없습니다. 날짜를 다시 입력하세요",Toast.LENGTH_SHORT).show();
                dateBtn_end.setText("");
            } else if (subtractDates(getCurrentTime(), dateBtn_start.getText().toString()) > 0 && getIntent().getStringExtra("record/plan").equals("plan")) {
                Toast.makeText(this,"오늘 이전의 계획을 할 수 없습니다. 날짜를 다시 입력하세요",Toast.LENGTH_SHORT).show();
                dateBtn_start.setText("");
            } else {
                findViewById(R.id.addPlaceBtn).setVisibility(View.VISIBLE);
                button_travel_upload.setVisibility(View.INVISIBLE);
                edittext_title.setEnabled(false);
                spinner.setEnabled(false);
                spinner2.setEnabled(false);
                dateBtn_end.setEnabled(false);
                dateBtn_start.setEnabled(false);
                button_travel_upload.setVisibility(View.INVISIBLE);
                findViewById(R.id.button_travel_update).setVisibility(View.VISIBLE);
                TravelUpload();
            }
        });

        findViewById(R.id.button_travel_update).setOnClickListener(v -> {
            showConfirmationDialog();
        });

        findViewById(R.id.addPlaceBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseActivity.class);
            intent.putExtra("total_day_count", subtractDates(dateBtn_end.getText().toString(), dateBtn_start.getText().toString())+1);
            intent.putExtra("province", spinner.getSelectedItem().toString().trim());
            intent.putExtra("city", spinner2.getSelectedItem().toString().trim());
            intent.putExtra("depart_date", dateBtn_start.getText().toString().trim());
            intent.putExtra("record/plan", getIntent().getStringExtra("record/plan"));
            startActivity(intent);

        });

        ImageButton chatBtn = findViewById(R.id.chatBtn);

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
                startActivity(intent);
            }
        });

        ImageButton meetupBtn = findViewById(R.id.meetupBtn);
        meetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupPostMainAcitivty.class);
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

        ImageButton record = findViewById(R.id.recordBtn);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 레이아웃 인플레이션
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_record_and_plan, null);

                // AlertDialog 생성
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RecordMain.this);
                builder.setView(dialogView);

                // 다이얼로그 버튼 설정
                Button buttonRecord = dialogView.findViewById(R.id.button_record);
                Button buttonPlan = dialogView.findViewById(R.id.button_plan);

                buttonRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 후기 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(RecordMain.this, RecordMain.class);
                        intent.putExtra("record/plan", "record");
                        startActivity(intent);
                    }
                });

                buttonPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 여행 계획 기록하기 버튼 클릭 시 동작
                        Intent intent = new Intent(RecordMain.this, PlanningMain.class);
                        intent.putExtra("record/plan", "plan");
                        startActivity(intent);
                    }
                });

                // AlertDialog 표시
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        ImageButton searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordMain.this, SearchReviewActivity.class);
                startActivity(intent);
            }
        });

    }


    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        courseArrayList = new ArrayList<>();
        SelectData_Course task = new SelectData_Course(courseArrayList);
        travel_id = fileHelper.readFromFile("travel_id");
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_course.php", travel_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new CourseAdapter(courseArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("최종 등록 하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String main_image_url;
                try {
                    main_image_url = courseArrayList.get(0).getStored_file_url();
                }catch (Exception e){
                    main_image_url = "-1";
                }

                // 예 버튼 클릭 시 동작
                dialog.dismiss();
                total_cost = 0; number_of_courses = 0;
                for (int i = 0; i < courseArrayList.size(); i++){
                    total_cost += Integer.parseInt(courseArrayList.get(i).getCost());
                    number_of_courses += 1;
                }
                travel_id = fileHelper.readFromFile("travel_id");
                UpdateData_Travel updateData_travel = new UpdateData_Travel();
                updateData_travel.execute("http://"+IP_ADDRESS+"/0503/updatedata_travel.php", travel_id, Integer.toString(number_of_courses) , Integer.toString(total_cost), main_image_url);
                finish();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아니오 버튼 클릭 시 동작
                dialog.dismiss();
                // 여기에 원하는 동작을 추가하세요.
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Refresh();
    }

    private void setCitySpinnerAdapterItem(int array_resource) {
        adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, (String[])getResources().getStringArray(array_resource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    private void TravelUpload(){
        String province = spinner.getSelectedItem().toString().trim();
        String city = spinner2.getSelectedItem().toString().trim();
        String number_of_courses = "0";
        String created_date = getCurrentTime();
        String visited = "0";
        if (getIntent().getStringExtra("record/plan").equals("record")){
            visited = "1";
        }
        String depart_date = dateBtn_start.getText().toString().trim();
        String last_date = dateBtn_end.getText().toString().trim();
        String total_cost = "0";
        String title = edittext_title.getText().toString().trim();
        String theme = spinner3.getSelectedItem().toString().trim();
        InsertData_Travel insertData_travel = new InsertData_Travel();
        insertData_travel.execute("http://"+IP_ADDRESS+"/0503/InsertData_Travel.php",
                user_id,created_date,visited, depart_date,last_date, total_cost, province, city, number_of_courses, title, theme);

        new Handler().postDelayed(() -> {
            String withdraw_result = insertData_travel.getReturn_string();
            if (withdraw_result.equals("실패")) {
                Toast.makeText(this, "여행 추가는 완료되었으나 travel_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else if (withdraw_result.equals("에러")) {
                Toast.makeText(this, "여행 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "여행 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                fileHelper.writeToFile("travel_id", withdraw_result);
            }
        }, 500); // 0.5초 지연 시간
    }

    public int subtractDates(String dateString1, String dateString2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(dateString1);
            Date date2 = format.parse(dateString2);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);

            long differenceInMillis = cal1.getTimeInMillis() - cal2.getTimeInMillis();
            long differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000);

            return (int) differenceInDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getDayOfWeekString(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        // 요일을 얻기 위해 Calendar 객체에서 해당 필드값을 가져옴
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekString = "일요일";
                break;
            case Calendar.MONDAY:
                dayOfWeekString = "월요일";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "화요일";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "수요일";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "목요일";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "금요일";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "토요일";
                break;
            default:
                dayOfWeekString = "";
        }
        return dayOfWeekString;
    }

}
