package com.cookandroid.travelerapplication.meetup;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.kotlin.KotlinActivityMeetupPostPlace;

public class OptionalBtnClickListener implements View.OnClickListener{
    private final Context mContext;
    private final TextView placeName;
    private final TextView placeCategory;
    private final TextView placeAddress;

    public OptionalBtnClickListener(Context context, TextView placeName, TextView placeCategory, TextView placeAddress) {
        mContext = context;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
        this.placeAddress = placeAddress;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addPlaceBtn:
                Intent intent = new Intent(mContext, KotlinActivityMeetupPostPlace.class);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.addPlanBtn:
                // 실행 코드
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String name = data.getStringExtra("name"); //장소이름
            String categoryGroupCode = data.getStringExtra("category_group_code"); //장소카테고리
            String address = data.getStringExtra("address"); //장소 도로명주소

            LinearLayout placeLayout = ((Activity) mContext).findViewById(R.id.placeLayout);

            placeName.setText(name);
            placeCategory.setText(categoryGroupCode);
            placeAddress.setText(address);

            placeLayout.setVisibility(View.VISIBLE);

            //(안쓰는 activity)todo:선택된 장소 db 추가 - place 테이블에 정보 추가 & meet_up_post 테이블에 place_id 추가

        }
    }
}

