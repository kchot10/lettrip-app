package com.cookandroid.travelerapplication.meetup;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divWidth;

    public RecyclerViewDecoration(int divWidth) {
        this.divWidth = divWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int column = itemPosition % 2;

        // 왼쪽 열의 아이템들에게 오른쪽 마진 추가
        if (column == 0) {
            outRect.right = divWidth;
        }
    }
}
