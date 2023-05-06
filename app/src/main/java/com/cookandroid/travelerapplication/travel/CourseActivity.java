package com.cookandroid.travelerapplication.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.InsertData_Course;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseActivity extends AppCompatActivity {

    String IP_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        findViewById(R.id.button_add_cource).setOnClickListener(v -> {
            EditText editText_arrived_time_hour = findViewById(R.id.editText_arrived_time_hour);
            EditText editText_arrived_time_min = findViewById(R.id.editText_arrived_time_min);
            String hour = editText_arrived_time_hour.getText().toString().trim();
            String min = editText_arrived_time_min.getText().toString().trim();
            EditText editText_cost = findViewById(R.id.editText_cost);
            EditText editText_day_count = findViewById(R.id.editText_day_count);

            String arrived_time = getCurrentTime_custom(hour, min);
            String cost = editText_cost.getText().toString().trim();
            String day_count = editText_day_count.getText().toString().trim();
            String place_id = "1";
            String review_review_id = "1";
            String travel_id = "1";

            InsertData_Course insertData_course = new InsertData_Course();
            insertData_course.execute("http://"+IP_ADDRESS+"/0503/InsertData_Course.php",arrived_time,cost,day_count, place_id,review_review_id, travel_id);

        });
    }

    private String getCurrentTime_custom(String hour, String min) {
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 5, 5, Integer.parseInt(hour), Integer.parseInt(min), 0);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}