package com.cookandroid.travelerapplication.travel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.task.InsertData_Course;
import com.cookandroid.travelerapplication.task.InsertData_Place;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseActivity extends AppCompatActivity {

    String IP_ADDRESS;
    FileHelper fileHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        fileHelper = new FileHelper(this);
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
            String place_id = fileHelper.readFromFile("place_id");
            String review_review_id = "1";
            String travel_id = fileHelper.readFromFile("travel_id");

            InsertData_Course insertData_course = new InsertData_Course();
            insertData_course.execute("http://"+IP_ADDRESS+"/0503/InsertData_Course.php",arrived_time,cost,day_count, place_id,review_review_id, travel_id);
            finish();
        });

        findViewById(R.id.button_place_search).setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinActivity.class);
            getKotlinActivityResult.launch(intent);
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

    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    Button button_place_search = findViewById(R.id.button_place_search);
                    button_place_search.setText(result.getData().getStringExtra("name"));
                    String place_name = result.getData().getStringExtra("name");
                    String road = result.getData().getStringExtra("road");
                    String address = result.getData().getStringExtra("address");
                    String x = result.getData().getStringExtra("location_x");
                    String y = result.getData().getStringExtra("location_y");
                    String location_point = "POINT("+x+","+y+")";
                    String category_code = result.getData().getStringExtra("category_group_code");
                    String category_name = result.getData().getStringExtra("category_group_name");
                    String province = result.getData().getStringExtra("province");
                    String city = result.getData().getStringExtra("city");
                    String total_rating = "3";

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
                            fileHelper.writeToFile("place_id", withdraw_result);
                        }
                    }, 500); // 0.5초 지연 시간

                }
            }
    );
}