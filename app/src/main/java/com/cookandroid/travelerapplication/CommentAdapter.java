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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> arrayList;
    private Context context;

    public CommentAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.textview_content.setText(arrayList.get(position).getContent());
        holder.textview_user_id.setText(arrayList.get(position).getName());
        holder.textview_created_date.setText(arrayList.get(position).getCreated_date());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);

    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textview_content;
        TextView textview_user_id;
        TextView textview_created_date;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textview_content = itemView.findViewById(R.id.textview_content);
            this.textview_user_id = itemView.findViewById(R.id.textview_user_id);
            this.textview_created_date = itemView.findViewById(R.id.textview_created_date);

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Intent intent;
                intent = new Intent(context, CommentListActivity.class);

                if (!arrayList.get(curpos).getParent_comment_id().equals("0")){
                    ((CommentListActivity) v.getContext()).setEditText(arrayList.get(curpos).getUser_id());
                    return;
                }
                intent.putExtra("comment_id", arrayList.get(curpos).getComment_id());
                intent.putExtra("created_date", arrayList.get(curpos).getCreated_date());
                intent.putExtra("modified_date", arrayList.get(curpos).getModified_date());
                intent.putExtra("content", arrayList.get(curpos).getContent());
                intent.putExtra("article_id", arrayList.get(curpos).getArticle_id());
                intent.putExtra("mentioned_user_id", arrayList.get(curpos).getMentioned_user_id());
                intent.putExtra("parent_comment_id", arrayList.get(curpos).getParent_comment_id());
                intent.putExtra("user_id", arrayList.get(curpos).getUser_id());
                intent.putExtra("name", arrayList.get(curpos).getName());
                context.startActivity(intent);
            });



        }
    }
}
