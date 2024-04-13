package com.example.springjwt.Service;

import com.example.springjwt.Model.AppUser;
import com.example.springjwt.Model.Dto.request.AppUserRequest;
import com.example.springjwt.Model.Dto.response.AppUserResponse;
import com.example.springjwt.Repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService{
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    public AppUserServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email);
    }

    @Override
    public AppUserResponse register(AppUserRequest appUserRequest) {
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
//        return appUserRepository.findByEmail(appUser.getEmail());
        return modelMapper.map(appUserRepository.findByEmail(appUser.getEmail()), AppUserResponse.class);
//        return appUserRepository.findByEmail(appUser.getEmail());
    }
}
