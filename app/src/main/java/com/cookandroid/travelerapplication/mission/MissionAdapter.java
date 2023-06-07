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

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {
    private ArrayList<Mission> arrayList;
    private Context context;

    public MissionAdapter(ArrayList<Mission> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission_list, parent, false);
        MissionViewHolder holder = new MissionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MissionViewHolder holder, int position) {
        holder.rankNum.setText(Integer.toString(position+1));
        holder.rankNickname.setText(arrayList.get(position).getNickname());
        holder.rankCount.setText(arrayList.get(position).getCount());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class MissionViewHolder extends RecyclerView.ViewHolder {
        TextView rankNum, rankNickname, rankCount;
        public MissionViewHolder(@NonNull View itemView) {
            super(itemView);
            rankNum = itemView.findViewById(R.id.rankNum);
            rankNickname = itemView.findViewById(R.id.rankNickname);
            rankCount = itemView.findViewById(R.id.rankCount);
        }
    }
}