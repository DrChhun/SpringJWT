package com.example.springjwt.Service;

import com.example.springjwt.Model.AppUser;
import com.example.springjwt.Model.Dto.request.AppUserRequest;
import com.example.springjwt.Model.Dto.response.AppUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    AppUserResponse register(AppUserRequest appUserRequest);
}
