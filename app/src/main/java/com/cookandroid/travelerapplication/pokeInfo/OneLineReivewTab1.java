package com.cookandroid.travelerapplication.pokeInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import java.util.List;

public class OneLineReivewTab1 extends Fragment {

    private RecyclerView recyclerView;
    private List<String> itemList;
    private ReviewItemAdapter adapter;
    public static OneLineReivewTab1 newInstance(int selectedTab) {
        OneLineReivewTab1 fragment = new OneLineReivewTab1();
        Bundle args = new Bundle();
        args.putInt("selectedTab", selectedTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_one_line_review_success, container, false);

        // 리사이클러뷰 초기화
        recyclerView = view.findViewById(R.id.recycler_view);
        itemList = new ArrayList<>();
        // itemList에 데이터 추가
        itemList.add("아이템 1");
        itemList.add("아이템 2");

        // 어댑터 설정
        adapter = new ReviewItemAdapter(itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

}
