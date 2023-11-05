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
    Fragment fragment1, fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_line_review);

        fragment1 = new Fragment_success();
        fragment2 = new Fragment_fail();

        getSupportFragmentManager().beginTransaction().add(R.id.LinearLayout, fragment1).commit();
        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    selected = fragment1;
                } else{
                    selected = fragment2;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PokeInfoMainActivity.class);
                startActivity(intent);
            }
        });


    }
}
