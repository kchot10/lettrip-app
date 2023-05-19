package com.cookandroid.travelerapplication.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {


    ArrayList<Travel> arrayList;
    Context mContext;

    public TravelAdapter(ArrayList<Travel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TravelAdapter.TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_list, parent, false);
        TravelViewHolder holder = new TravelViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.TravelViewHolder holder, int position) {
        holder.textView_city.setText(arrayList.get(position).getCity());
        holder.textView_places.setText(arrayList.get(position).getPlaces());
        holder.textView_travel_theme.setText(arrayList.get(position).getTravel_theme());
        holder.textView_total_cost.setText(arrayList.get(position).getTotal_cost());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder {
        TextView textView_travel_theme, textView_city, textView_total_cost, textView_places;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_travel_theme = itemView.findViewById(R.id.textView_travel_theme);
            this.textView_city = itemView.findViewById(R.id.textView_city);
            this.textView_total_cost = itemView.findViewById(R.id.textView_total_cost);
            this.textView_places = itemView.findViewById(R.id.textView_places);
        }
    }
}
