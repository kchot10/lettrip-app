package com.cookandroid.travelerapplication.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private ArrayList<Course> arrayList;
    private Context context;

    public CourseAdapter(ArrayList<Course> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_list, parent, false);
        CourseViewHolder holder = new CourseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        holder.textview_place_name.setText(arrayList.get(position).getPlace_name());
        holder.textview_arrived_time.setText(arrayList.get(position).getArrived_time());
        holder.textview_cost.setText(arrayList.get(position).getCost());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textview_place_name;
        TextView textview_arrived_time;
        TextView textview_cost;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textview_place_name = itemView.findViewById(R.id.textView_place_name);
            this.textview_arrived_time = itemView.findViewById(R.id.textView_arrived_time);
            this.textview_cost = itemView.findViewById(R.id.textView_cost);
        }
    }
}
