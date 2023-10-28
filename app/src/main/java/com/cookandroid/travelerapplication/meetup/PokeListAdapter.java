package com.cookandroid.travelerapplication.meetup;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;

import java.util.List;

public class PokeListAdapter extends RecyclerView.Adapter<PokeListAdapter.ViewHolder> {
    private List<PokeItem> items;
    private Context context;

    public PokeListAdapter(List<PokeItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poke, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PokeItem item = items.get(position);
        holder.userName.setText(item.getUserName());
        holder.meetupSuccessNum.setText("매칭 성공 "+item.getMeetupSuccessNum()+"회");
        holder.meetupFailNum.setText("매칭 실패 "+item.getMeetupFailNum()+"회");
        holder.oneLineReview.setText(item.getOneLineReview());

        String birthDate = item.getBirthDate();
        if(!birthDate.equals("null")){
            holder.brithDate.setText(birthDate);
        }

        String sex = item.getUserSex();
        if(sex.isEmpty() || sex == null || sex.equals("")){

        } else if(sex.equals("male")){
            Glide.with(context)
                    .load(R.drawable.man_icon)
                    .into(holder.userSex);
        } else if(sex.equals("female")){
            Glide.with(context)
                    .load(R.drawable.woman_icon)
                    .into(holder.userSex);
        }

        String image_url = item.getImageResource();
        if(!(image_url == null || image_url.equals("null") || image_url.isEmpty() || image_url.equals(""))) {
            Glide.with(context)
                    .load(image_url)
                    .into(holder.profilePhoto);
        }

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭 이벤트 처리 - 채팅방 만들어지도록 코드 추가!!**
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePhoto;
        public TextView userName;
        public ImageView userSex;
        public TextView userBirth; // 사용자 생년월일 TextView 추가
        public TextView meetupSuccessNum;
        public TextView meetupFailNum;
        public TextView oneLineReview;
        public TextView brithDate;
        public Button acceptBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profilePhoto);
            userName = itemView.findViewById(R.id.userName);
            userSex = itemView.findViewById(R.id.userSex);
            userBirth = itemView.findViewById(R.id.userBirth); // 추가된 부분
            meetupSuccessNum = itemView.findViewById(R.id.meetupSuccessNum);
            meetupFailNum = itemView.findViewById(R.id.meetupFailNum);
            oneLineReview = itemView.findViewById(R.id.oneLineReview);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            brithDate = itemView.findViewById(R.id.userBirth);
            profilePhoto.setClipToOutline(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
