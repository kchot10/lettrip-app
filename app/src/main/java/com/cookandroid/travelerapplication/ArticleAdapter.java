package com.cookandroid.travelerapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.BoardViewHolder> {


    private ArrayList<Article> arrayList;
    private Context context;

    public ArticleAdapter(ArrayList<Article> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_list, parent, false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.textview_title.setText(arrayList.get(position).getTitle());
        holder.textview_author.setText(arrayList.get(position).getUser_id());
        holder.textview_date_of_writing.setText(arrayList.get(position).getCreated_date());
        holder.textview_count_view.setText(arrayList.get(position).getHit());
        holder.textview_content.setText(arrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView textview_title;
        TextView textview_author;
        TextView textview_date_of_writing;
        TextView textview_content;
        TextView textview_count_view;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textview_title = itemView.findViewById(R.id.textview_title);
            this.textview_author = itemView.findViewById(R.id.textview_author);
            this.textview_date_of_writing = itemView.findViewById(R.id.textview_date_of_writing);
            this.textview_content = itemView.findViewById(R.id.textview_content);
            this.textview_count_view = itemView.findViewById(R.id.textview_count_view);

        }
    }
}
