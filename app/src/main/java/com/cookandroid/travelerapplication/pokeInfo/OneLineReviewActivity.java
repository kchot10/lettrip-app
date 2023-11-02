package com.cookandroid.travelerapplication.pokeInfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cookandroid.travelerapplication.R;
import com.google.android.material.tabs.TabLayout;

public class OneLineReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_line_review_success);

        //뷰페이저 어댑터 설정
        ViewPager viewPager = findViewById(R.id.view_pager);
        OneLineReviewAdapter oneLineReviewAdapter = new OneLineReviewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(oneLineReviewAdapter);

        //탭과 뷰페이저 연결
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
