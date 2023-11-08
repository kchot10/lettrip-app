package com.cookandroid.travelerapplication.mypage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleListActivity;
import com.cookandroid.travelerapplication.chat.ChatListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.kotlin.KakaoAPI3;
import com.cookandroid.travelerapplication.kotlin.Place;
import com.cookandroid.travelerapplication.kotlin.ResultSearchKeyword;
import com.cookandroid.travelerapplication.meetup.MeetupPost;
import com.cookandroid.travelerapplication.meetup.MeetupPostAdapter;
import com.cookandroid.travelerapplication.meetup.RecyclerViewDecoration;
import com.cookandroid.travelerapplication.meetup.model.GpsType;
import com.cookandroid.travelerapplication.mission.MissionMainActivity;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.mypage.MypageMainActivity;
import com.cookandroid.travelerapplication.record.RecordMain;
import com.cookandroid.travelerapplication.task.SelectData_MeetUpPost;
import com.cookandroid.travelerapplication.task.SelectData_MyMeetUpPost;
import com.cookandroid.travelerapplication.task.SelectData_UserInfo;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MypageMeetUpPostList extends AppCompatActivity implements SelectData_MyMeetUpPost.AsyncTaskCompleteListener {
    private String IP_ADDRESS, user_id;
    ImageButton backBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MeetupPost> meetupPostList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_my_meetup_post);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(30));

        Refresh();
    }

    private void Refresh() {
        SelectData_MyMeetUpPost selectData_myMeetUpPost = new SelectData_MyMeetUpPost(this);
        selectData_myMeetUpPost.execute("http://" + IP_ADDRESS + "/1028/SelectData_MyMeetUpPost.php", user_id);
    }

    @Override
    public void onTaskComplete(ArrayList<MeetupPost> result) {
        try {
            MeetupPost meetupPost = result.get(0);
            runOnUiThread(()->{
                if(result.size() == 0){
                    Toast.makeText(getApplicationContext(),"관련 친구매칭 글이 없습니다",Toast.LENGTH_SHORT).show();
                }
                adapter = new MeetupPostAdapter(result, getApplicationContext());
                recyclerView.setAdapter(adapter);
            });
        }catch (Exception e){
            Log.e("errors", "나의 밋업포스트를 가져오기 못했음");
        }
    }
}
