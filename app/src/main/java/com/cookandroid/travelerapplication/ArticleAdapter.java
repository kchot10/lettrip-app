package com.cookandroid.travelerapplication;

import android.content.Context;
import android.content.Intent;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_list, parent, false);
        BoardViewHolder holder = new BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.textview_title.setText(arrayList.get(position).getTitle());
        holder.textview_author.setText(arrayList.get(position).getName());
        holder.textview_date_of_writing.setText(arrayList.get(position).getCreated_date());
        holder.textview_count_view.setText(arrayList.get(position).getHit());

    }

    @Override
    public int getItemCount() {

        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView textview_title;
        TextView textview_author;
        TextView textview_date_of_writing;
        TextView textview_count_view;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textview_title = itemView.findViewById(R.id.textview_title);
            this.textview_author = itemView.findViewById(R.id.textview_author);
            this.textview_date_of_writing = itemView.findViewById(R.id.textview_date_of_writing);
            this.textview_count_view = itemView.findViewById(R.id.textview_count_view);

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, ArticleContentActivity.class);
                intent.putExtra("article_id", arrayList.get(curpos).getArticle_id());
                intent.putExtra("created_date", arrayList.get(curpos).getCreated_date());
                intent.putExtra("modified_date", arrayList.get(curpos).getModified_date());
                intent.putExtra("content", arrayList.get(curpos).getContent());
                intent.putExtra("hit", arrayList.get(curpos).getHit());
                intent.putExtra("like_count", arrayList.get(curpos).getLike_count());
                intent.putExtra("title", arrayList.get(curpos).getTitle());
                intent.putExtra("user_id", arrayList.get(curpos).getUser_id());
                intent.putExtra("name", arrayList.get(curpos).getName());
                context.startActivity(intent);
            });

        }
    }
}
