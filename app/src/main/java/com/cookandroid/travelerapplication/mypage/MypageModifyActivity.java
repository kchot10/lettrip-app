package com.cookandroid.travelerapplication.mypage;

import static android.provider.LiveFolders.INTENT;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.MbEditText;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.helper.S3Uploader;
import com.cookandroid.travelerapplication.meetup.PokeItem;
import com.cookandroid.travelerapplication.meetup.PokeListAdapter;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;
import com.cookandroid.travelerapplication.task.UpdateData_UserInfo;
import com.kakao.sdk.user.model.User;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MypageModifyActivity extends AppCompatActivity implements S3Uploader.OnUploadListener, SelectData_UserInfo.AsyncTaskCompleteListener{
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_IMAGE = 200;

    FileHelper fileHelper;
    String IP_ADDRESS, user_id;
    String newNickName, image_url, newBirth;
    Uri selectedImageUri;
    MbEditText userNickName_editText;

    TextView userName;
    String mImageUrl = "";

    ImageButton editBtn_userName, editBtn_userEmail, profilePhotoBtn, editBtn_userBirth;

    ArrayList<UserInfo> userInfoArrayList;
    ImageButton backBtn; TextView userBirth; TextView userEmail;
    Spinner userSexSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_modify);
        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        profilePhotoBtn = findViewById(R.id.profile_modifyBtn);
        editBtn_userName = findViewById(R.id.editBtn_userName);
        editBtn_userEmail = findViewById(R.id.editBtn_userEmail);
        Button Modify_fisnishBtn = findViewById(R.id.modifyOkBtn);
        userName = findViewById(R.id.userName);
        editBtn_userBirth = findViewById(R.id.editBtn_userBirth);
        backBtn = findViewById(R.id.backBtn);
        userBirth = findViewById(R.id.userBirth);
        userSexSpinner = findViewById(R.id.userSexSpinner);

        Refresh();

        profilePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestImageUpload();
            }
        });

        editBtn_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_userNickName();
            }
        });

        editBtn_userBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_userBirth();
            }
        });

        Modify_fisnishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_finish();
            }
        });

        requestPermissions();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // 뒤로가기 버튼을 눌른 것과 동일한 동작
            }
        });

        //성별 선택 스피너
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSexSpinner.setAdapter(adapter);
        String selectedGender = ""; // 성별을 저장할 변수

        userSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);

                // 선택된 성별에 따라 변수에 값을 저장
                if (selectedGender.equals("남자")) {
                    selectedGender = "MALE";
                } else if (selectedGender.equals("여자")) {
                    selectedGender = "FEMALE";
                }
                // TODO: 선택된 성별 DB 추가 (selectedGender변수에 저장되어 있음)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않았을 때의 동작 수행
            }
        });

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void Refresh() {
        userInfoArrayList = new ArrayList<>();
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(userInfoArrayList, this);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", user_id);
        new Handler().postDelayed(() -> {
            try {
                userName.setText(userInfoArrayList.get(0).getNickname());
                Glide.with(this)
                        .load(userInfoArrayList.get(0).getStored_file_url())
                        .placeholder(R.drawable.user)
                        .into(profilePhotoBtn);
            }catch (Exception e){
                Log.e("nullException", "사용자 정보 불러오기 실패");
            }
        }, 500); // 0.5초 지연 시간
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
        S3Uploader s3Uploader = new S3Uploader(this);
        s3Uploader.uploadImage(imageUri, this);
    }

    
    private void Modify_finish() {
        try {
            newNickName = String.valueOf(userNickName_editText.getText());

        }catch (Exception e){
            newNickName = "";
        }
        image_url = mImageUrl;
        if (newNickName.equals("")&&mImageUrl.equals("")){
            Toast.makeText(this,"변경사항이 없습니다!",Toast.LENGTH_SHORT).show();
        }else {
            if (newNickName.equals("")){
                newNickName = userInfoArrayList.get(0).getNickname();
            }
            if (image_url.equals("")){
                image_url = userInfoArrayList.get(0).getStored_file_url();
            }

            UpdateData_UserInfo updateData_userInfo = new UpdateData_UserInfo();
            updateData_userInfo.execute("http://" + IP_ADDRESS + "/0601/UpdateData_UserInfo.php", user_id, newNickName, image_url);
            Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
            finish();
        }



    }

    private void Modify_userNickName() {
        editBtn_userName.setVisibility(View.INVISIBLE);
        //textView를 Edittext로 변경
        userNickName_editText = new MbEditText(this);
        TextView userNickName = findViewById(R.id.userName);

        userNickName_editText.setId(R.id.userName);
        ViewGroup parent = (ViewGroup)  userNickName.getParent();
        int index = parent.indexOfChild(userNickName);
        parent.removeView(userNickName);

        String name = (String) userNickName.getText();
        userNickName_editText.setText(name);
        parent.addView(userNickName_editText, index);

    }

    private void Modify_userBirth() {

            // DatePickerDialog를 통해 날짜 선택
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            // 사용자가 날짜를 선택했을 때 호출되는 콜백
                            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                            // 선택된 날짜를 EditText에 설정
                            userBirth.setText(selectedDate);

                            // 선택된 날짜를 newBirth 변수에 저장
                            newBirth = selectedDate;
                        }
                    }, year, month, day);

            // DatePickerDialog 표시
            datePickerDialog.show();

        String selectedDate = year + "-" + (month + 1) + "-" + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;

        try {
            date = sdf.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            // TODO:DB에 생년월일 저장 - date 변수에 저장되어 있는 상태
        } else {
            // 변환에 실패한 경우
        }

    }


    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onSuccess(String imageUrl, String fileSize, String originalFileName, String storedFileName) {
        mImageUrl = imageUrl;
        Toast.makeText(this, "이미지 업로드 성공: " + imageUrl, Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(mImageUrl)
                .into(profilePhotoBtn);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onTaskComplete_SelectData_UserInfo(UserInfo result) {

    }
}
