package com.example.springjwt.Model.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private List<String> roles;
}
