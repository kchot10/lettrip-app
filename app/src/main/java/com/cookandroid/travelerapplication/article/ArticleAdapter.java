package com.cookandroid.travelerapplication.article;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.comment.CommentAdapter;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_ltem, parent, false);
        ArticleAdapter.BoardViewHolder holder = new ArticleAdapter.BoardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Article item = arrayList.get(position);
        holder.setItem(item);
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
        TextView textview_count_like;
        TextView board_comment;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textview_title = itemView.findViewById(R.id.textview_title);
            this.textview_author = itemView.findViewById(R.id.textview_author);
            this.textview_date_of_writing = itemView.findViewById(R.id.textview_date_of_writing);
            this.textview_count_view = itemView.findViewById(R.id.textview_count_view);
            this.textview_count_like = itemView.findViewById(R.id.board_comment);
            this.board_comment = itemView.findViewById(R.id.board_comment);

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
                intent.putExtra("image_url", arrayList.get(curpos).getImage_url());
                intent.putExtra("comment_number", arrayList.get(curpos).getComment_number());
                context.startActivity(intent);
            });

        }

        public void setItem(Article item){
            textview_title.setText(item.getTitle());
            textview_author.setText(item.getName());
            textview_date_of_writing.setText(item.getCreated_date());
            textview_count_view.setText(" " + item.getHit());
            textview_count_like.setText(" " + item.getLike_count());
            board_comment.setText(" "+item.getComment_number());
        }
    }


}
