package com.cookandroid.travelerapplication.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.MbEditText;
import com.cookandroid.travelerapplication.comment.Comment;
import com.cookandroid.travelerapplication.comment.CommentAdapter;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.DeleteData_Article;
import com.cookandroid.travelerapplication.task.InsertData_Comment;
import com.cookandroid.travelerapplication.task.SelectData_Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArticleContentActivity extends AppCompatActivity {

    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    TextView textview_name, textView_date, textview_title, textview_content, textview_count_view, board_comment;

    ImageView profilePhoto;
    RecyclerView recyclerView_comment;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private String article_id;

    private MbEditText edittext_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content2);
        edittext_content = findViewById(R.id.edittext_content);
        textview_name = findViewById(R.id.textview_user_id);
        textView_date = findViewById(R.id.textview_created_date);
        textview_title = findViewById(R.id.textview_title);
        textview_content = findViewById(R.id.textview_content);
        textview_count_view = findViewById(R.id.textview_count_view);
        board_comment = findViewById(R.id.board_comment);
        // Todo: img_url 불러와서 profilePhoto에 Gilde로 이미지 넣기.
        textview_name.setText(getIntent().getStringExtra("name"));
        textView_date.setText(getIntent().getStringExtra("created_date"));
        textview_title.setText(getIntent().getStringExtra("title"));
        textview_content.setText(getIntent().getStringExtra("content"));
        textview_count_view.setText(getIntent().getStringExtra("hit"));
        //Todo: comment_number(댓글 수)의 데이터를 실제론 못들고왔음.
        //board_comment.setText(getIntent().getStringExtra("comment_number"));
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        String user_id_login = fileHelper.readFromFile("user_id").trim();
        article_id = getIntent().getStringExtra("article_id");


        recyclerView_comment = findViewById(R.id.recyclerView_comment);
        recyclerView_comment.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_comment.setLayoutManager(layoutManager);
        Refresh();


        if (user_id_login.equals(getIntent().getStringExtra("user_id"))) {
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);
            findViewById(R.id.button_delete_article).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.button_delete_article).setOnClickListener(v -> {
            String article_id = getIntent().getStringExtra("article_id").trim();
            DeleteData_Article task = new DeleteData_Article();
            task.execute("http://"+IP_ADDRESS+"/0411/deletedata_article.php",article_id);
            finish();
        });

        findViewById(R.id.button_update).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            intent.putExtra("sign", "1");
            intent.putExtra("title", getIntent().getStringExtra("title"));
            intent.putExtra("content", getIntent().getStringExtra("content"));
            intent.putExtra("article_id", getIntent().getStringExtra("article_id"));
            startActivity(intent);
            finish();
        });

        findViewById(R.id.button_add).setOnClickListener(v -> {
            String content = edittext_content.getText().toString().trim();

            if (content.equals("")){
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String currentTime = getCurrentTime();
                String created_date = currentTime;
                String modified_date = currentTime;
                String parent_comment_id = getIntent().getStringExtra("comment_id");
                if (parent_comment_id == null){
                    parent_comment_id = "0";
                }
                InsertData_Comment task = new InsertData_Comment();
                task.execute("http://" + IP_ADDRESS + "/0422/InsertData_Comment.php", "0", created_date, modified_date, content, article_id, "0", parent_comment_id, user_id_login);
                Refresh();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                edittext_content.setText("");
                edittext_content.clearFocus();
            }
        });

    }

    public void Refresh() {
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(commentArrayList);
        String parent_comment_id = getIntent().getStringExtra("comment_id");
        if (parent_comment_id == null){
            parent_comment_id = "0";
        }
        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_comment.php", article_id, parent_comment_id);

        try {
            new Handler().postDelayed(() -> {
                adapter = new CommentAdapter(commentArrayList, this);
                recyclerView_comment.setAdapter(adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
}