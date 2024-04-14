package com.example.springjwt.Repository;

import com.example.springjwt.Model.AppUser;
import com.example.springjwt.Model.Dto.request.AppUserRequest;
import com.example.springjwt.Model.Dto.response.AppUserResponse;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;

@Mapper
public interface AppUserRepository {
    @Select("""
           SELECT r.role_name FROM roles r INNER JOIN user_role ur
           ON r.id = ur.role_id WHERE user_id = #{userId}
           """)
    String getRoleByUserId(Integer userId);
    @Select("""
           SELECT * FROM users WHERE email = #{email}
           """)
    @Results(id = "userMap", value = {
            @Result(property = "roles", column = "id",
                    many = @Many(select = "getRoleByUserId")
            ),
            @Result(property = "id", column = "id"),
            @Result(property = "fullName", column = "full_name")
    })
    AppUser findByEmail(String email);

    @Select("""
        INSERT INTO users VALUES (default, #{appUser.fullName}, #{appUser.email}, #{appUser.password})
        RETURNING *
    """)
    @ResultMap("userMap")
    AppUser register(@Param("appUser") AppUserRequest appUserRequest);

    @Insert("""
        INSERT INTO user_role VALUES (#{userId}, #{roleId})
    """)
    void insertUserIdAndRoleId(Integer userId, Integer roleId);

    @Insert("""
        INSERT INTO otps VALUES (default, #{otp}, #{issued}, #{expiration}, #{verify}, #{userId})
    """)
    void insertUserOtp(String otp, Timestamp issued, Timestamp expiration, Boolean verify, Integer userId);
}
