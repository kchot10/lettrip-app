package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Travel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Travel> travelArrayList;
    String IP_ADDRESS;
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

        findViewById(R.id.button_search).setOnClickListener(v -> {
            Refresh();
        });
    }

    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        travelArrayList = new ArrayList<>();
        SelectData_Travel task = new SelectData_Travel(travelArrayList);
        String city = editText_city.getText().toString().trim();
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_travel.php", city);
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