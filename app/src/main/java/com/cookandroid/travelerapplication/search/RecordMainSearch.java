package com.cookandroid.travelerapplication.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.record.Course;
import com.cookandroid.travelerapplication.record.CourseAdapter;
import com.cookandroid.travelerapplication.task.InsertData_Like;
import com.cookandroid.travelerapplication.task.SelectData_Course;
import com.cookandroid.travelerapplication.task.SelectData_Like;

import java.util.ArrayList;

public class RecordMainSearch extends AppCompatActivity {


    ArrayList<Course> courseArrayList;
    String IP_ADDRESS, user_id;
    String travel_id, city, total_cost, number_of_courses;

    TextView textView_total_cost_search, textView_number_of_courses_search, textView_city_search, textView_theme_search;
    FileHelper fileHelper;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main_search);
        textView_total_cost_search =findViewById(R.id.textView_total_cost_search);
        textView_number_of_courses_search =findViewById(R.id.textView_number_of_courses_search);
        textView_city_search =findViewById(R.id.textView_city_search);
        textView_theme_search =findViewById(R.id.textView_theme_search);
        textView_theme_search.setText(getIntent().getStringExtra("travel_theme"));
        textView_number_of_courses_search.setText(getIntent().getStringExtra("number_of_courses"));
        textView_total_cost_search.setText(getIntent().getStringExtra("total_cost"));
        textView_city_search.setText(getIntent().getStringExtra("city"));

        fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        user_id = fileHelper.readFromFile("user_id");
        recyclerView = findViewById(R.id.recyclerView_course);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        city = getIntent().getStringExtra("city");
        total_cost = getIntent().getStringExtra("total_cost");
        number_of_courses = getIntent().getStringExtra("number_of_courses");
        travel_id = getIntent().getStringExtra("travel_id");
        Refresh();

        findViewById(R.id.heartBtn2).setOnClickListener(v -> {
            String liked_type = "1";
            ArrayList<Like> likeArrayList = new ArrayList<>();
            SelectData_Like selectData_like = new SelectData_Like(likeArrayList);
            selectData_like.execute("http://" + IP_ADDRESS + "/0601/select_like.php",liked_type, travel_id, user_id);

//            new Handler().postDelayed(() -> {
//                String like_id = "";
//                try {
//                    like_id = likeArrayList.get(0).getLike_id();
//                }catch (Exception e){
//                    Log.e("youn", "like_id 불러오기 실패");
//                }
//                if ( !like_id.equals("") ) { // like_id에 아무것도 저장되어있지 않지 않다면 (뭐라도 있다면)
//                    DeleteData_Like deleteData_like = new DeleteData_Like();
//                    deleteData_like.execute("http://" + IP_ADDRESS + "/0601/deleteData_like.php",like_id);
//                }else{
//                    InsertData_Like insertData_like = new InsertData_Like();
//                    insertData_like.execute("http://" + IP_ADDRESS + "/0601/InsertData_like_travel.php", liked_type, travel_id, user_id);
//                }
//            }, 500); // 0.5초 지연 시간



        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Refresh();
    }

    public void Refresh() {
        // Record class, SelectData_Record task, RecordAdapter
        courseArrayList = new ArrayList<>();
        SelectData_Course task = new SelectData_Course(courseArrayList);
        task.execute("http://" + IP_ADDRESS + "/0503/selectdata_course.php", travel_id);
        try {
            new Handler().postDelayed(() -> {
                recyclerView_adapter = new CourseAdapter(courseArrayList, this);
                recyclerView.setAdapter(recyclerView_adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}