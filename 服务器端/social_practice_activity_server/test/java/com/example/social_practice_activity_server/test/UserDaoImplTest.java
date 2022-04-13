package com.example.social_practice_activity_server.test;

import com.example.social_practice_activity_server.dao.UserDao;
import com.example.social_practice_activity_server.dao.impl.UserDaoImpl;
import com.example.social_practice_activity_server.pojo.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDaoImplTest {
    UserDao userDao = new UserDaoImpl();

    @Test
    public void queryUserByUsername() {
        System.out.println(userDao.queryUserById("44"));
    }

    @Test
    public void queryUserByUsernameAndPassword() {
        System.out.println(userDao.queryUserByIdAndPassword("43", "a"));
    }

    @Test
    public void saveUser() {
//        System.out.println(userDao.saveUser(new User("45", "a", "b", "aaaa", "2.2.2.2", 22910)));
    }
    @Test
    public void updateUser(){
//        System.out.println(userDao.updateUser(new User("45", "aa", "b", "aaaa", "21.12.22.12", 22910)));

    }

    @Test
    public void updateIpAndPort(){
//        System.out.println(userDao.updateIpAndPort(new User("42", "a", null, null, "21.12.22.12", 22910)));
    }
}