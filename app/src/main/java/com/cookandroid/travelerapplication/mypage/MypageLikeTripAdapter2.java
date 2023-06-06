package com.cookandroid.travelerapplication.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MypageLikeTripAdapter2 extends RecyclerView.Adapter<MypageLikeTripAdapter2.ViewHolder>{
    ArrayList<Place> items = new ArrayList<Place>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeName;
        TextView minusSign;

        public ViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.placeName);
            minusSign = itemView.findViewById(R.id.minusSign);

        }
        public void setItem(Place item){
            placeName.setText(item.getPlaceName());

        }
    }

    @NonNull
    @Override
    public MypageLikeTripAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_mypage_like_trip_course, parent, false);

        return new MypageLikeTripAdapter2.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MypageLikeTripAdapter2.ViewHolder holder, int position) {
        Place item = items.get(position);
        holder.setItem(item);

        //가장 마지막 아이템이면 - 안보이게 처리
        if(position == getItemCount() - 1){
            holder.minusSign.setVisibility(View.GONE);
        } else{
            holder.minusSign.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Place> getItems() {
        return items;
    }

    public void setItems(ArrayList<Place> items) {
        this.items = items;
    }
    public void addItem(Place item){
        items.add(item);
    }

}
