package com.cookandroid.travelerapplication.pokeInfo;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cookandroid.travelerapplication.R;

import java.net.URI;
import java.util.List;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ViewHolder> {
    private List<String> itemList;

    public ReviewItemAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_line_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = itemList.get(position);
        //holder.itemTextView.setText(item);

        String imageURL = "https://example.com/profile.jpg";
        Uri profileURI = Uri.parse(imageURL);
        String oneLineReview_text = "한줄리뷰 내용 초기화";

        //todo:db에서 프로필 사진과 한줄리뷰 텍스트 가져오기

        holder.profilePhoto.setImageURI(profileURI);
        holder.oneLineReivew.setText(oneLineReview_text);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePhoto;
        TextView oneLineReivew;

        public ViewHolder(View view) {
            super(view);
            //itemTextView = view.findViewById(R.id.itemTextView);
            profilePhoto = view.findViewById(R.id.profilePhoto);
            oneLineReivew = view.findViewById(R.id.oneLineReivew);
        }
    }
}
