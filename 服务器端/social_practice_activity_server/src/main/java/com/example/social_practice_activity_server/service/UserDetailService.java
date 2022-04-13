package com.example.social_practice_activity_server.service;

import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.pojo.UserDetail;

public interface UserDetailService {
    /**
     * 根据用户 id 查询
     * @param id
     * @return
     */
    public UserDetail find(String id);

    /**
     * 用户注册
     * @param userDetail
     */
    public void add(UserDetail userDetail);

    /**
     * 用户信息跟新
     * @param userDetail
     */
    public void update(UserDetail userDetail);

    /**
     * 用户人脸信息跟新
     * @param face
     * @param id
     */
    public void updateByFace(String face, String id);
}