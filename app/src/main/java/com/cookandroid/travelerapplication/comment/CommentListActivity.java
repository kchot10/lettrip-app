package com.cookandroid.travelerapplication.comment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.travelerapplication.MbEditText;
import com.cookandroid.travelerapplication.account.LoginActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.InsertData_Comment;
import com.cookandroid.travelerapplication.task.SelectData_Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentListActivity extends AppCompatActivity {

    private final String BASEID = "-1";

    TextView textview_created_date_comment, textview_user_id_comment, textview_content_comment;
    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String article_id;
    TextView textView_mention;
    private MbEditText edittext_content;

    private String mentioned_user_id = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        FileHelper fileHelper = new FileHelper(this);
        IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        edittext_content = findViewById(R.id.edittext_content);
        article_id = getIntent().getStringExtra("article_id");
        ImageView profilePhoto = findViewById(R.id.profilePhoto_comment);
        TextView textView_mention = findViewById(R.id.textView_mention);
        textview_user_id_comment = findViewById(R.id.textview_user_id_comment);
        textview_created_date_comment = findViewById(R.id.textview_created_date_comment);
        textview_content_comment = findViewById(R.id.textview_content_comment);
        Glide.with(this)
                .load(getIntent().getStringExtra("image_url"))
                .into(profilePhoto);
        textview_user_id_comment.setText(getIntent().getStringExtra("name"));
        textview_content_comment.setText(getIntent().getStringExtra("content"));
        textview_created_date_comment.setText(getIntent().getStringExtra("created_date"));

        recyclerView = findViewById(R.id.RecyclerView_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Refresh();


        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        findViewById(R.id.button_add).setOnClickListener(v -> {
            String content = edittext_content.getText().toString().trim();

            if (content.equals("")){
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String currentTime = getCurrentTime();
                String created_date = currentTime;
                String modified_date = currentTime;
                String user_id = fileHelper.readFromFile("user_id");
                String parent_comment_id = getIntent().getStringExtra("comment_id");
                if (parent_comment_id == null){
                    parent_comment_id = BASEID;
                }
                InsertData_Comment task = new InsertData_Comment();
                task.execute("http://" + IP_ADDRESS + "/0422/InsertData_Comment.php", BASEID, created_date, modified_date, content, article_id, mentioned_user_id, parent_comment_id, user_id);
                Refresh();
                textView_mention.setText("");
                edittext_content.setText("");
                edittext_content.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        textView_mention.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommentListActivity.this);
            builder.setMessage("언급자를 지우시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textView_mention.setText("");
                    mentioned_user_id = BASEID;
                }
            });
            builder.setNegativeButton("취소", null);
            builder.show();
        });

    }

    public void Refresh() {
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(commentArrayList);
        String parent_comment_id = getIntent().getStringExtra("comment_id");
        if (parent_comment_id == null){
            parent_comment_id = BASEID;
        }
        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_comment.php", article_id, parent_comment_id);

        try {
            new Handler().postDelayed(() -> {
                adapter = new CommentAdapter(commentArrayList, this);
                recyclerView.setAdapter(adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public void setEditText(String user_name, String user_id) {
        textView_mention = findViewById(R.id.textView_mention);
        textView_mention.setText("@"+user_name+" ");
        mentioned_user_id = user_id;
    }
}