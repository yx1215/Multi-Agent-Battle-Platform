package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {

        Map<String, String> map = new HashMap<>();
        if (username == null){
            map.put("error_message", "username cannot be empty");
            return map;
        }
        if (password == null || confirmedPassword == null){
            map.put("error_message", "password cannot be empty");
            return map;
        }

        username = username.trim();
        if (username.length() == 0){
            map.put("error_message", "username cannot be empty");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message", "password cannot be empty");
            return map;
        }

        if (username.length() > 100){
            map.put("error_message", "username too long");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100){
            map.put("error_message", "password too long");
            return map;
        }

        if (!password.equals(confirmedPassword)){
            map.put("error_message", "Confirmed password is not the same as the password.");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()){
            map.put("error_message", "username already exists.");
            return map;
        }

        String encoded = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/217756_lg_b9bc65a321.jpg";
        User user = new User(null, username, encoded, photo, 1500);
        userMapper.insert(user);
        map.put("error_message", "success");

        return map;
    }
}
