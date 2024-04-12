package com.example.springjwt.Repository;

import com.example.springjwt.Model.AppUser;
import org.apache.ibatis.annotations.*;

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
}
