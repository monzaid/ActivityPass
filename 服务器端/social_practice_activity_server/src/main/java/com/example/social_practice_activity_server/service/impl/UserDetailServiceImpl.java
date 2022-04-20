package com.example.social_practice_activity_server.service.impl;

import com.example.social_practice_activity_server.dao.UserDetailDao;
import com.example.social_practice_activity_server.dao.impl.UserDetailDaoImpl;
import com.example.social_practice_activity_server.pojo.UserDetail;
import com.example.social_practice_activity_server.service.UserDetailService;

public class UserDetailServiceImpl implements UserDetailService {
    private UserDetailDao userDetailDao = new UserDetailDaoImpl();

    @Override
    public UserDetail find(String id) {
        return userDetailDao.queryUserDetailById(id);
    }

    @Override
    public void add(UserDetail userDetail) {
        userDetailDao.saveUserDetail(userDetail);
    }

    @Override
    public void update(UserDetail userDetail) {
        userDetailDao.updateUserDetail(userDetail);
    }

    @Override
    public void updateByFace(String face, String id) {
        userDetailDao.updateUserDetailByFace(face, id);
    }
}
