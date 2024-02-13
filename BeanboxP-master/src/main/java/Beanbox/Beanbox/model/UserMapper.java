package Beanbox.Beanbox.model;

import Beanbox.Beanbox.dto.UserDto;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username, password, email, number) VALUES (#{username}, #{password}, #{email}, #{number})")
    void insert(UserDto user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    UserDto findByUsername(String username);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(String username);

    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    int countByEmail(String email);

    @Select("SELECT COUNT(*) FROM user WHERE number = #{number}")
    int countByNumber(String number);
}
