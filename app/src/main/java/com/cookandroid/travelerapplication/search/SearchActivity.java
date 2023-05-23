package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Travel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS;
    String total_cost, province, number_of_courses, travel_theme;
    Spinner spinner_cost, spinner_province, spinner_theme, spinner_number_of_courses;
    FileHelper fileHelper;
    EditText editText_city;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        editText_city = findViewById(R.id.editText_city);
        recyclerView = findViewById(R.id.RecyclerView_Travel);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        spinner_cost = findViewById(R.id.spinner_cost);
        spinner_province = findViewById(R.id.spinner_province);
        spinner_number_of_courses = findViewById(R.id.spinner_number_of_courses);
        spinner_theme = findViewById(R.id.spinner_theme);
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


        findViewById(R.id.button_search).setOnClickListener(v -> {
            Refresh();
        });
    }

    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        travelArrayList = new ArrayList<>();
        SelectData_Travel task = new SelectData_Travel(travelArrayList);
        String city = editText_city.getText().toString().trim();
        province = spinner_province.getSelectedItem().toString().trim();
        number_of_courses = spinner_number_of_courses.getSelectedItem().toString().trim();
        travel_theme = spinner_theme.getSelectedItem().toString().trim();
        Toast.makeText(this, "total_cost"+total_cost+
                "province"+province+
                "number_of_courses"+number_of_courses+
                "travel_theme"+travel_theme, Toast.LENGTH_LONG).show();
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_travel.php", city, province, total_cost, number_of_courses,travel_theme);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new TravelAdapter(travelArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}