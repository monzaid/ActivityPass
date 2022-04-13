package com.example.social_practice_activity_server.service;

import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.Page;

import java.sql.Timestamp;
import java.util.List;

public interface ActivityService {

    /**
     * 通过活动 id 查询活动
     * @param id
     * @return 返回 null 说明没有这个活动， 否则返回一个活动对象
     */
    public Activity queryActivityById(int id);
    /**
     * 通过发布者 id 查询活动
     * @param id
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageBySenderId(String id, int begin, int pageSize);
    /**
     * 通过活动标题查询活动
     * @param title
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageByTitle(String title, int begin, int pageSize);
    /**
     * 通过发布活动的组织、个人查询活动
     * @param team
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageByTeam(String team, int begin, int pageSize);
    /**
     * 通过活动的地点查询活动
     * @param place
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageByPlace(String place, int begin, int pageSize);
    /**
     * 通过活动的地点查询活动
     * @param x
     * @param y
     * @param distance
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageByPlace(double x, double y, double distance, int begin, int pageSize);
    /**
     * 通过活动的开始时间查询活动
     * @param time
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> pageByTime(Timestamp time, int begin, int pageSize);

    /**
     * 新增活动
     * @param activity
     */
    public void addActivity(Activity activity);

    /**
     * 删除活动
     * @param id
     */
    public void deleteActivity(int id);

    /**
     * 更新活动
     * @param activity
     */
    public void updateActivity(Activity activity);

    /**
     * 通过 页面第几页和大小查询活动
     * @param begin 页面第几页
     * @param pageSize 页面大小
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Activity> page(int begin, int pageSize);

}
