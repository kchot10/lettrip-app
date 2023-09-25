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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.helper.S3Uploader;
import com.cookandroid.travelerapplication.kotlin.KotlinActivity;
import com.cookandroid.travelerapplication.task.InsertData_Course;
import com.cookandroid.travelerapplication.task.InsertData_Image;
import com.cookandroid.travelerapplication.task.InsertData_Place;
import com.cookandroid.travelerapplication.task.InsertData_Review;
import com.cookandroid.travelerapplication.task.SelectData_Place;
import com.cookandroid.travelerapplication.task.UpdateData_Place;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseActivity extends AppCompatActivity implements S3Uploader.OnUploadListener {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_IMAGE = 200;
    String place_name, address, category_name;

    ArrayList<ImageReview> arrayList_image_review;
    private S3Uploader s3Uploader;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    String IP_ADDRESS;
    FileHelper fileHelper;
    String city, province, depart_date;


    Button button_place_search;
    String place_address_and_placeName;

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
        CheckBox checkBox = findViewById(R.id.checkBox);
        editText_detailed_review = findViewById(R.id.editText_detailed_review);
        edit_rating = findViewById(R.id.edit_rating);

        if (getIntent().getStringExtra("record/plan").equals("plan")){
            edit_rating.setVisibility(View.INVISIBLE);
            editText_detailed_review.setVisibility(View.INVISIBLE);
            Button button = findViewById(R.id.button_add_review);
            button.setText("등록");
        }

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

        arrayList_image_review = new ArrayList<>();


        findViewById(R.id.button_add_review).setOnClickListener(v -> {
            if (button_place_search.getText().toString().trim().equals("장소 검색")){
                Toast.makeText(this, "장소를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_day_count.getText().toString().trim().equals("")) {
                Toast.makeText(this, "일차를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_arrived_time_min.getText().toString().trim().equals("")||editText_arrived_time_hour.getText().toString().trim().equals("")) {
                Toast.makeText(this, "도착 시간을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_cost.getText().toString().trim().equals("")) {
                Toast.makeText(this, "비용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (editText_detailed_review.getText().toString().trim().equals("") && getIntent().getStringExtra("record/plan").equals("record")) {
                Toast.makeText(this, "상세 후기를 입력하세요", Toast.LENGTH_SHORT).show();
            }

            else {
                String created_date = getCurrentTime();
                String detailed_review = editText_detailed_review.getText().toString().trim();
                String rating = Float.toString(edit_rating.getRating());
                String solo_friendly_rating = "0";
                String visit_times = "1";
                String place_id = fileHelper.readFromFile("place_id");
                String user_id = fileHelper.readFromFile("user_id");
                if(getIntent().getStringExtra("record/plan").equals("plan")){
                    rating = "0";
                }
                if (checkBox.isChecked()) {
                    solo_friendly_rating = "1";
                }

                String place_category = category_name;
                InsertData_Review insertData_review = new InsertData_Review();
                insertData_review.execute("http://"+IP_ADDRESS+"/0503/InsertData_Review.php"
                        ,created_date,detailed_review,rating, solo_friendly_rating,visit_times, place_id, user_id, place_category, address, place_name);
                new Handler().postDelayed(() -> {
                    String withdraw_result = insertData_review.getReturn_string();
                    if (withdraw_result.equals("실패")) {
                        Toast.makeText(this, "리뷰 추가는 완료되었으나 review_id를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    } else if (withdraw_result.equals("에러")) {
                        Toast.makeText(this, "리뷰 추가가 에러났습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "리뷰 추가에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        UpdateData_Place updateData_place = new UpdateData_Place();
                        updateData_place.execute("http://"+IP_ADDRESS+"/0601/UpdateData_Place.php", place_id);
                        fileHelper.writeToFile("review_id", withdraw_result);
                        if (getIntent().getStringExtra("record/plan").equals("plan")){
                            new Handler().postDelayed(() -> {
                                    findViewById(R.id.button_add_cource).performClick();
                            }, 500);
                        } else{
                            findViewById(R.id.button_image_add).setVisibility(View.VISIBLE);
                            findViewById(R.id.button_add_cource).setVisibility(View.VISIBLE);
                            findViewById(R.id.button_add_review).setVisibility(View.INVISIBLE);
                            edit_rating.setEnabled(false);
                            editText_detailed_review.setEnabled(false);
                        }
                    }
                }, 1000); // 0.5초 지연 시간

            }

        });
        editText_day_count.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                int max_day_count = getIntent().getIntExtra("total_day_count", 1);
                String day_count = editText_day_count.getText().toString();
                if (day_count.isEmpty()){
                    return;
                }else {
                    int value = Integer.parseInt(day_count);
                    if ( value > max_day_count || 1 > value) {
                        Toast.makeText(getApplicationContext(), "1부터 "+ max_day_count+ "사이의 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        editText_day_count.setText("");
                    }
                }
            }
        });

        editText_arrived_time_hour.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String hour = editText_arrived_time_hour.getText().toString();
                if (hour.isEmpty()){
                    return;
                } else {
                    int value = Integer.parseInt(hour);
                    // 값이 1~24 사이인지 확인합니다.
                    if (value >= 0 && value <= 23) {
                        editText_arrived_time_min.setText("00");
                    } else {
                        Toast.makeText(getApplicationContext(), "0부터 23 사이의 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        editText_arrived_time_hour.setText("");
                    }
                }
            }
        });

        editText_arrived_time_min.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String min = editText_arrived_time_min.getText().toString();
                if (min.isEmpty()){
                    return;
                } else {
                    int value = Integer.parseInt(min);
                    if (value >= 0 && value <= 59) {
                    } else {
                        Toast.makeText(getApplicationContext(), "0부터 59 사이의 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        editText_arrived_time_min.setText("");
                    }
                }
            }
        });



        editText_detailed_review.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력 변경 중의 동작
                int maxLines = 10; // 최대 허용 라인 수
                String[] lines = editText_detailed_review.getText().toString().split("\n");
                if (lines.length > maxLines) {
                    // 최대 허용 라인 수를 초과한 경우 입력 제거
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < maxLines; i++) {
                        sb.append(lines[i]).append("\n");
                    }
                    editText_detailed_review.setText(sb.toString().trim());
                    editText_detailed_review.setSelection(editText_detailed_review.getText().length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            // 최소값과 최대값 설정
            float minValue = 1.0f;
            // 현재 선택된 별점이 최소값보다 작은 경우 최소값으로 설정
            if (rating < minValue) {
                ratingBar.setRating(minValue);
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
                String review_review_id = fileHelper.readFromFile("review_id");
                String travel_id = fileHelper.readFromFile("travel_id");

                for (int i = 0; i < arrayList_image_review.size(); i++) {
                    InsertData_Image insertData_image = new InsertData_Image();
                    insertData_image.execute("http://" + IP_ADDRESS + "/0503/InsertData_Image.php"
                            , arrayList_image_review.get(i).getFileSize()
                            , arrayList_image_review.get(i).getOriginalFileName()
                            , arrayList_image_review.get(i).getStoredFileName()
                            , arrayList_image_review.get(i).getImageUrl()
                            , review_review_id);
                }


                InsertData_Course insertData_course = new InsertData_Course();
                insertData_course.execute("http://"+IP_ADDRESS+"/0503/InsertData_Course.php",arrived_time,cost,day_count, place_id,review_review_id, travel_id);

                Log.d("lettrip", "arrived_time:"+arrived_time+
                                "\ncost:"+cost + "\nday_count:"+day_count+
                        "\nplace_id:"+place_id+
                        "\nreview_review_id:"+review_review_id+
                        "\ntravel_id:"+travel_id);
                finish();
            }
        });

        findViewById(R.id.button_place_search).setOnClickListener(v -> {
            Intent intent = new Intent(this, KotlinActivity.class);
            try {
                if (!place_address_and_placeName.isEmpty() || place_address_and_placeName != ""){
                    intent.putExtra("place_address_and_placeName", place_address_and_placeName);
                }
            }catch (Exception e){
                Log.e("Error", "place_address_and_placeName is empty");
            }
            getKotlinActivityResult.launch(intent);
        });

        requestPermissions();

        new Handler().postDelayed(()-> {
            try {
                if (!getIntent().getStringExtra("place_address_and_placeName").isEmpty() || !getIntent().getStringExtra("place_address_and_placeName").equals("")){
                    place_address_and_placeName = getIntent().getStringExtra("place_address_and_placeName");
                    button_place_search.performClick();
                }
            }catch (Exception e){
                Log.e("Error", "place_address_and_placeName is empty");
            }
        }, 500);
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
        adapter = new ImageAdapter(arrayList_image_review, this);
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
                    place_name = result.getData().getStringExtra("name");
                    String road = result.getData().getStringExtra("road");
                    address = result.getData().getStringExtra("address");
                    String x = result.getData().getStringExtra("location_x");
                    String y = result.getData().getStringExtra("location_y");
                    String location_point = "POINT("+x+" "+y+")";
                    String category_code = result.getData().getStringExtra("category_group_code");
                    category_name = result.getData().getStringExtra("category_group_name");
                    String total_rating = Float.toString(edit_rating.getRating());

                    if (getIntent().getStringExtra("record/plan").equals("record")) {
                        findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
                    }

                    ArrayList<Place> arrayListPlace = new ArrayList<>();
                    SelectData_Place selectData_place = new SelectData_Place(arrayListPlace);
                    selectData_place.execute("http://"+IP_ADDRESS+"/0601/select_location_point.php",location_point);
                    new Handler().postDelayed(() -> {
                        String place_id = "";
                        try {
                            place_id = arrayListPlace.get(0).getPlace_id();
                        }catch (Exception e){
                            Log.e("youn", "place_id 불러오기 실패");
                        }
                        if ( !place_id.equals("") ){ // place_id에 아무것도 저장되어있지 않지 않다면
                            fileHelper.writeToFile("place_id", place_id);
                            Toast.makeText(this, "기존에 저장되어있던 place_id 불러오기 성공! place_id:"+place_id, Toast.LENGTH_SHORT).show();
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
                                    fileHelper.writeToFile("place_id", withdraw_result);
                                }
                            }, 1000); // 0.5초 지연 시간
                        }

                    },500);
                }
            }
    );

    @Override
    public void onProgress(int progress) {
    }

    @Override
    public void onSuccess(String imageUrl, String fileSize, String originalFileName, String storedFileName) {
        // 해결되면 주석해제
        ImageReview image_review = new ImageReview(imageUrl, fileSize, originalFileName, storedFileName);
        arrayList_image_review.add(image_review);
        Toast.makeText(this, "이미지 업로드 성공: ", Toast.LENGTH_SHORT).show();
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