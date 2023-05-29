package com.cookandroid.travelerapplication.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.travelerapplication.comment.Comment;
import com.cookandroid.travelerapplication.article.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectData_Article extends AsyncTask<String,Void,String> { // 통신을 위한 InsertData 생성
    ProgressDialog progressDialog;
    private static String TAG = "youn"; //phptest log 찍으려는 용도

    public ArrayList articleArrayList;

    public <T> SelectData_Article(ArrayList<T> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }

    private String return_string = "";
    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String) params[0];

        String postParameters = "";
        try {
            String article_id = (String) params[1];
            String parent_comment_id = (String) params[2];
            postParameters ="article_id="+article_id+"&parent_comment_id="+parent_comment_id;
        }catch (Exception e){
        }

        try{ // HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송한다.
            URL url = new URL(serverURL); //주소가 저장된 변수를 이곳에 입력한다.
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생한다.

            httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생한다.

            httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 한다.

            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();

            //전송할 데이터가 저장된 변수를 이곳에 입력한다. 인코딩을 고려해줘야 하기 때문에 UTF-8 형식으로 넣어준다.
            outputStream.write(postParameters.getBytes("UTF-8"));

            outputStream.flush();//현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다.
            outputStream.close(); //객체를 닫음으로써 자원을 반납한다.


            int responseStatusCode = httpURLConnection.getResponseCode(); //응답을 읽는다.
            Log.d(TAG, "POST response code-" + responseStatusCode);

            InputStream inputStream;

            if(responseStatusCode == httpURLConnection.HTTP_OK){ //만약 정상적인 응답 데이터 라면
                inputStream=httpURLConnection.getInputStream();
                Log.d("php정상: ","정상적으로 출력"); //로그 메세지로 정상적으로 출력을 찍는다.
            }
            else {
                inputStream = httpURLConnection.getErrorStream(); //만약 에러가 발생한다면
                Log.d("php비정상: ","비정상적으로 출력"); // 로그 메세지로 비정상적으로 출력을 찍는다.
            }

            // StringBuilder를 사용하여 수신되는 데이터를 저장한다.
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) !=null ) {
                sb.append(line);
            }

            bufferedReader.close();

            Log.d("php 값 :", sb.toString());

            try{
                parseJSONArray(sb.toString());
            }catch (Exception e){
                Log.d("youn", "JSON Error\n");
            }




            //저장된 데이터를 스트링으로 변환하여 리턴값으로 받는다.
            return  sb.toString();


        }

        catch (Exception e) {

            Log.d(TAG, "SelectData_Article: Error",e);

            return  new String("Error " + e.getMessage());

        }

    }

    private void parseJSONArray(String result) throws JSONException {
        // JSON 형태의 데이터를 파싱하여 JSONArray로 변환
        JSONArray jsonArray = new JSONArray(result);

        try {
            jsonArray.getJSONObject(0).getString("hit");
            parseJSONArray_Article(jsonArray);
        }catch (Exception e){
            parseJSONArray_Comment(jsonArray);
        }

//        parseJSONArray_Article(jsonArray);
//        parseJSONArray_Comment(jsonArray);
    }

    private void parseJSONArray_Article(JSONArray jsonArray) throws JSONException {

        // JSONArray로부터 데이터 추출
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Article article = new Article();

            String articleId = jsonObject.getString("article_id");
            String createdDate = jsonObject.getString("created_date");
            String modified_date = jsonObject.getString("modified_date");
            String content = jsonObject.getString("content");
            String hit = jsonObject.getString("hit");
            String like_count = jsonObject.getString("like_count");
            String title = jsonObject.getString("title");
            String user_id = jsonObject.getString("user_id");
            String name = jsonObject.getString("name");
            String image_url = jsonObject.getString("image_url");
            String comment_number = jsonObject.getString("comment_number");

            article.setArticle_id(articleId);
            article.setCreated_date(createdDate);
            article.setModified_date(modified_date);
            article.setContent(content);
            article.setHit(hit);
            article.setLike_count(like_count);
            article.setTitle(title);
            article.setUser_id(user_id);
            article.setName(name);
            article.setImage_url(image_url);
            article.setComment_number(comment_number);

            articleArrayList.add(article);

        }
    }

    private void parseJSONArray_Comment(JSONArray jsonArray) throws JSONException {
        // JSON 형태의 데이터를 파싱하여 JSONArray로 변환

        // JSONArray로부터 데이터 추출
        for (int i = 0; i < jsonArray.length(); i++) {


            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Comment comment = new Comment();

            String comment_id = jsonObject.getString("comment_id");
            String createdDate = jsonObject.getString("created_date");
            String modified_date = jsonObject.getString("modified_date");
            String content = jsonObject.getString("content");
            String article_id = jsonObject.getString("article_id");
            String mentioned_user_id = jsonObject.getString("mentioned_user_id");
            String parent_comment_id = jsonObject.getString("parent_comment_id");
            String user_id = jsonObject.getString("user_id");
            String name = jsonObject.getString("name");
            String mentioned_user_name = jsonObject.getString("mentioned_user_name");
            String image_url = jsonObject.getString("image_url");
            String comment_count = jsonObject.getString("comment_count");

            comment.setComment_id(comment_id);
            comment.setCreated_date(createdDate);
            comment.setModified_date(modified_date);
            comment.setContent(content);
            comment.setArticle_id(article_id);
            comment.setMentioned_user_id(mentioned_user_id);
            comment.setParent_comment_id(parent_comment_id);
            comment.setUser_id(user_id);
            comment.setName(name);
            comment.setMentioned_user_name(mentioned_user_name);
            comment.setImage_url(image_url);
            comment.setComment_count(comment_count);

            articleArrayList.add(comment);

        }
    }

    public String get_return_string(){
        return return_string;
    }

    public String getTwoCharsAfterString(String str, String searchString) {
        String result = "";
        int index = str.indexOf(searchString);
        if (index != -1 && index + searchString.length() + 2 <= str.length()) {
            result = str.substring(index + searchString.length(), index + searchString.length() + 2);
        }
        return result;
    }

}
