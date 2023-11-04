package com.cookandroid.travelerapplication.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.model.GpsType;
import com.cookandroid.travelerapplication.task.SelectData_MeetUpPost;
import com.cookandroid.travelerapplication.task.SelectData_Poke;

import java.util.ArrayList;
import java.util.List;

public class MeetupPostMainAcitivty extends AppCompatActivity implements SelectData_MeetUpPost.AsyncTaskCompleteListener {
    String IP_ADDRESS = "43.201.78.198", user_id="25";
    FileHelper fileHelper;
    ImageButton chatBtn;
    Spinner gpsSelected;
    Spinner city1;
    Spinner city2;
    Button addPost;
    String is_gps_enabled = "all";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_main);

        fileHelper = new FileHelper(this);
        fileHelper.writeToFile("IP_ADDRESS", IP_ADDRESS);//Todo: 나중에 쓰는 부분은 지울듯
        fileHelper.writeToFile("user_id", user_id);//Todo: 나중에 쓰는 부분은 지울듯
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");

        recyclerView = findViewById(R.id.RecyclerView_MeetUpPost);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        chatBtn = findViewById(R.id.chatBtn);
        gpsSelected = findViewById(R.id.GpsSpinner);
        city1 = findViewById(R.id.citySpinner);
        city2 = findViewById(R.id.citySpinner2);
        addPost = findViewById(R.id.writeBtn);

        //gps 스피너
        String[] gpsStatus = {"전체", "GPS 정보 사용", "GPS 정보 미사용"};
        ArrayAdapter<String> adapterGpsSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpsStatus); //추후 스피너 레이아웃 커스텀하기
        adapterGpsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gpsSelected.setAdapter(adapterGpsSpinner);

        Refresh(GpsType.GPS_ALL.toString());
        gpsSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedGpsStatus = gpsStatus[position];
                switch (selectedGpsStatus) {
                    case "전체":
                        Refresh(GpsType.GPS_ALL.toString());
                        break;
                    case "GPS 정보 사용":
                        Refresh(GpsType.GPS_ENABLE.toString());
                        break;
                    case "GPS 정보 미사용":
                        Refresh(GpsType.GPS_DISABLE.toString());
                        break;
                    // 다른 GPS 상태에 대한 case 문 추가
                    default:
                        // 기본적으로 처리할 내용 (예: 아무 동작 안 함)
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //글쓰기 수행
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetupAddPostActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.addItemDecoration(new RecyclerViewDecoration(30));

    }



    private void Refresh(String gpsType) {
        SelectData_MeetUpPost selectData_meetUpPost = new SelectData_MeetUpPost(this);
        selectData_meetUpPost.execute("http://" + IP_ADDRESS + "/1028/SelectData_MeetUpPost.php", gpsType);
    }

    @Override
    public void onTaskComplete(ArrayList<MeetupPost> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result.size() == 0){
                    Toast.makeText(getApplicationContext(),"관련 친구매칭 글이 없습니다",Toast.LENGTH_SHORT).show();
                }
                adapter = new MeetupPostAdapter(result, getApplicationContext());
                recyclerView.setAdapter(adapter);

            }
        });
    }
}
