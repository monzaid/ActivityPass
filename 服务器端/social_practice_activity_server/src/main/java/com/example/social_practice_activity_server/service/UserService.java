package com.example.social_practice_activity_server.service;

import com.example.social_practice_activity_server.pojo.User;

public interface UserService {

    /**
     * 用户注册
     * @param user
     */
    public void register(User user);

    /**
     * 用户登录
     * @param user
     * @return 返回 null 说明没有这个用户， 否则返回一个用户对象
     */
    public User login(User user);

    /**
     * 检查用户 id 是否存在
     * @param id
     * @return
     */
    public boolean isExistId(String id);

    /**
     * 用户信息跟新
     * @param user
     */
    public void update(User user);
}