package com.klaus.critcpoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String destinatario, String assunto, String mensagem) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(destinatario);
        mailMessage.setSubject(assunto);
        mailMessage.setText(mensagem);
        mailMessage.setFrom("critcpoint@gmail.com");
        mailSender.send(mailMessage);
    }
}
