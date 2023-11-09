package com.cookandroid.travelerapplication.pokeInfo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_MeetUp_Review;

import java.util.ArrayList;
import java.util.List;

public class Fragment_fail extends Fragment implements SelectData_MeetUp_Review.AsyncTaskCompleteListener{

    private RecyclerView recyclerView;
    private FileHelper fileHelper;
    List<OneLineReviewData> dataList;
    ArrayList<MeetUpReview> result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fileHelper = new FileHelper(getContext());
        String IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        String user_id = fileHelper.readFromFile("user_id");

        View view = inflater.inflate(R.layout.item_one_line_review_success, container, false);
        // 리싸이클러뷰 초기화
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 데이터 리스트 생성 (예시)
        dataList = new ArrayList<>();
        //dataList.add(new OneLineReviewData(R.drawable.profile_photo_mypage, "시간 약속을 안지켜요.."));
        //dataList.add(new OneLineReviewData(R.drawable.profile_photo_mypage, "마음대로 약속 취소해버림 ㅜ"));


        String partner_user_id = fileHelper.readFromFile("partner_user_id");

        Boolean isComleted = false;
        SelectData_MeetUp_Review selectData_meetUp_review = new SelectData_MeetUp_Review(this);
        selectData_meetUp_review.execute("http://" + IP_ADDRESS + "/1107/SelectData_MeetUp_Review.php",partner_user_id,isComleted.toString());


        result= new ArrayList<>();
        new Handler().postDelayed(()->{
            // 어댑터 생성 및 리싸이클러뷰에 설정
            result.forEach(meetUpReview -> {
                dataList.add(new OneLineReviewData(meetUpReview.getImage_url(), meetUpReview.getContent()));
            });
            OneLineReviewAdapter adapter = new OneLineReviewAdapter(dataList, getActivity());
            recyclerView.setAdapter(adapter);


        },500);
        return view;
    }

    @Override
    public void onTaskComplete(ArrayList<MeetUpReview> result) {
        this.result = result;
    }
}
