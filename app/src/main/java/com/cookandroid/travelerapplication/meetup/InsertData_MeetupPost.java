package com.cookandroid.travelerapplication.meetup;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertData_MeetupPost extends AsyncTask<String, Void, String> {
    private String return_string;

    @Override
    protected String doInBackground(String... params) {
        String serverURL = params[0];
        String city = params[1];
        String content = params[2];
        String is_gps_enabled = params[3];
        String meet_up_date = params[4];
        String meet_up_post_status = params[5];
        String province = params[6];
        String title = params[7];
        String place_id = params[8];
        String travel_id = params[9];
        String user_id = params[10];
        String created_date = params[11];
        String modified_date = params[12];

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = "city=" + city +
                    "&content=" + content +
                    "&is_gps_enabled=" + is_gps_enabled +
                    "&meet_up_date=" + meet_up_date +
                    "&meet_up_post_status=" + meet_up_post_status +
                    "&province=" + province +
                    "&title=" + title +
                    "&place_id=" + place_id +
                    "&travel_id=" + travel_id +
                    "&user_id=" + user_id +
                    "&created_date=" + created_date +
                    "&modified_date=" + modified_date;
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("InsertData_MeetupPost", "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                Log.d("InsertData_MeetupPost", "정상적으로 응답이 왔습니다.");
            } else {
                inputStream = httpURLConnection.getErrorStream();
                Log.e("InsertData_MeetupPost", "에러 응답 - " + responseStatusCode);
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
            Log.d("InsertData_MeetupPost", "결과 : " + result);

            if (result.equals("불러오기 성공")) {
                return_string = "성공";
            } else if (result.equals("불러오기 실패")) {
                return_string = "실패";
            } else {
                return_string = "에러";
            }

            return result;

        } catch (Exception e) {
            Log.e("InsertData_MeetupPost", "InsertData_MeetupPost: Error", e);
            return new String("Error: " + e.getMessage());
        }
    }

    public String getReturn_string() {
        return return_string;
    }
}
