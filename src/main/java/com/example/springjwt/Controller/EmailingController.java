package com.example.springjwt.Controller;

import com.example.springjwt.Model.Dto.request.MailRequest;
import com.example.springjwt.Service.EmailingService;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class EmailingController {
    private final EmailingService emailingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendMail() throws MessagingException {
        emailingService.sendMail("srykimchhun9@gmail.com", "12345");
    }
}

//https://gaganchordia.medium.com/spring-boot-mail-thymeleaf-362fda6000cf
//https://www.youtube.com/watch?v=kBtgDVzjFzs&ab_channel=RishalCode
