package com.cookandroid.travelerapplication.search;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.account.LoginActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.helper.S3Uploader;
import com.cookandroid.travelerapplication.main.MainActivity;
import com.cookandroid.travelerapplication.record.ImageAdapter;
import com.cookandroid.travelerapplication.record.ImageReview;
import com.cookandroid.travelerapplication.task.InsertData_Image;
import com.cookandroid.travelerapplication.task.SelectData_ImageFile;
import com.cookandroid.travelerapplication.task.UpdateData_Place;
import com.cookandroid.travelerapplication.task.UpdateData_Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseActivitySearch extends AppCompatActivity implements S3Uploader.OnUploadListener {


    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_IMAGE = 200;
    ArrayList<ImageReview> arrayList_image_review;
    String IP_ADDRESS;
    String review_id;
    String travel_id;
    int image_count;
    S3Uploader s3Uploader;
    TextView textView_category_search, textView_place_name_search, textView_cost_search, textView_arrived_time_search, textView_detailed_review;

    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);
        textView_category_search = findViewById(R.id.textView_category_search);
        textView_place_name_search = findViewById(R.id.textView_place_name_search);
        textView_cost_search = findViewById(R.id.textView_cost_search);
        textView_arrived_time_search = findViewById(R.id.textView_arrived_time_search);
        textView_detailed_review = findViewById(R.id.textView_detailed_review);
        travel_id = getIntent().getStringExtra("travel_id");
        textView_category_search.setText(" "+getIntent().getStringExtra("category_name")+" ");
        textView_place_name_search.setText(getIntent().getStringExtra("place_name"));
        textView_cost_search.setText(getIntent().getStringExtra("cost")+"원");
        textView_arrived_time_search.setText(getIntent().getStringExtra("arrived_time"));
        textView_detailed_review.setText(getIntent().getStringExtra("detailed_review"));
        textView_detailed_review.setEnabled(false);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        String mUser_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.recyclerView_imageFile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        review_id = getIntent().getStringExtra("review_id");
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        s3Uploader = new S3Uploader(this);

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        if (getIntent().getStringExtra("user_id").equals(mUser_id)) {
            if (subtractDates(getIntent().getStringExtra("arrived_time_real"), getCurrentTime()) <= 0) {
                if (Float.parseFloat(getIntent().getStringExtra("rating")) == 0) {
                    ratingBar.setRating(5);
                } else {
                    ratingBar.setRating(Float.parseFloat(getIntent().getStringExtra("rating")));
                }
                Button button_add_image = findViewById(R.id.button_add_image);
                ViewGroup.LayoutParams layoutParams = button_add_image.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;  // 원하는 가로 크기를 픽셀 단위로 지정
                button_add_image.setLayoutParams(layoutParams);
                ViewGroup.LayoutParams layoutParams2 = ratingBar.getLayoutParams();
                layoutParams2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                ratingBar.setLayoutParams(layoutParams2);
                textView_detailed_review.setEnabled(true);
                findViewById(R.id.button_update_review).setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "해당 날짜가 되지 않아 후기를 등록할 수 없습니다!", Toast.LENGTH_SHORT).show();
                findViewById(R.id.recyclerView_imageFile).setVisibility(View.INVISIBLE);
                findViewById(R.id.textView_detailed_review).setVisibility(View.INVISIBLE);
            }
        }

        findViewById(R.id.button_add_image).setOnClickListener(v -> {
            requestImageUpload();
        });

        findViewById(R.id.button_update_review).setOnClickListener(v -> {
            String detailed_review = textView_detailed_review.getText().toString();
            String rating = Float.toString(ratingBar.getRating());
            UpdateData_Review updateData_review = new UpdateData_Review();
            updateData_review.execute("http://" + IP_ADDRESS + "/0503/updatedata_review.php", review_id, detailed_review, rating, travel_id);

            String place_id = getIntent().getStringExtra("place_id");
            Toast.makeText(this,"place_id: "+place_id, Toast.LENGTH_SHORT).show();
            UpdateData_Place updateData_place = new UpdateData_Place();
            updateData_place.execute("http://"+IP_ADDRESS+"/0601/UpdateData_Place.php", place_id);

            for (int i = image_count; i < arrayList_image_review.size(); i++) {
                InsertData_Image insertData_image = new InsertData_Image();
                insertData_image.execute("http://" + IP_ADDRESS + "/0503/InsertData_Image.php"
                        , arrayList_image_review.get(i).getFileSize()
                        , arrayList_image_review.get(i).getOriginalFileName()
                        , arrayList_image_review.get(i).getStoredFileName()
                        , arrayList_image_review.get(i).getImageUrl()
                        , review_id);
            }




            finish();
        });

        requestPermissions();

        // Record class, SelectData_Record task, RecordAdapter
        arrayList_image_review = new ArrayList<>();
        SelectData_ImageFile task = new SelectData_ImageFile(arrayList_image_review);
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_imagefile.php", review_id);
        try {
            new Handler().postDelayed(() -> {
                image_count = arrayList_image_review.size();
                recyclerView_adapter = new ImageAdapter(arrayList_image_review, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
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


    public void Refresh() {
        recyclerView_adapter = new ImageAdapter(arrayList_image_review, this);
        recyclerView.setAdapter(recyclerView_adapter);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onSuccess(String imageUrl, String fileSize, String originalFileName, String storedFileName) {
        ImageReview image_review = new ImageReview(imageUrl, fileSize, originalFileName, storedFileName);
        arrayList_image_review.add(image_review);
        Toast.makeText(this, "이미지 업로드 성공: " + imageUrl, Toast.LENGTH_SHORT).show();
        Refresh();
    }

    @Override
    public void onFailure() {

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
    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}