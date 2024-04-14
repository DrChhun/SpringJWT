package com.example.springjwt.Service;

import com.example.springjwt.Model.Dto.request.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailingService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromMail);
        //set email where you want to send to
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Registered please verify with OTP.");

        Context context = new Context();
        context.setVariable("content", otp);
//        context.
        String processedString = templateEngine.process("template", context);
        mimeMessageHelper.setText(processedString, true);

//        if(request.isHTML()) {
//            Context context = new Context();
//            /*
//            content is the variable defined in our HTML template within the div tag
//            */
//            context.setVariable("content", "gg");
//            String processedString = templateEngine.process("template", context);
//
//            mimeMessageHelper.setText(processedString, true);
//        } else {
//            mimeMessageHelper.setText(request.getMessage(), false);
//        }

        mailSender.send(mimeMessage);
    }
}
