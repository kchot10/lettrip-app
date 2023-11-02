package com.cookandroid.travelerapplication.pokeInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cookandroid.travelerapplication.R;

public class OneLineReivewTab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_one_line_review_viewpager, container, false);
        return view;
    }

}
