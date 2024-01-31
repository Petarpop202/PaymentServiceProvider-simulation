package org.example.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.PaymentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    private final String emailTo = "spring.mail.username";
    private final String emailFrom = "psp@gmail.com";
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Async
    public void sendTokenMail(String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(Objects.requireNonNull(env.getProperty(emailTo)));
        helper.setFrom(emailFrom);
        helper.setSubject("Subscription token");
        helper.setText("Token for your subscription: " + token, false);
        javaMailSender.send(mimeMessage);
        logger.info("Subscription mail sent to user !");
    }
}
