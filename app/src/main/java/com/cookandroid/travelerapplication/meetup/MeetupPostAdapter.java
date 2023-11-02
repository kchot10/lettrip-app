package com.cookandroid.travelerapplication.meetup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeetupPostAdapter extends RecyclerView.Adapter<MeetupPostAdapter.MeetupPostViewHolder> {
    private List<MeetupPost> meetupPostList;
    private Context context;

    public static class MeetupPostViewHolder extends RecyclerView.ViewHolder {
        public ImageView isGPSselected;
        public TextView cityName;
        public TextView date;
        public TextView meetupContent;
        public TextView nicknameTextView;
        public ImageView sexIcon;
        public de.hdodenhof.circleimageview.CircleImageView circle_iv;


        public MeetupPostViewHolder(View itemView) {
            super(itemView);
            isGPSselected = itemView.findViewById(R.id.isGPSselected);
            cityName = itemView.findViewById(R.id.cityName);
            date = itemView.findViewById(R.id.date);
            meetupContent = itemView.findViewById(R.id.content);
            nicknameTextView = itemView.findViewById(R.id.nickName);
            sexIcon = itemView.findViewById(R.id.sexIcon);
            circle_iv = itemView.findViewById(R.id.circle_iv);
        }
    }

    public MeetupPostAdapter(List<MeetupPost> meetupPostList, Context context) {
        this.meetupPostList = meetupPostList;
        this.context = context;
    }

    @Override
    public MeetupPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meetup_main, parent, false);
        return new MeetupPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeetupPostViewHolder holder, int position) {
        MeetupPost meetupPost = meetupPostList.get(position);

        try {
            String originalDate = meetupPost.getCreated_date();
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = originalFormat.parse(originalDate);
            holder.date.setText(targetFormat.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.isGPSselected.setImageResource(meetupPost.getIs_gps_enabled().equals("1") ? R.drawable.meetup_post_gps_icon : R.drawable.meetup_post_nongps_icon);
        holder.cityName.setText(meetupPost.getCity());

        holder.meetupContent.setText(meetupPost.getContent());
        holder.nicknameTextView.setText(meetupPost.getNickname());
        String sex = meetupPost.getSex();
        if(sex.isEmpty() || sex == null || sex.equals("")){
            Glide.with(context)
                    .load(android.R.color.transparent)
                    .into(holder.sexIcon);
        } else if(sex.equals("MALE")){
            Glide.with(context)
                    .load(R.drawable.man_icon)
                    .into(holder.sexIcon);
        } else if(sex.equals("FEMALE")){
            Glide.with(context)
                    .load(R.drawable.woman_icon)
                    .into(holder.sexIcon);
        }
        String image_url = meetupPost.getImage_url();
        if(!(image_url == null || image_url.equals("null") || image_url.isEmpty() || image_url.equals(""))) {
            Glide.with(context)
                    .load(image_url)
                    .into(holder.circle_iv);
        }

        // 유저의 닉네임 가져오기

    }

    @Override
    public int getItemCount() {
        return meetupPostList.size();
    }
}
