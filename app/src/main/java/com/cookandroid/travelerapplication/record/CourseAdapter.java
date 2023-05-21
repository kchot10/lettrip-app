package com.cookandroid.travelerapplication.record;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;
import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_list, parent, false);
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
        holder.textview_cost.setText(arrayList.get(position).getCost());

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
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.placePhoto = itemView.findViewById(R.id.placePhoto);
            this.textview_place_name = itemView.findViewById(R.id.textView_place_name);
            this.textview_arrived_time = itemView.findViewById(R.id.textView_arrived_time);
            this.textview_cost = itemView.findViewById(R.id.textView_cost);

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, CourseActivitySearch.class);
                intent.putExtra("review_id", arrayList.get(curpos).getReview_id());
//                intent.putExtra("comment_id", arrayList.get(curpos).getComment_id());
//                intent.putExtra("created_date", arrayList.get(curpos).getCreated_date());
//                intent.putExtra("modified_date", arrayList.get(curpos).getModified_date());
//                intent.putExtra("content", arrayList.get(curpos).getContent());
//                intent.putExtra("article_id", arrayList.get(curpos).getArticle_id());
//                intent.putExtra("mentioned_user_id", arrayList.get(curpos).getMentioned_user_id());
//                intent.putExtra("parent_comment_id", arrayList.get(curpos).getParent_comment_id());
//                intent.putExtra("user_id", arrayList.get(curpos).getUser_id());
//                intent.putExtra("name", arrayList.get(curpos).getName());
                context.startActivity(intent);
            });
        }
    }
}
