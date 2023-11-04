package com.cookandroid.travelerapplication.task;

import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.travelerapplication.meetup.PokeItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InsertData_Meetup extends AsyncTask<String, Void, String> {
    private String return_string;
    private String request_id;
    private AsyncTaskCompleteListener callback;

    public InsertData_Meetup(AsyncTaskCompleteListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = params[0];
        String meet_up_post_id = params[1];
        String write_user_id = params[2];
        String request_user_id = params[3];
        String meet_up_date = params[4];

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = "meet_up_post_id=" + meet_up_post_id +
                    "&write_user_id=" + write_user_id +
                    "&request_user_id=" + request_user_id +
                    "&meet_up_date=" + meet_up_date;
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("InsertData_Meetup", "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                Log.d("InsertData_Meetup", "정상적으로 응답이 왔습니다.");
            } else {
                inputStream = httpURLConnection.getErrorStream();
                Log.e("InsertData_Meetup", "에러 응답 - " + responseStatusCode);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();

            String result = sb.toString().trim();
            Log.d("InsertData_Meetup", "결과 : " + result);

            callback.onTaskComplete(result);

            return result;

        } catch (Exception e) {
            Log.e("InsertData_Meetup", "InsertData_Meetup: Error", e);
            return new String("Error: " + e.getMessage());
        }
    }

    public interface AsyncTaskCompleteListener {
        void onTaskComplete(String result_string);
    }
}
