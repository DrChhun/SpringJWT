package com.example.springjwt.Service;

import com.example.springjwt.Model.AppUser;
import com.example.springjwt.Model.Dto.request.AppUserRequest;
import com.example.springjwt.Model.Dto.response.AppUserResponse;
import com.example.springjwt.Model.Otp;
import com.example.springjwt.Repository.AppUserRepository;
import com.example.springjwt.Util.OtpUtil;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AppUserServiceImpl implements AppUserService{
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final OtpUtil otpUtil;
    private final EmailingService emailingService;
    public AppUserServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, OtpUtil otpUtil, EmailingService emailingService) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.otpUtil = otpUtil;
        this.emailingService = emailingService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUserResponse register(AppUserRequest appUserRequest) {
        //get generate otp code from util package
        String otp = otpUtil.generateOtp();
        //enctype password
        appUserRequest.setPassword(bCryptPasswordEncoder.encode(
                appUserRequest.getPassword()
        ));
        AppUser appUser = appUserRepository.register(appUserRequest);
        for (String role: appUserRequest.getRoles()) {
            if (role.equals("ROLE_USER")) {
                appUserRepository.insertUserIdAndRoleId(appUser.getId(), 1);
            }

            if (role.equals("ROLE_ADMIN")) {
                appUserRepository.insertUserIdAndRoleId(appUser.getId(), 2);
            }
        }

        //calling emailing service for send otp code to email, also send email and otp as argument.
        try {
            emailingService.sendMail(appUser.getEmail(), otp);
            appUserRepository.insertUserOtp(otp, Timestamp.valueOf("2024-04-14 21:58:58.000000"), Timestamp.valueOf("2024-04-14 21:58:58.000000"), true, appUser.getId());
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP code.");
        }

        return modelMapper.map(appUserRepository.findByEmail(appUser.getEmail()), AppUserResponse.class);
    }

    @Override
    public String verify(String otp) {
        Otp user = appUserRepository.findOtpByUserId(27);
        if (user.getOtpCode().equals(otp) && Duration.between(user.getIssuedAt().getTimestamp().toInstant(), LocalDateTime.now()).getSeconds() < (1  * 60)) {
//            user.setVerify(true);
            System.out.println("right here");
            appUserRepository.verifyUserOtp(27, true);
        }
        return "gg";
    }
}
