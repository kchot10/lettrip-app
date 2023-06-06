package com.cookandroid.travelerapplication.mypage;

import static android.provider.LiveFolders.INTENT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.MbEditText;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;
import com.kakao.sdk.user.model.User;

import java.io.File;
import java.util.ArrayList;

public class MypageModifyActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    FileHelper fileHelper;
    String IP_ADDRESS, user_id;
    String newNickName, stored_file_url;
    Uri selectedImageUri;
    MbEditText userNickName_editText;
    TextView userName;

    ImageButton editBtn_userName, editBtn_userEmail, profilePhotoBtn;
    
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

        ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
        SelectData_UserInfo selectData_userInfo = new SelectData_UserInfo(userInfoArrayList);
        selectData_userInfo.execute("http://"+IP_ADDRESS+"/0601/selectData_userInfo.php", user_id);
        new Handler().postDelayed(() -> {
            userName.setText(userInfoArrayList.get(0).getNickname());
            Glide.with(this)
                    .load(userInfoArrayList.get(0).getStored_file_url())
                    .placeholder(R.drawable.user)
                    .into(profilePhotoBtn);
        }, 500); // 0.5초 지연 시간

        profilePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        editBtn_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_userNickName();
            }
        });

        Modify_fisnishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_finish();
            }
        });
    }

    
    private void Modify_finish() {
        try {
            newNickName = String.valueOf(userNickName_editText.getText());
            Toast.makeText(this,"newNickName: "+newNickName,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            newNickName = "";
            Toast.makeText(this,"변경사항이 없습니다!",Toast.LENGTH_SHORT).show();
        }
        //nickname, imageurl db에 저장
        Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
        finish();
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

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            //해당 사진에 대한 처리
            ImageButton profilePhotoBtn = findViewById(R.id.profile_modifyBtn);
            profilePhotoBtn.setImageURI(selectedImageUri);
        }
    }
}
