package com.cookandroid.travelerapplication.mongo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.mission.UserInfo;
import com.cookandroid.travelerapplication.task.SelectData_MyPoint;

import java.util.ArrayList;

public class MongoActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "43.201.21.27";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mongo);
        TextView myPointText = findViewById(R.id.textView6);

        ArrayList<UserInfo> arrayListUserInfo = new ArrayList();
        SelectData_MyPoint selectData_myPoint = new SelectData_MyPoint(arrayListUserInfo);
        selectData_myPoint.execute("http://" + IP_ADDRESS + "/0930/select_my_point.php", "1");
        new Handler().postDelayed(() -> {
            try {
                String myPoint = arrayListUserInfo.get(0).getPoint()+" P";
                myPointText.setText(myPoint);
            }catch (Exception e){
                Log.e("youn", "point 불러오기 실패");
            }
        }, 2000); // 0.5초 지연 시간
    }
}