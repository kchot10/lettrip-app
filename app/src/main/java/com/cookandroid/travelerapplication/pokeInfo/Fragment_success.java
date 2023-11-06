package com.cookandroid.travelerapplication.pokeInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_success extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_one_line_review_success, container, false);
        // 리싸이클러뷰 초기화
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 데이터 리스트 생성 (예시)
        List<OneLineReviewData> dataList = new ArrayList<>();
        dataList.add(new OneLineReviewData(R.drawable.profile_photo_mypage, "약속 시간을 잘 지켜요!"));
        dataList.add(new OneLineReviewData(R.drawable.profile_photo_mypage, "친절하게 대해 주셔서 감사해요!"));
        //todo:매칭 성공 한줄리뷰 불러와서 리스트에 추가(프로필, 한줄리뷰 내용)

        // 어댑터 생성 및 리싸이클러뷰에 설정
        OneLineReviewAdapter adapter = new OneLineReviewAdapter(dataList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
