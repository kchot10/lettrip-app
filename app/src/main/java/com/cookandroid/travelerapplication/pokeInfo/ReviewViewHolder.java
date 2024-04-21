package com.cookandroid.travelerapplication.pokeInfo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.text.BreakIterator;

public class ReviewViewHolder  extends RecyclerView.ViewHolder {
    TextView userNickName;
    ImageView profilePhoto;
    TextView oneLineReview;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        profilePhoto = itemView.findViewById(R.id.profilePhoto);
        oneLineReview = itemView.findViewById(R.id.oneLineReivew);
        oneLineReview = itemView.findViewById(R.id.userNickName);

    }
}
