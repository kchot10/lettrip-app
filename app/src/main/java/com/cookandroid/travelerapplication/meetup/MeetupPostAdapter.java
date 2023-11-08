package com.cookandroid.travelerapplication.meetup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.chat.ChatRoomActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MeetupPostAdapter extends RecyclerView.Adapter<MeetupPostAdapter.MeetupPostViewHolder> {
    private List<MeetupPost> meetupPostList;
    private Context context;
    private String selectedCityName;



    public class MeetupPostViewHolder extends RecyclerView.ViewHolder {
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
//
//            itemView.setOnClickListener(v -> {
//                int curpos = getAbsoluteAdapterPosition();
//                Intent intent;
//                intent = new Intent(context, PokeListActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("meet_up_post_id", meetupPostList.get(curpos).getMeet_up_post_id());
//                context.startActivity(intent);
//            });

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, MeetupPostDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("meetup_post", meetupPostList.get(curpos)); // MeetupPost 클래스 형태로 보내는건 오류
                context.startActivity(intent);
            });
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

        } else{
            holder.circle_iv.setImageResource(R.drawable.profile_photo_mypage);

        }

        //city 필터링
        if (selectedCityName != null && !selectedCityName.isEmpty() && !meetupPost.getCity().equals(selectedCityName)) {
            holder.itemView.setVisibility(View.GONE); // 선택된 도시와 일치하지 않는 아이템을 숨김
            return; // 선택된 도시와 일치하지 않으면 더 이상 코드를 실행하지 않음
        }

    }

    @Override
    public int getItemCount() {
        return meetupPostList.size();
    }
}
