package com.example.social_practice_activity_server.test;

import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.service.UserService;
import com.example.social_practice_activity_server.service.impl.UserServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
    UserService userService = new UserServiceImpl();
    @Test
    public void register() {
//        userService.register(new User("55", "b", "c", null));
    }

    @Test
    public void login() {
//        System.out.println(userService.login(new User("44", "b", "c", null)));
    }

    @Test
    public void isExistId() {
        System.out.println(userService.isExistId("44"));
    }

    @Test
    public void updateIpAndPort(){
//        userService.updateIpAndPort(new User("42", "a", null, null, "21.12.22.121", 22911));
    }
}