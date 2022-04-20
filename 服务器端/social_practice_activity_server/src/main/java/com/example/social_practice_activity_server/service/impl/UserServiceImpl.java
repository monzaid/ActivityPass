package com.example.social_practice_activity_server.service.impl;

import com.example.social_practice_activity_server.dao.impl.UserDaoImpl;
import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDaoImpl userDao = new UserDaoImpl();
    @Override
    public void register(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User login(User user) {
        return userDao.queryUserByIdAndPassword(user.getId(), user.getPassword());
    }

    @Override
    public boolean isExistId(String id) {
        if (userDao.queryUserById(id) == null){
            return false;
        }
        return true;
    }

    @Override
    public void update(User user) {
        userDao.updateUser(user);
    }
}
