package com.example.springjwt.Util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    //create a method to generate number with 6 figure
    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);
        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
}
