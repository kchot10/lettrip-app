package com.cookandroid.travelerapplication.pokeInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import java.util.List;


public class OneLineReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>  {
    private List<OneLineReviewData> dataList;
    private Context context;

    public OneLineReviewAdapter(List<OneLineReviewData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_line_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        OneLineReviewData data = dataList.get(position);

        Glide.with(context)
                .load(data.getProfilePhoto())
                .placeholder(R.drawable.user)
                .into(holder.profilePhoto);
//        holder.profilePhoto.setImageResource();
        holder.oneLineReview.setText(data.getOneLineReview());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
