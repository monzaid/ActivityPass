package com.example.social_practice_activity_server.dao;

import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.pojo.UserDetail;

public interface UserDetailDao {
    /**
     * 根据用户名查询用户详细信息
     * @param id 用户名
     * @return 返回 null 说明没有这个用户， 否则返回一个用户详细信息对象
     */
    public UserDetail queryUserDetailById(String id);

    /**
     * 保存用户详细信息
     * @param userDetail 用户详细信息
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int saveUserDetail(UserDetail userDetail);

    /**
     * 更新用户信息
     * @param userDetail 用户详细信息
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int updateUserDetail(UserDetail userDetail);

    /**
     * 跟新用户信息
     * @param face 人脸 id
     * @param id 用户名
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int updateUserDetailByFace(String face, String id);
}