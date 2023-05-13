package com.cookandroid.travelerapplication.record;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.List;

public class RecordPlaceAdapter extends RecyclerView.Adapter<RecordPlaceAdapter.viewHolder>{
    private List<placeModel> activityList;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView placeName;
        public TextView cost;
        public TextView date;

        public viewHolder(@NonNull View itemView) {
            super(itemView);placeName = itemView.findViewById(R.id.placeText);
            cost = itemView.findViewById(R.id.costText);
            date = itemView.findViewById(R.id.dateText);
        }
    }


}
