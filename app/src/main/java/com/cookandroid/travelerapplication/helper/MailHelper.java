package com.cookandroid.travelerapplication.helper;

import android.util.Log;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com"; // 이메일 발송에 사용할 서버의 호스트명
    private String user; // 이메일 발송에 사용할 계정의 이메일 주소
    private String password; // 이메일 발송에 사용할 계정의 비밀번호
    private Session session;

    public MailHelper(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 메일 전송시 사용할 프로토콜
        props.setProperty("mail.host", mailhost); // SMTP 서버 호스트명
        props.put("mail.smtp.auth", "true"); // SMTP 인증 사용 여부
        props.put("mail.smtp.port", "465"); // SMTP 포트
        props.put("mail.smtp.socketFactory.port", "465"); // SSL 포트
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Socket Factory 클래스

        session = Session.getInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setSender(new InternetAddress(user)); // 발신자 이메일 주소 설정
            message.setSubject(subject); // 이메일 제목 설정
            message.setContent(body, "text/plain; charset=UTF-8"); // 이메일 본문 내용과 타입 설정
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients)); // 수신자 이메일 주소 설정

            Transport.send(message); // 메일 보내기
        } catch (MessagingException e) {
            Log.e("MailSender", "Failed to send email.", e);
            throw e;
        }
    }
}
