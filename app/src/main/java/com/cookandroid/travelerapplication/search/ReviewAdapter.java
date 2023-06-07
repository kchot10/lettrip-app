package com.cookandroid.travelerapplication.search;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import java.util.ArrayList;

import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.search.CourseActivitySearch;
import com.cookandroid.travelerapplication.search.RecordMainSearch;
import com.cookandroid.travelerapplication.search.Review;
import com.cookandroid.travelerapplication.task.DeleteData_Course;
import com.cookandroid.travelerapplication.task.DeleteData_Travel;

import android.widget.ImageView;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> arrayList;
    private Context context;
    private String mUser_id, IP_ADDRESS;

    public ReviewAdapter(ArrayList<Review> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        FileHelper fileHelper = new FileHelper(context);
        mUser_id = fileHelper.readFromFile("user_id");
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list_temp, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImage_url())
                .placeholder(R.drawable.user)
                .into(holder.imageView_profile_review);
        Glide.with(holder.itemView.getContext())
                .load(arrayList.get(position).getStored_file_url())
                .placeholder(R.drawable.logo)
                .into(holder.imageView_stored_file_url_review);
        holder.textView_cost_review.setText(arrayList.get(position).getCost());
        holder.textView_detailReview_review.setText(arrayList.get(position).getDetailed_review());
        holder.textView_createdDate_review.setText(arrayList.get(position).getFormatted_created_date());
        holder.textView_nickName_review.setText(arrayList.get(position).getNickname());
        if (arrayList.get(position).getSolo_friendly_rating().equals("0")){
            holder.textView_soloFriendly_review.setVisibility(View.INVISIBLE);
        }
        holder.textView_visitTimes_review.setText(arrayList.get(position).getVisit_times());
        holder.ratingBar_review.setRating(Float.parseFloat(arrayList.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_profile_review, imageView_stored_file_url_review;
        TextView textView_nickName_review, textView_createdDate_review,
                textView_detailReview_review, textView_visitTimes_review,
                textView_cost_review, textView_soloFriendly_review;
        RatingBar ratingBar_review;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView_profile_review = itemView.findViewById(R.id.imageView_profile_review);
            this.imageView_stored_file_url_review = itemView.findViewById(R.id.imageView_stored_file_url_review);
            this.textView_nickName_review = itemView.findViewById(R.id.textView_nickName_review);
            this.textView_createdDate_review = itemView.findViewById(R.id.textView_createdDate_review);
            this.textView_detailReview_review = itemView.findViewById(R.id.textView_detailReview_review);
            this.textView_visitTimes_review = itemView.findViewById(R.id.textView_visitTimes_review);
            this.textView_cost_review = itemView.findViewById(R.id.textView_cost_review);
            this.textView_soloFriendly_review = itemView.findViewById(R.id.textView_soloFriendly_review);
            this.ratingBar_review = itemView.findViewById(R.id.ratingBar_review);

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, CourseActivitySearch.class);
                intent.putExtra("travel_id", arrayList.get(curpos).getTravel_id());
                intent.putExtra("place_id", arrayList.get(curpos).getPlace_id());
                intent.putExtra("rating", arrayList.get(curpos).getRating());
                intent.putExtra("user_id", arrayList.get(curpos).getUser_id());
                intent.putExtra("review_id", arrayList.get(curpos).getReview_id());
                intent.putExtra("arrived_time", arrayList.get(curpos).getArrived_time());
                intent.putExtra("cost", arrayList.get(curpos).getCost());
                intent.putExtra("place_name", arrayList.get(curpos).getPlace_name());
                intent.putExtra("category_name", arrayList.get(curpos).getCategory_name());
                intent.putExtra("detailed_review", arrayList.get(curpos).getDetailed_review());
                intent.putExtra("arrived_time_real", arrayList.get(curpos).getArrived_time_real());

                context.startActivity(intent);
            });
        }
    }
}
