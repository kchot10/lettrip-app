package com.cookandroid.travelerapplication.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertData_Travel extends AsyncTask<String,Void,String> { // 통신을 위한 InsertData 생성
    ProgressDialog progressDialog;
    private static String TAG = "youn"; //phptest log 찍으려는 용도

    private String return_string;
    @Override
    protected String doInBackground(String... params) {

        String serverURL = (String) params[0];
        String user_id = (String)params[1];
        String created_date = (String)params[2];
        String is_visited = (String)params[3];
        String depart_date = (String)params[4];
        String last_date = (String)params[5];
        String total_cost = (String)params[6];
        String province = (String)params[7];
        String city = (String)params[8];
        String number_of_courses = (String)params[9];
        String title = (String)params[10];
        String travel_theme = (String)params[11];
        // 없는거 : province(행정구역), city, number_of_courses


        String postParameters ="user_id="+user_id+"&created_date="+created_date
                +"&is_visited="+is_visited+"&depart_date="+depart_date
                +"&last_date="+last_date+"&total_cost="+total_cost
                +"&province="+province
                +"&city="+city+"&number_of_courses="+number_of_courses
                +"&title="+title+"&travel_theme="+travel_theme;

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

            String result = getTwoCharsAfterString(sb.toString(), "불러오기 ");
            Log.d("lettrip", result);
            if (result.equals("성공")){
                return_string = getTwoCharsAfterString(sb.toString(), "travel_id:");
            }else if(result.equals("실패")) {
                return_string = "실패";
            }else{
                return_string = "에러";
            }


            //저장된 데이터를 스트링으로 변환하여 리턴값으로 받는다.
            return  sb.toString();


        }

        catch (Exception e) {

            Log.d(TAG, "InsertData_Travel: Error",e);

            return  new String("Error " + e.getMessage());

        }

    }

    public String getReturn_string() {
        return return_string;
    }

    public String getTwoCharsAfterString(String str, String searchString) {
        String result = "";
        int index = str.indexOf(searchString);
        if (index != -1) {
            int endIndex = str.indexOf(" ", index + searchString.length());
            if (endIndex == -1) {
                endIndex = str.length();
            }
            result = str.substring(index + searchString.length(), endIndex);
        }
        return result;
    }
}

