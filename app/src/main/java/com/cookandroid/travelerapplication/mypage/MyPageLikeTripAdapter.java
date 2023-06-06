package com.cookandroid.travelerapplication.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class MyPageLikeTripAdapter extends RecyclerView.Adapter<MyPageLikeTripAdapter.ViewHolder> {
    ArrayList<LikeTrip> items = new ArrayList<LikeTrip>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripCity;
        TextView tripCost;
        TextView tripType;

        public ViewHolder(View itemView) {
            super(itemView);

            tripCity = itemView.findViewById(R.id.tripCity);
            tripCost = itemView.findViewById(R.id.tripCost);
            tripType = itemView.findViewById(R.id.tripType);
        }
        public void setItem(LikeTrip item){
            tripCity.setText(item.getCity());
            tripCost.setText(item.getCost());
            tripType.setText(item.getTripType());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_mypage_like_trip, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageLikeTripAdapter.ViewHolder holder, int position) {
        LikeTrip item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LikeTrip item){
        items.add(item);
    }

    public void setItems(ArrayList<LikeTrip> items){
        this.items = items;
    }

    public LikeTrip getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, LikeTrip item){
        items.set(position, item);
    }
}
