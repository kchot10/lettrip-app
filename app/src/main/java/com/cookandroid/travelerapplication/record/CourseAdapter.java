package com.cookandroid.travelerapplication.record;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import com.cookandroid.travelerapplication.search.CourseActivitySearch;
import com.cookandroid.travelerapplication.search.RecordMainSearch;

import android.widget.ImageView;


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_course, parent, false);
        CourseViewHolder holder = new CourseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).getStored_file_url())
                .into(holder.placePhoto);
        holder.textview_place_name.setText(arrayList.get(position).getPlace_name());
        holder.textview_arrived_time.setText(arrayList.get(position).getArrived_time());
        holder.textview_cost.setText(arrayList.get(position).getCost()+"원");
        holder.textview_daycount.setText(arrayList.get(position).getDay_count()+"일차");
        holder.ratingBar_rating.setRating(Integer.parseInt(arrayList.get(position).getRating()));

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView placePhoto;
        TextView textview_place_name;
        TextView textview_arrived_time;
        TextView textview_cost;
        TextView textview_daycount;
        RatingBar ratingBar_rating;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.placePhoto = itemView.findViewById(R.id.placeImageView);
            this.textview_place_name = itemView.findViewById(R.id.cityText2);
            this.textview_arrived_time = itemView.findViewById(R.id.timeText);
            this.textview_cost = itemView.findViewById(R.id.costText);
            this.textview_daycount = itemView.findViewById(R.id.textView_daycount);
            this.ratingBar_rating = itemView.findViewById(R.id.ratingBar_rating);

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, CourseActivitySearch.class);
                intent.putExtra("review_id", arrayList.get(curpos).getReview_id());
                intent.putExtra("arrived_time", arrayList.get(curpos).getArrived_time());
                intent.putExtra("cost", arrayList.get(curpos).getCost());
                intent.putExtra("place_name", arrayList.get(curpos).getPlace_name());
                intent.putExtra("category_name", arrayList.get(curpos).getCategory_name());

                context.startActivity(intent);
            });
        }
    }
}
