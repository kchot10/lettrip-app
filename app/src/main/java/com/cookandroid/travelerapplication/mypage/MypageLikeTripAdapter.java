package com.cookandroid.travelerapplication.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class MypageLikeTripAdapter extends RecyclerView.Adapter<MypageLikeTripAdapter.ViewHolder> {
    ArrayList<LikeTrip> items = new ArrayList<LikeTrip>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripCity;
        TextView tripCost;
        TextView tripType;

        RecyclerView recyclerView;

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

            recyclerView = itemView.findViewById(R.id.RecyclerView_course);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

            MypageLikeTripAdapter2 myadapter = new MypageLikeTripAdapter2();
            myadapter.addItem(new Place("팔공산 영화관")); //test data
            myadapter.addItem(new Place("2번째 장소")); //test data
            myadapter.addItem(new Place("3번째 장소")); //test data
            //db에서 장소list 받아와서 리사이클러뷰에 추가++
            recyclerView.setAdapter(myadapter);

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
    public void onBindViewHolder(@NonNull MypageLikeTripAdapter.ViewHolder holder, int position) {
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
