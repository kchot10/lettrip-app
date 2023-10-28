package com.cookandroid.travelerapplication.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PokeListActivity extends AppCompatActivity {
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        List<PokeItem> itemList = new ArrayList<>();
        //아래는 예시 코드이고, db에서 읽어들이는 코드 추가해야 함
        itemList.add(new PokeItem("박성화 (떵)", "남자", "매칭 완료 2회", "매칭 실패 0회", "한 줄 리뷰 입니다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ", R.drawable.meetup_profile_round));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                // 이전 화면으로 돌아가기 위해 이전 액티비티를 호출
                Intent intent = new Intent(context, MeetupPostDetailActivity.class);
                context.startActivity(intent);

                // 현재 액티비티를 종료
                ((Activity) context).finish();
            }
        });
    }
}
