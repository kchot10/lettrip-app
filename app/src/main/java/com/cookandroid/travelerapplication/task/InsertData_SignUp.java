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

public class InsertData_SignUp extends AsyncTask<String,Void,String> { // 통신을 위한 InsertData 생성
    ProgressDialog progressDialog;
    private static String TAG = "youn"; //phptest log 찍으려는 용도

    @Override
    protected String doInBackground(String... params) {
            /*
            PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비한다.
            POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않는다.
            이 값들은 InsertData 객체.excute 에서 매개변수로 준 값들이 배열 개념으로 차례대로 들어가
            값을 받아오는 개념이다.
             */
        String serverURL = (String) params[0];
        String email = (String)params[1];
        String password = (String)params[2];
        String name = (String)params[3];
        String image_url = (String)params[4];
        String nickname = (String)params[5];
        String provider_type = (String)params[6];



            /*
            HTTP 메세지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야한다.
            전송할 데이터는 "이름=값" 형식이며 여러 개를 보내야 할 경우에 항목 사이에 &를 추가해준다.
            여기에 적어준 이름들은 나중에 PHP에서 사용하여 값을 얻게 된다.
             */
        // useremail=kchot10@naver.com    &userid=kchot10@naver.com   &userpw=123123
        String postParameters ="email="+email+"&password="+password
                +"&name="+name+"&image_url="+image_url
                +"&nickname="+nickname+"&provider_type="+provider_type;

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


            //저장된 데이터를 스트링으로 변환하여 리턴값으로 받는다.
            return  sb.toString();


        }

        catch (Exception e) {

            Log.d(TAG, "InsertData: Error",e);

            return  new String("Error " + e.getMessage());

        }

    }
}
