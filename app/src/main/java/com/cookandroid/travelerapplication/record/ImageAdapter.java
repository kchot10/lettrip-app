package com.cookandroid.travelerapplication.record;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<ImageReview> arrayList;
    private Context context;

    public ImageAdapter(ArrayList<ImageReview> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_list, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//         이미지 로드 및 표시
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImageUrl())
                .into(holder.imageView_review);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_review;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView_review = itemView.findViewById(R.id.imageView_review);
        }
    }
}