package com.cookandroid.travelerapplication.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.travelerapplication.meetup.PokeItem;
import com.cookandroid.travelerapplication.mission.Mission;
import com.cookandroid.travelerapplication.mission.MyMission;

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

public class SelectData_Poke extends AsyncTask<String,Void,String> { // 통신을 위한 InsertData 생성
    ProgressDialog progressDialog;
    private static String TAG = "youn"; //phptest log 찍으려는 용도
    private AsyncTaskCompleteListener callback;
    public ArrayList articleArrayList;

    public <T> SelectData_Poke(ArrayList<T> articleArrayList, AsyncTaskCompleteListener callback) {
        this.articleArrayList = articleArrayList;
        this.callback = callback;
    }

    private String return_string = "";
    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String) params[0];

        String postParameters = "";
        try {
            String meet_up_post_id = (String) params[1];
            postParameters ="meet_up_post_id="+meet_up_post_id;
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
                callback.onTaskComplete(null);
                Log.d("youn", "JSON Error\n");
            }




            //저장된 데이터를 스트링으로 변환하여 리턴값으로 받는다.
            return  sb.toString();


        }

        catch (Exception e) {

            Log.d(TAG, "SelectData_MyMission: Error",e);

            return  new String("Error " + e.getMessage());

        }

    }

    private void parseJSONArray(String result) throws JSONException {
        // JSON 형태의 데이터를 파싱하여 JSONArray로 변환
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            PokeItem pokeItem = new PokeItem();

            String image_url = jsonObject.getString("image_url");
            String meet_up_cancelled_count = jsonObject.getString("meet_up_cancelled_count");
            String meet_up_completed_count = jsonObject.getString("meet_up_completed_count");
            String brief_message = jsonObject.getString("brief_message");
            String nickname = jsonObject.getString("nickname");
            String sex = jsonObject.getString("sex");
            String birth_date = jsonObject.getString("birth_date");
            String meet_up_post_id = jsonObject.getString("meet_up_post_id");
            String write_user_id = jsonObject.getString("write_user_id");
            String request_user_id = jsonObject.getString("user_id");
            String meet_up_id = jsonObject.getString("meet_up_id");
            pokeItem.setImageResource(image_url);
            pokeItem.setMeetupFailNum(meet_up_cancelled_count);
            pokeItem.setMeetupSuccessNum(meet_up_completed_count);
            pokeItem.setOneLineReview(brief_message);
            pokeItem.setUserName(nickname);
            pokeItem.setUserSex(sex);
            pokeItem.setBirthDate(birth_date);
            pokeItem.setMeet_up_post_id(meet_up_post_id);
            pokeItem.setWrite_user_id(write_user_id);
            pokeItem.setRequest_user_id(request_user_id);
            pokeItem.setMeet_up_id(meet_up_id);
            articleArrayList.add(pokeItem);
        }

        callback.onTaskComplete(articleArrayList);
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

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(ArrayList<PokeItem> result);
    }

}
