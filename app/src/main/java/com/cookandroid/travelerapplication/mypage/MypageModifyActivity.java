package com.cookandroid.travelerapplication.mypage;

import static android.provider.LiveFolders.INTENT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.cookandroid.travelerapplication.R;

public class MypageModifyActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    String pw;
    String name;
    String newEmail;
    String newNickName;
    Uri selectedImageUri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_modify);

        ImageButton profilePhotoBtn = findViewById(R.id.profile_modifyBtn);
        ImageButton editBtn_userName = findViewById(R.id.editBtn_userName);
        ImageButton editBtn_userEmail = findViewById(R.id.editBtn_userEmail);
        EditText password_EditText = findViewById(R.id.editTextPassword);
        EditText userName_EditText = findViewById(R.id.editTextPassword);
        Button Modify_fisnishBtn = findViewById(R.id.modifyOkBtn);


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

        editBtn_userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_userEmail();
            }
        });

        password_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_password();
            }
        });

        userName_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modify_userName();
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
        //name, email, pw, nickname, imageurl db에 저장
        Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
        startActivity(intent);
    }

    private void Modify_userName() {
        //입력된 이름 가져오기
        EditText userName_EditText = findViewById(R.id.editTextPassword);
        name = String.valueOf(userName_EditText.getText());
    }

    private void Modify_password() {
        //입력된 비밀번호 가져오기
        EditText password_EditText = findViewById(R.id.editTextPassword);
        pw = String.valueOf(password_EditText.getText());
    }

    private void Modify_userEmail() {
        //textView를 Edittext로 변경
        EditText userEmail_editText = new EditText(this);
        TextView userName = findViewById(R.id.userEmail);

        userEmail_editText.setId(R.id.userEmail);
        ViewGroup parent = (ViewGroup)  userName.getParent();
        int index = parent.indexOfChild(userName);
        parent.removeView(userName);

        String email = (String) userName.getText();
        userEmail_editText.setText(email);
        parent.addView(userEmail_editText, index);

        newEmail = String.valueOf(userEmail_editText.getText());
    }

    private void Modify_userNickName() {
        //textView를 Edittext로 변경
        EditText userNickName_editText = new EditText(this);
        TextView userNickName = findViewById(R.id.userName);

        userNickName_editText.setId(R.id.userName);
        ViewGroup parent = (ViewGroup)  userNickName.getParent();
        int index = parent.indexOfChild(userNickName);
        parent.removeView(userNickName);

        String name = (String) userNickName.getText();
        userNickName_editText.setText(name);
        parent.addView(userNickName_editText, index);

        newNickName = String.valueOf(userNickName_editText.getText());
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
