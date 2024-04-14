package com.example.springjwt.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    int otpId;
    String otpCode;
    Timestamp issuedAt;
    Timestamp expiration;
    boolean verify;
    AppUser userId;
}
