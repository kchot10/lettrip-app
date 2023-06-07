package com.cookandroid.travelerapplication.mission;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class MyMissionAdapter extends RecyclerView.Adapter<MyMissionAdapter.MyMissionViewHolder> {
    private ArrayList<MyMission> arrayList;
    private Context context;

    public MyMissionAdapter(ArrayList<MyMission> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyMissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_mission_list_temp, parent, false);
        MyMissionViewHolder holder = new MyMissionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMissionViewHolder holder, int position) {
        holder.textView_numbering.setText(Integer.toString(position+1));
        holder.textView_mission_type.setText(arrayList.get(position).getMission_type());
        holder.textView_accomplished_date.setText(arrayList.get(position).getAccomplished_date());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class MyMissionViewHolder extends RecyclerView.ViewHolder {
        TextView textView_accomplished_date, textView_mission_type, textView_numbering;
        public MyMissionViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_numbering = itemView.findViewById(R.id.textView_numbering);
            textView_accomplished_date = itemView.findViewById(R.id.textView_accomplished_date);
            textView_mission_type = itemView.findViewById(R.id.textView_mission_type);
        }
    }
}