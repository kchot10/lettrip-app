package com.cookandroid.travelerapplication.meetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;

import java.util.List;

public class PokeListAdapter extends RecyclerView.Adapter<PokeListAdapter.ViewHolder> {
    private List<PokeItem> items;

    public PokeListAdapter(List<PokeItem> items) {
        this.items = items;
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
        holder.userSex.setText(item.getUserSex());
        holder.meetupSuccessNum.setText(item.getMeetupSuccessNum());
        holder.meetupFailNum.setText(item.getMeetupFailNum());
        holder.oneLineReview.setText(item.getOneLineReview());
        holder.profilePhoto.setImageResource(item.getImageResource());

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
        public TextView userSex;
        public TextView userBirth; // 사용자 생년월일 TextView 추가
        public TextView meetupSuccessNum;
        public TextView meetupFailNum;
        public TextView oneLineReview;
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
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
