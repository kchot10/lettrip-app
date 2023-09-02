package com.cookandroid.travelerapplication.recommend;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class PlaceScoreAdapter extends RecyclerView.Adapter<PlaceScoreAdapter.PlaceScoreViewHolder> {
    private ArrayList<PlaceScore> arrayList;
    private Context context;

    public PlaceScoreAdapter(ArrayList<PlaceScore> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaceScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_score_list, parent, false);
        PlaceScoreViewHolder holder = new PlaceScoreViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceScoreViewHolder holder, int position) {
        holder.placeName.setText(arrayList.get(position).getPlaceName());
        holder.place_score.setText(arrayList.get(position).getScore());
        holder.place_rating.setText(arrayList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PlaceScoreViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, place_score, place_rating;
        public PlaceScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.textView_recommend_placeName);
            place_score = itemView.findViewById(R.id.textView_recommend_score);
            place_rating = itemView.findViewById(R.id.textView_recommend_rating);
        }
    }
}