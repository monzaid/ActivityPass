package com.example.social_practice_activity_server.dao;

import com.example.social_practice_activity_server.pojo.ActivityRegister;

import java.util.List;

public interface ActivityRegisterDao {

    /**
     * 通过活动 id 查询活动管理信息总数
     * @param id 活动 id
     * @return 活动总数
     */
    public int queryActivityRegisterByIdCount(int id);
    /**
     * 通过活动 id 查询活动管理信息
     * @param id 活动 id
     * @return 返回 null 说明没有这个活动， 否则返回一个活动管理信息对象
     */
    public List<ActivityRegister> queryActivityRegisterById(int id, int begin, int pageSize);

    /**
     * 通过用户 id 查询活动总数
     * @param id 用户 id
     * @return 活动总数
     */
    public int queryActivityRegisterByIdCount(String id);

    /**
     * 根据用户 id 查询活动
     * @param id 用户 id
     * @return 返回 null 说明没有这个活动， 否则返回一个活动管理信息对象
     */
    public List<ActivityRegister> queryActivityRegisterById(String id, int begin, int pageSize);

    /**
     * 根据活动 id 和用户 id 查询活动
     * @param ActivityId 活动 id
     * @param userId 用户 id
     * @return 返回 null 说明没有这个活动， 否则返回一个活动管理信息对象
     */
    public ActivityRegister queryActivityRegisterByActivityIdAndUserId(int ActivityId, String userId);

    /**
     * 保存用户活动登记信息
     * @param activityRegister 用户活动登记信息
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int saveActivityRegister(ActivityRegister activityRegister);

    /**
     * 更新用户活动登记信息
     * @param activityRegister 用户活动登记信息
     * @return 返回 -1 说明更新失败， 否则返回 1
     */
    public int updateActivityRegister(ActivityRegister activityRegister);

    /**
     * 删除用户活动登记信息
     * @param id 活动 id
     * @param u_id 用户 id
     * @return 返回 -1 说明更新失败， 否则返回 1
     */
    public int deleteActivityRegister(int id, String u_id);
}
