package com.cookandroid.travelerapplication.pokeInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cookandroid.travelerapplication.R;
import com.google.android.material.tabs.TabLayout;

public class OneLineReviewActivity extends AppCompatActivity {

    ImageButton backBtn;
    OneLineReviewAdapter oneLineReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_line_review);

                backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PokeInfoMainActivity.class);
                startActivity(intent);
            }
        });

        //뷰페이저 어댑터 설정
        ViewPager viewPager = findViewById(R.id.view_pager);
        OneLineReviewAdapter oneLineReviewAdapter = new OneLineReviewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(oneLineReviewAdapter);

        //탭과 뷰페이저 연결
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        // 탭을 클릭했을 때 해당 프래그먼트를 표시하는 리스너 설정
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment fragment = null;

                switch (position) {
                    case 0:
                        fragment = new OneLineReivewTab1();
                        break;
                    case 1:
                        fragment = new OneLineReivewTab2();
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.view_pager, fragment) // 프래그먼트를 view_pager 영역에 표시
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
