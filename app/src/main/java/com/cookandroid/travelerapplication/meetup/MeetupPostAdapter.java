package com.cookandroid.travelerapplication.meetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.List;

public class MeetupPostAdapter extends RecyclerView.Adapter<MeetupPostAdapter.MeetupPostViewHolder> {
    private List<MeetupPost> meetupPostList;

    public static class MeetupPostViewHolder extends RecyclerView.ViewHolder {
        public ImageView isGPSselected;
        public TextView cityName;
        public TextView date;
        public TextView meetupContent;
        public TextView nicknameTextView;


        public MeetupPostViewHolder(View itemView) {
            super(itemView);
            isGPSselected = itemView.findViewById(R.id.isGPSselected);
            cityName = itemView.findViewById(R.id.cityName);
            date = itemView.findViewById(R.id.date);
            meetupContent = itemView.findViewById(R.id.content);
            nicknameTextView = itemView.findViewById(R.id.nickName);
        }
    }

    public MeetupPostAdapter(List<MeetupPost> meetupPostList) {
        this.meetupPostList = meetupPostList;
    }

    @Override
    public MeetupPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meetup_main, parent, false);
        return new MeetupPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeetupPostViewHolder holder, int position) {
        MeetupPost meetupPost = meetupPostList.get(position);

        holder.isGPSselected.setImageResource(meetupPost.isGPSEnabled() ? R.drawable.meetup_post_gps_icon : R.drawable.meetup_post_nongps_icon);
        holder.cityName.setText(meetupPost.getCityName());
        holder.date.setText(meetupPost.getDate());
        holder.meetupContent.setText(meetupPost.getContent());
        // 유저의 닉네임 가져오기

    }

    @Override
    public int getItemCount() {
        return meetupPostList.size();
    }
}
