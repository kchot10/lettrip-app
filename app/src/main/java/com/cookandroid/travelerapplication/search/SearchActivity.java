package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_Travel_Mypage;

import java.sql.Ref;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS, user_id;
    FileHelper fileHelper;
    String total_cost ="", province, number_of_courses, travel_theme;
    Spinner spinner_cost, spinner_province, spinner_city, spinner_theme, spinner_number_of_courses;
    EditText editText_city;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        editText_city = findViewById(R.id.editText_city);
        recyclerView = findViewById(R.id.RecyclerView_Travel);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        spinner_cost = findViewById(R.id.spinner_cost);
        spinner_province = findViewById(R.id.spinner_province);
        spinner_city = findViewById(R.id.spinner_city);
        spinner_number_of_courses = findViewById(R.id.spinner_number_of_courses);
        spinner_theme = findViewById(R.id.spinner_theme);

        if (getIntent().getStringExtra("search/mypage").equals("mypage")){
            LinearLayout layout  = findViewById(R.id.linearLayout_category);
            layout.getLayoutParams().height = 0;
            layout.requestLayout();
            Refresh();
        }
        spinner_cost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        total_cost = "100000";
                        break;
                    case 2:
                        total_cost = "200000";
                        break;
                    case 3:
                        total_cost = "300000";
                        break;
                    case 4:
                        total_cost = "400000";
                        break;
                    case 5:
                        total_cost = "500000";
                        break;
                    case 6:
                        total_cost = "600000";
                        break;
                    case 7:
                        total_cost = "700000";
                        break;
                    case 8:
                        total_cost = "800000";
                        break;
                    case 9:
                        total_cost = "900000";
                        break;
                    case 10:
                        total_cost = "1000000";
                        break;
                    case 11:
                        total_cost = "0";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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


        findViewById(R.id.button_search).setOnClickListener(v -> {
            Refresh();
        });
    }

    private void setCitySpinnerAdapterItem(int array_resource) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, (String[]) getResources().getStringArray(array_resource));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter);
    }


    public void Refresh() {
        if (getIntent().getStringExtra("search/mypage").equals("mypage")){
            travelArrayList = new ArrayList<>();
            SelectData_Travel_Mypage task = new SelectData_Travel_Mypage(travelArrayList);
            task.execute("http://" + IP_ADDRESS + "/0503/selectdata_travel_mypage.php", user_id);
            try {
                new Handler().postDelayed(() -> {
                    recyclerView_adapter = new TravelAdapter(travelArrayList, this);
                    recyclerView.setAdapter(recyclerView_adapter);
                }, 1000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }else{
            // Record class, SelectData_Record task, RecordAdapter
            travelArrayList = new ArrayList<>();
            String city = spinner_city.getSelectedItem().toString().trim();
            province = spinner_province.getSelectedItem().toString().trim();
            number_of_courses = spinner_number_of_courses.getSelectedItem().toString().trim();
            travel_theme = spinner_theme.getSelectedItem().toString().trim();
            if (number_of_courses.equals("코스 수")){
                number_of_courses = "";
            }
            if (travel_theme.equals("테마 선택")){
                travel_theme = "";
            }
            SelectData_Travel task = new SelectData_Travel(travelArrayList);
            task.execute("http://" + IP_ADDRESS + "/0503/selectdata_travel.php", city, province, total_cost, number_of_courses,travel_theme);
            try {
                new Handler().postDelayed(() -> {
                    if (travelArrayList.size() == 0){
                        Toast.makeText(this, "검색하신 결과가 없습니다!", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView_adapter = new TravelAdapter(travelArrayList, this);
                    recyclerView.setAdapter(recyclerView_adapter);
                }, 1000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }
            total_cost = "";
        }
    }
}