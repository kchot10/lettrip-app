package com.cookandroid.travelerapplication.record;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.task.InsertData_Course;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.InsertData_Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseActivity extends AppCompatActivity implements S3Uploader.OnUploadListener {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_IMAGE = 200;

    ArrayList<String> imageArrayList;
    private S3Uploader s3Uploader;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    String IP_ADDRESS;
    FileHelper fileHelper;
    String city, province, depart_date;


    Button button_place_search;

    EditText editText_detailed_review;
    RatingBar edit_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_course);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        depart_date = getIntent().getStringExtra("depart_date");
        button_place_search = findViewById(R.id.button_place_search);
        EditText editText_day_count = findViewById(R.id.editText_day_count);
        EditText editText_arrived_time_hour = findViewById(R.id.editText_arrived_time_hour);
        EditText editText_arrived_time_min = findViewById(R.id.editText_arrived_time_min);
        EditText editText_cost = findViewById(R.id.editText_cost);
        editText_detailed_review = findViewById(R.id.editText_detailed_review);
        edit_rating = findViewById(R.id.edit_rating);



        recyclerView = findViewById(R.id.RecyclerView_placePhoto);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

        findViewById(R.id.button_image_add).setOnClickListener(v -> {
            requestImageUpload();
        });

        // S3Uploader 초기화
        s3Uploader = new S3Uploader(this);

        imageArrayList = new ArrayList<>();

        findViewById(R.id.button_add_review).setOnClickListener(v -> {
            if (button_place_search.getText().toString().trim().equals("장소 검색")){
                Toast.makeText(this, "장소를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_day_count.getText().toString().trim().equals("")) {
                Toast.makeText(this, "일차를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_arrived_time_min.getText().toString().trim().equals("")||editText_arrived_time_hour.getText().toString().trim().equals("")) {
                Toast.makeText(this, "도착 시간을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_cost.getText().toString().trim().equals("")) {
                Toast.makeText(this, "비용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_detailed_review.getText().toString().trim().equals("")) {
                Toast.makeText(this, "상세 후기를 입력하세요", Toast.LENGTH_SHORT).show();
            }

            else {
                String created_date = getCurrentTime();
                String detailed_review = editText_detailed_review.getText().toString().trim();
                String rating = Float.toString(edit_rating.getRating());
                String solo_friendly_rating = Float.toString(edit_rating.getRating()); //Todo: 일단 똑같이했는데 아마 혼밥 여행 관련인듯
                String visit_times = "1";
                String place_id = fileHelper.readFromFile("place_id");
                String user_id = fileHelper.readFromFile("user_id");

                InsertData_Review insertData_review = new InsertData_Review();
                insertData_review.execute("http://"+IP_ADDRESS+"/0503/InsertData_Review.php"
                        ,created_date,detailed_review,rating, solo_friendly_rating,visit_times, place_id, user_id);
                new Handler().postDelayed(() -> {
                    String withdraw_result = insertData_review.getReturn_string();
                    if (withdraw_result.equals("실패")) {
                        Toast.makeText(this, "리뷰 추가는 완료되었으나 review_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    } else if (withdraw_result.equals("에러")) {
                        Toast.makeText(this, "리뷰 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "리뷰 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        fileHelper.writeToFile("review_id", withdraw_result);
                        findViewById(R.id.button_image_add).setVisibility(View.VISIBLE);
                        findViewById(R.id.button_add_cource).setVisibility(View.VISIBLE);
                        findViewById(R.id.button_add_review).setVisibility(View.INVISIBLE);
                        edit_rating.setEnabled(false);
                        editText_detailed_review.setEnabled(false);
                    }
                }, 1000); // 0.5초 지연 시간

            }

        });

        findViewById(R.id.button_add_cource).setOnClickListener(v -> {
            if (button_place_search.getText().toString().trim().equals("장소 검색")){
                Toast.makeText(this, "장소를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_day_count.getText().toString().trim().equals("")) {
                Toast.makeText(this, "일차를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_arrived_time_min.getText().toString().trim().equals("")||editText_arrived_time_hour.getText().toString().trim().equals("")) {
                Toast.makeText(this, "도착 시간을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_cost.getText().toString().trim().equals("")) {
                Toast.makeText(this, "비용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String hour = editText_arrived_time_hour.getText().toString().trim();
                String min = editText_arrived_time_min.getText().toString().trim();
                String cost = editText_cost.getText().toString().trim();
                String day_count = editText_day_count.getText().toString().trim();
                depart_date = addDays(depart_date, day_count);
                String arrived_time = getCurrentTime_custom(hour, min);
                String place_id = fileHelper.readFromFile("place_id");
                String review_review_id = "1";
                String travel_id = fileHelper.readFromFile("travel_id");

                InsertData_Course insertData_course = new InsertData_Course();
                insertData_course.execute("http://"+IP_ADDRESS+"/0503/InsertData_Course.php",arrived_time,cost,day_count, place_id,review_review_id, travel_id);
                finish();
            }
        });

        findViewById(R.id.button_place_search).setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinActivity.class);
            getKotlinActivityResult.launch(intent);
        });

        requestPermissions();
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }

    private void requestImageUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    uploadImage(imageUri);
                }
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        s3Uploader.uploadImage(imageUri, this);
    }

    private void Refresh() {
        adapter = new ImageAdapter(imageArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    private String getCurrentTime_custom(String hour, String min) {
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int RealMonth_const = 1;
        calendar.set(2023, 5-RealMonth_const, 5, Integer.parseInt(hour), Integer.parseInt(min), 0);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(depart_date+" HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    private String addDays(String date, String days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, Integer.parseInt(days));
        return sdf.format(cal.getTime());
    }


    private final ActivityResultLauncher<Intent> getKotlinActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    button_place_search.setText(result.getData().getStringExtra("name"));
                    String place_name = result.getData().getStringExtra("name");
                    String road = result.getData().getStringExtra("road");
                    String address = result.getData().getStringExtra("address");
                    String x = result.getData().getStringExtra("location_x");
                    String y = result.getData().getStringExtra("location_y");
                    String location_point = "POINT("+x+" "+y+")";
                    String category_code = result.getData().getStringExtra("category_group_code");
                    String category_name = result.getData().getStringExtra("category_group_name");
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

                    }, 1000); // 0.5초 지연 시간



                }
            }
    );

    @Override
    public void onProgress(int progress) {
    }

    @Override
    public void onSuccess(String imageUrl) {
        // 해결되면 주석해제
//        imageArrayList.add(imageUrl);
        imageArrayList.add("https://lettripbucket.s3.ap-northeast-2.amazonaws.com/1516b285-750f-4aef-bddf-74f80d5e1cee.png");
        Toast.makeText(this, "이미지 업로드 성공: " + imageUrl, Toast.LENGTH_SHORT).show();
        Refresh();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}