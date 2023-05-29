package com.cookandroid.travelerapplication.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.cookandroid.travelerapplication.helper.GMailSender;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AsyncTask<String,Void,String> {

    Context context;
    String returnString = "";

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    String user = "kchot10@gmail.com"; // 보내는 계정의 id
    String password = "qcixhdqihejkoryk"; // 보내는 계정의 pw
    GMailSender gMailSender = new GMailSender(user, password);
    String emailCode = gMailSender.getEmailCode();

    public String getEmailCode() {
        return emailCode;
    }

    public SendMail(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String sendTo = strings[0];

        try {
            gMailSender.sendMail("Lettrip 인증 코드 메일입니다.", "인증 코드: "+emailCode, sendTo);
            this.setReturnString("인증번호가 전송되었습니다.");
        } catch (SendFailedException e) {
            this.setReturnString("이메일 형식이 잘못되었습니다.");
        } catch (MessagingException e) {
            this.setReturnString("인터넷 연결을 확인해주십시오");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}