package com.cookandroid.travelerapplication.meetup;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.travelerapplication.R;

public class ItemMeetupMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_meetup_main);

        ImageView imageView = findViewById(R.id.isGPSselected);

        // 현재 이미지뷰의 리소스 ID를 가져옵니다.
        int currentImageResource = imageView.getDrawable().getConstantState().hashCode();

        int imageResource;
        int widthInDp;

        if (currentImageResource == getResources().getDrawable(R.drawable.meetup_post_nongps_icon).getConstantState().hashCode()) {
            imageResource = R.drawable.meetup_post_nongps_icon;
            widthInDp = 61; // meetup_post_nongps_icon의 경우 61dp
        } else {
            imageResource = R.drawable.meetup_post_gps_icon;
            widthInDp = 45; // meetup_post_gps_icon의 경우 45dp
        }

        imageView.setImageResource(imageResource);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, getResources().getDisplayMetrics());
        imageView.setLayoutParams(params);



    }
}
