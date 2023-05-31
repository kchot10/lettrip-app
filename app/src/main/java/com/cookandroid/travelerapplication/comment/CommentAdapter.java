package com.cookandroid.travelerapplication.comment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.DeleteData_Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final String BASEID = "-1";
    private final String COUNTZERO = "0";

    private ArrayList<Comment> arrayList;
    private Context context;
    private String user_id;

    public CommentAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        FileHelper fileHelper = new FileHelper(context);
        this.user_id = fileHelper.readFromFile("user_id");
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment, parent, false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(context)
                .load(arrayList.get(position).getImage_url())
                .placeholder(R.drawable.user)
                .into(holder.profilePhoto);
        // @tools:sample/avatars[0]
        String mentioned_user_name = arrayList.get(position).getMentioned_user_name();
        if (!mentioned_user_name.equals("null")) {
            holder.textview_mentioned_user_name.setText("@"+mentioned_user_name+" ");
        }else {
            holder.textview_mentioned_user_name.setText("");
        }
        holder.textview_content.setText(arrayList.get(position).getContent());
        holder.textview_user_id.setText(arrayList.get(position).getName());
        holder.textview_created_date.setText(arrayList.get(position).getCreated_date());
        if (user_id.trim().equals(arrayList.get(position).getUser_id())){
            holder.button_delete.setVisibility(View.VISIBLE);
        }
        if (!arrayList.get(position).getComment_count().equals(COUNTZERO)) {
            holder.comment_number.setText("답글 " + arrayList.get(position).getComment_count() + "개");
        } else {
            holder.comment_number.setHeight(0);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);

    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePhoto;
        TextView textview_mentioned_user_name;
        TextView textview_content;
        TextView textview_user_id;
        TextView textview_created_date;
        TextView comment_number;
        Button button_delete;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profilePhoto = itemView.findViewById(R.id.profilePhoto);
            this.textview_mentioned_user_name = itemView.findViewById(R.id.textview_mentioned_user_name);
            this.textview_content = itemView.findViewById(R.id.textview_content);
            this.textview_user_id = itemView.findViewById(R.id.textview_user_id);
            this.textview_created_date = itemView.findViewById(R.id.textview_created_date);
            comment_number = itemView.findViewById(R.id.comment_number);
            this.button_delete = itemView.findViewById(R.id.button_delete);
            FileHelper fileHelper = new FileHelper(context);

            button_delete.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                String IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
                DeleteData_Comment task = new DeleteData_Comment();
                task.execute("http://"+IP_ADDRESS+"/0411/deletedata_comment.php",arrayList.get(curpos).getComment_id());
                arrayList.remove(curpos);
                notifyItemRemoved(curpos);
                notifyItemRangeChanged(curpos, arrayList.size());
            });

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                Log.d("youn", ":"+curpos);
                Intent intent;
                intent = new Intent(context, CommentListActivity.class);

                if (!arrayList.get(curpos).getParent_comment_id().equals(BASEID)){
                    ((CommentListActivity) v.getContext()).setEditText(arrayList.get(curpos).getName(), arrayList.get(curpos).getUser_id()); //오류 발생
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
                intent.putExtra("image_url", arrayList.get(curpos).getImage_url());

                context.startActivity(intent);
            });



        }
    }
}
