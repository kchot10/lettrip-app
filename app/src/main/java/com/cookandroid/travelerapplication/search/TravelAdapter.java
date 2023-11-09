package com.cookandroid.travelerapplication.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.chat.ChatRoom;
import com.cookandroid.travelerapplication.comment.CommentListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.DeleteData_Travel;
import com.cookandroid.travelerapplication.task.SelectData_ChatRoom;
import com.cookandroid.travelerapplication.task.UpdateData_Place;

import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {


    ArrayList<Travel> arrayList;
    Context context;
    String mUser_id, IP_ADDRESS;
    Boolean is_select;
    private AsyncTaskCompleteListener callback;

    public TravelAdapter(ArrayList<Travel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.context = mContext;
        this.is_select = false;
        FileHelper fileHelper = new FileHelper(context);
        mUser_id = fileHelper.readFromFile("user_id");
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
    }

    public TravelAdapter(ArrayList<Travel> arrayList, Context mContext, Boolean is_select, AsyncTaskCompleteListener callback) {
        this.arrayList = arrayList;
        this.context = mContext;
        this.is_select = is_select;
        this.callback = callback;
        FileHelper fileHelper = new FileHelper(context);
        mUser_id = fileHelper.readFromFile("user_id");
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
    }

    @NonNull
    @Override
    public TravelAdapter.TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_like_trip, parent, false);
        TravelViewHolder holder = new TravelViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapter.TravelViewHolder holder, int position) {
        holder.textView_city.setText(arrayList.get(position).getCity());
        holder.textView_places.setText((!arrayList.get(position).getPlaces().equals("null") ? arrayList.get(position).getPlaces():"장소 없음"));
        holder.textView_travel_theme.setText(arrayList.get(position).getTravel_theme());
        holder.textView_total_cost.setText(arrayList.get(position).getTotal_cost()+"원");
        if(arrayList.get(position).getUser_id().equals(mUser_id)){
            holder.button_delete_travel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder {
        TextView textView_travel_theme, textView_city, textView_total_cost, textView_places;
        ImageButton button_delete_travel;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_travel_theme = itemView.findViewById(R.id.tripTheme);
            this.textView_city = itemView.findViewById(R.id.tripCity);
            this.textView_total_cost = itemView.findViewById(R.id.tripCost);
            this.textView_places = itemView.findViewById(R.id.tripCourse);
            this.button_delete_travel = itemView.findViewById(R.id.imageButton);

            button_delete_travel.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("["+textView_city.getText().toString()+"] 여행을 정말로 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int curpos = getAbsoluteAdapterPosition();
                        DeleteData_Travel deleteData_travel = new DeleteData_Travel();
                        deleteData_travel.execute("http://" + IP_ADDRESS + "/0411/deletedata_travel.php", arrayList.get(curpos).getTravel_id());
                        new Handler().postDelayed(() -> {
                            arrayList.remove(curpos);
                            notifyItemRemoved(curpos);
                            notifyItemRangeChanged(curpos, arrayList.size());
                            UpdateData_Place updateData_place = new UpdateData_Place();
                            updateData_place.execute("http://"+IP_ADDRESS+"/0601/UpdateData_PlaceAll.php", "All");
                        }, 200);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            });

            itemView.setOnClickListener(v -> {
                int curpos = getAbsoluteAdapterPosition();
                if(is_select == true){
                    Log.e("errors", arrayList.get(curpos).toString()+arrayList.get(curpos).getTitle());
                    callback.onTaskComplete(arrayList.get(curpos));
                }else {
                    Intent intent;
                    intent = new Intent(context, RecordMainSearch.class);
                    intent.putExtra("travel_id", arrayList.get(curpos).getTravel_id());
                    intent.putExtra("city", arrayList.get(curpos).getCity());
                    intent.putExtra("total_cost", arrayList.get(curpos).getTotal_cost());
                    intent.putExtra("number_of_courses", arrayList.get(curpos).getNumber_of_courses());
                    intent.putExtra("travel_theme", arrayList.get(curpos).getTravel_theme());
                    context.startActivity(intent);
                }
            });
        }
    }
    public interface AsyncTaskCompleteListener {
        void onTaskComplete(Travel result);
    }
}
