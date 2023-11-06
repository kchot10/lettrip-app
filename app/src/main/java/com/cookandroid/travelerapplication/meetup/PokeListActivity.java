package com.cookandroid.travelerapplication.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.article.ArticleAdapter;
import com.cookandroid.travelerapplication.chat.ChatRoomActivity;
import com.cookandroid.travelerapplication.comment.Comment;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.task.SelectData_Article;
import com.cookandroid.travelerapplication.task.SelectData_Article2;
import com.cookandroid.travelerapplication.task.SelectData_Poke;

import java.util.ArrayList;
import java.util.List;

public class PokeListActivity extends AppCompatActivity implements SelectData_Poke.AsyncTaskCompleteListener {
    FileHelper fileHelper;
    String IP_ADDRESS, user_id;
    ImageButton backBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
//        user_id = fileHelper.readFromFile("user_id");

        recyclerView = findViewById(R.id.RecyclerView_poke);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                // 이전 화면으로 돌아가기 위해 이전 액티비티를 호출
                Intent intent = new Intent(context, MeetupPostDetailActivity.class);
                context.startActivity(intent);

                // 현재 액티비티를 종료
                ((Activity) context).finish();
            }
        });
    }


    public void Refresh() {
//        if (TYPE == NEW) {
//            articleArrayList = new ArrayList<>();
//            SelectData_Article2 task = new SelectData_Article2(articleArrayList);
//            task.execute("http://" + IP_ADDRESS + "/0422/selectdata_article2.php", article_type);
//        }
//        itemList.add(new PokeItem("박성화 (떵)", "남자", "매칭 완료 2회", "매칭 실패 0회", "한 줄 리뷰 입니다", R.drawable.meetup_profile_round));

        String meet_up_post_id = getIntent().getStringExtra("meet_up_post_id"); //Todo: MeetUpPostActivity에서 받아와서 getIntent를 통해 불러오기

        ArrayList<PokeItem> pokeItemArrayList = new ArrayList<>();
        SelectData_Poke task = new SelectData_Poke(pokeItemArrayList, this);
        task.execute("http://" + IP_ADDRESS + "/1028/SelectData_Poke.php", meet_up_post_id);

    }
    @Override
    public void onTaskComplete(ArrayList<PokeItem> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    TextView pokeTitle = findViewById(R.id.pokeTitle);
                    pokeTitle.setText( result.size() + "명이 쿸 찔렀습니다");
                    adapter = new PokeListAdapter(result, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
