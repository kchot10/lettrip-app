package com.cookandroid.travelerapplication.planning;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

public class PlanningMainActivity extends AppCompatActivity {
    Button saveBtn1; Button saveBtn2;EditText titleEditText;Button cityBtn1;Button cityBtn2;Button dateBtn;EditText priceEditText;
    TextView courseNumTextView;RecyclerView planningRecyclerView;FloatingActionButton floatingActionButton;Button mission;
    ImageButton board;ImageButton planningAndRecord;ImageButton chatting;ImageButton mypage;

    // MySQL 연결 정보
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/database-2"; // 데이터베이스 URL
    static final String USER = "admin"; // 데이터베이스 사용자 이름
    static final String PASS = "lettrip"; // 데이터베이스 암호


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_main);

        saveBtn1 = findViewById(R.id.planning_saveBtn1);
        saveBtn2 = findViewById(R.id.planning_saveBtn2);
        titleEditText = findViewById(R.id.planningTitleEditText);
        cityBtn1 = findViewById(R.id.cityBtn1);
        cityBtn2 = findViewById(R.id.cityBtn2);
        dateBtn = findViewById(R.id.dateBtn);
        priceEditText = findViewById(R.id.plainText_price);
        dateBtn = findViewById(R.id.dateBtn);
        courseNumTextView = findViewById(R.id.courseNum);
        planningRecyclerView = findViewById(R.id.planningRecyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        mission = findViewById(R.id.missionBtn);
        board = findViewById(R.id.boardBtn);
        planningAndRecord = findViewById(R.id.planningAndRecordBtn);
        chatting = findViewById(R.id.chattingBtn);
        mypage = findViewById(R.id.mypageBtn);


        //플로팅액션버튼 클릭 리스너
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputPopup();
            }
        });


        //데이터베이스에 저장
        saveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String city1 = cityBtn1.getText().toString();
                String city2 = cityBtn2.getText().toString();
                String date = dateBtn.getText().toString();
                int price = Integer.parseInt(priceEditText.getText().toString());
                int courseNum = Integer.parseInt(courseNumTextView.getText().toString());

                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String userEmail = preferences.getString("email", ""); // 기본값은 빈 문자열("")

                //시작날짜 & 종료날짜 구분 저장
                String[] dates = date.split("-");
                String startDate = dates[0];
                String endDate = dates[1];

                try {
                    savePlanning(title, city1, city2, startDate, endDate, price, courseNum, userEmail);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    //플로팅 팝업 열기
    private void showInputPopup(){
        Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_planning_input, null);

        //팝업 위치 설정
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        // FloatingActionButton의 위치 가져오기 - 미완
        int[] location = new int[2];
        floatingActionButton.getLocationOnScreen(location);

        int fabX = location[0];
        int fabY = location[1];

        // 팝업의 높이 (예시로 100dp)
        int popupHeight = 100;

        // 팝업의 위치 계산
        int popupX = fabX; // FloatingActionButton과 동일한 X 좌표
        int popupY = fabY - popupHeight - 10; // 팝업을 FloatingActionButton 위로 10dp 띄움

        // 팝업 생성
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY);

        // 팝업 내부의 버튼 등에 대한 처리 추가하기 **

    }

    //여행 코스 정보 db 저장
    private void savePlanning(String title, String city1, String city2, String startDate, String endDate, int price, int courseNum, String email) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // JDBC 드라이버 로딩
            Class.forName(JDBC_DRIVER);

            // 데이터베이스 연결
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 사용자 이메일을 기반으로 userId를 가져오는 쿼리
            String getUserQuery = "SELECT user_id FROM User WHERE email = ?";
            stmt = conn.prepareStatement(getUserQuery);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            int userId = -1; // 기본값으로 초기화
            if (rs.next()) {
                userId = rs.getInt("user_id");
            } else {
                System.err.println("해당 사용자를 찾을 수 없습니다."); // 사용자를 찾을 수 없을 때의 처리
                return; // 함수 종료
            }

            // 쿼리 작성
            String sql = "INSERT INTO Travel (user_id, title, city1, city2, start_date, end_date, price, course_num) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // PreparedStatement 생성
            stmt = conn.prepareStatement(sql);

            // 파라미터 설정
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, city1);
            stmt.setString(4, city2);
            stmt.setString(5, startDate);
            stmt.setString(6, endDate);
            stmt.setInt(7, price);
            stmt.setInt(8, courseNum);

            // 쿼리 실행
            stmt.executeUpdate();

            // 생성된 travelId 가져오기
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int travelId = generatedKeys.getInt(1);
                System.out.println("여행 정보가 데이터베이스에 저장되었습니다. Travel ID: " + travelId);
            } else {
                System.err.println("여행 정보 저장 중 오류 발생");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}
