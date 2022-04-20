package com.example.social_practice_activity_server.dao;

import com.example.social_practice_activity_server.pojo.User;

public interface UserDao{
    /**
     * 根据用户名查询用户信息
     * @param id 用户名
     * @return 返回 null 说明没有这个用户， 否则返回一个用户对象
     */
    public User queryUserById(String id);

    /**
     * 根据用户名和密码查询用户信息
     * @param id 用户名
     * @param password 用户密码
     * @return 返回 null 说明没有这个用户， 否则返回一个用户对象
     */
    public User queryUserByIdAndPassword(String id, String password);

    /**
     * 保存用户信息
     * @param user 用户对象
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int saveUser(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int updateUser(User user);
}