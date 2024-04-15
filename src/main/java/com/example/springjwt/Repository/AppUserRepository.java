package com.example.springjwt.Repository;

import ch.qos.logback.core.model.TimestampModel;
import ch.qos.logback.core.model.processor.TimestampModelHandler;
import com.example.springjwt.Model.AppUser;
import com.example.springjwt.Model.Dto.request.AppUserRequest;
import com.example.springjwt.Model.Dto.response.AppUserResponse;
import com.example.springjwt.Model.Otp;
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
        select u.fullname from users u inner join otps o on u.id = o.user_id where id = #{id};
    """)
    String getUserByUserId(Integer id);
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
        SELECT * FROM otps WHERE user_id = #{id};
    """)
    @Results(id = "otpMap", value = {
            @Result(property = "otpId", column = "otp_id"),
            @Result(property = "otpCode", column = "otp_code"),
            @Result(property = "issuedAt", column = "issued_at"),
//            @Result(property = "userId", column = "user_id", one = @One(select = "getUserByUserId"))
    })
    Otp findOtpByUserId(Integer id);

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

    @Select("""
        UPDATE otps
        SET verify = #{b}
        WHERE user_id = #{id}
    """)
    void verifyUserOtp(Integer id, boolean b);

    @Select("""
        UPDATE otps
        SET otp_code = #{otp}
        WHERE user_id = #{id};
    """)
    void resendVerify(Integer id, String otp);
}
