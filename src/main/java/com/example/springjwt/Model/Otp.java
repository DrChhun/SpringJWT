package com.example.springjwt.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Otp {
    int otpId;
    String otpCode;
    LocalDateTime issuedAt;
    Timestamp expiration;
    boolean verify;
    AppUser userId;
}
