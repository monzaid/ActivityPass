package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.dao.UserDao;
import com.example.social_practice_activity_server.pojo.User;

public class UserDaoImpl extends baseDao implements UserDao {
    @Override
    public User queryUserById(String id) {
        String sql = "select * from t_user where `id` = ?";
        return queryForOne(User.class, sql, id);
    }

    @Override
    public User queryUserByIdAndPassword(String id, String password) {
        String sql = "select * from t_user where `id` = ? and `password` = ?";
        return queryForOne(User.class, sql, id, password);
    }

    @Override
    public int saveUser(User user) {
        String sql = "insert into t_user values(?, ?)";
        return update(sql, user.getId(), user.getPassword());
    }

    @Override
    public int updateUser(User user) {
        String sql = "update t_user set `password` = ? where `id` = ?";
        return update(sql, user.getPassword(), user.getId());
    }
}