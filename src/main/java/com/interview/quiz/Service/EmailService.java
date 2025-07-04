package com.interview.quiz.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

 @Autowired
 private JavaMailSender  mailSender;

 public void sendInvite(String to, String recruiterName, String link) {
     SimpleMailMessage message = new SimpleMailMessage();
     message.setTo(to);
     message.setSubject("Interview Invitation from " + recruiterName);
     message.setText("Hello,\n\nYou have an interview scheduled.\nJoin here: " + link + "\n\nBest of luck!");
     mailSender.send(message);
 }
}
