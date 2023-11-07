package com.cookandroid.travelerapplication.task;

import static com.cookandroid.travelerapplication.meetup.MeetupPostDetailActivity.reformatDate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.travelerapplication.chat.ChatRoom;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.meetup.PokeItem;
import com.cookandroid.travelerapplication.mission.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class SelectData_ChatRoom extends AsyncTask<String,Void,String>{ // 통신을 위한 InsertData 생성
    ProgressDialog progressDialog;
    private static String TAG = "youn"; //phptest log 찍으려는 용도
    private ArrayList<ChatRoom> arrayList;
    private AsyncTaskCompleteListener callback;
    private String return_string;

    public SelectData_ChatRoom(AsyncTaskCompleteListener callback) {
        this.arrayList = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {

        String serverURL = (String) params[0];
        String user_id = (String)params[1];

        String postParameters ="user_id="+user_id;

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

            if(callback == null){
                callback.onTaskComplete_SelectData_ChatRoom(null);
            }else{
                parseJSONArray(sb.toString());
            }

            //저장된 데이터를 스트링으로 변환하여 리턴값으로 받는다.
            return  sb.toString();


        }

        catch (Exception e) {

            Log.d(TAG, "InsertData_Travel: Error",e);

            return  new String("Error " + e.getMessage());

        }

    }



    private void parseJSONArray(String result) throws JSONException {
        // JSON 형태의 데이터를 파싱하여 JSONArray로 변환
        JSONArray jsonArray = new JSONArray(result);

        // 날짜 형식을 지정하고 TimeZone을 한국 시간으로 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ChatRoom chatRoom = new ChatRoom();



            String room_id = jsonObject.getJSONObject("_id").getString("$oid");
            String meet_up_post_id = jsonObject.getString("meet_up_post_id");
            String request_user_id = jsonObject.getString("request_user_id");
            String write_user_id = jsonObject.getString("write_user_id");




            String meet_up_id, meet_up_status, last_message, last_message_time = "null";
            try{
                meet_up_id = jsonObject.getString("meet_up_id");
            }catch (Exception e){
                meet_up_id = "null";
            }
            try{
                meet_up_status = jsonObject.getString("meet_up_status");
            }catch (Exception e){
                meet_up_status = "null";
            }
            try{
                last_message = jsonObject.getString("last_message");
            }catch (Exception e){
                last_message = "null";
            }
            try{
                if(!jsonObject.getString("last_message_time").equals("null")){
                    JSONObject lastMessageTimeObject = jsonObject.getJSONObject("last_message_time");
                    JSONObject lastMessageTimeObject2 = lastMessageTimeObject.getJSONObject("$date");
                    long timestamp2 = lastMessageTimeObject2.getLong("$numberLong");
                    Date date = new Date(timestamp2);
                    last_message_time = dateFormat.format(date);
                }
            }catch (Exception e){
                last_message_time = "null";
            }

            chatRoom.setRoom_id(room_id);
            chatRoom.setMeet_up_post_id(meet_up_post_id);
            chatRoom.setRequest_user_id(request_user_id);
            chatRoom.setMeet_up_id(meet_up_id);
            chatRoom.setMeet_up_status(meet_up_status);
            chatRoom.setWrite_user_id(write_user_id);
            chatRoom.setChatContent(last_message);
            chatRoom.setTime(last_message_time);

            arrayList.add(chatRoom);
        }

        callback.onTaskComplete_SelectData_ChatRoom(arrayList);
    }


    public interface AsyncTaskCompleteListener {
        void onTaskComplete_SelectData_ChatRoom(ArrayList<ChatRoom> result);
    }

}

