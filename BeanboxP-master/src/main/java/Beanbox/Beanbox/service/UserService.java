package Beanbox.Beanbox.service;

import Beanbox.Beanbox.dto.UserDto;
import Beanbox.Beanbox.model.UserMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;

    private final HttpSession httpSession;

    public UserService(UserMapper userMapper, HttpSession httpSession) {
        this.userMapper = userMapper;
        this.httpSession = httpSession;
    }

    public Map<String, String> signup(UserDto user) {
        Map<String, String> response = new HashMap<>();

        if (userMapper.countByUsername(user.getUsername()) == 0 &&
                userMapper.countByEmail(user.getEmail()) == 0 &&
                userMapper.countByNumber(user.getNumber()) == 0) {
            userMapper.insert(user);
            response.put("result", "success");
        } else {
            response.put("result", "fail");
        }

        return response;
    }

    public Map<String, String> signin(UserDto user) {
        Map<String, String> response = new HashMap<>();

        UserDto foundUser = userMapper.findByUsername(user.getUsername());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            response.put("result", "success");
            response.put("username", foundUser.getUsername());

            httpSession.setAttribute("username", foundUser.getUsername());
        } else {
            response.put("result", "fail");
        }

        return response;
    }
}

