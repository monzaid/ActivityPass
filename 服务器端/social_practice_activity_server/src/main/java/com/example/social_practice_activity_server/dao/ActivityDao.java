package com.example.social_practice_activity_server.dao;

import com.example.social_practice_activity_server.pojo.Activity;

import java.sql.Timestamp;
import java.util.List;

public interface ActivityDao {

    /**
     * 通过活动 id 查询活动
     * @param id
     * @return 返回 null 说明没有这个活动， 否则返回一个活动对象
     */
    public Activity queryActivityById(int id);

    /**
     * 通过发布者 id 查询活动总数
     * @param id
     * @return 活动总数
     */
    public int queryActivityBySenderIdCount(String id);
    /**
     * 通过发布者 id 查询活动
     * @param id
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityBySenderId(String id, int begin, int pageSize);
    /**
     * 通过活动标题查询活动总数
     * @param title
     * @return 活动总数
     */
    public int queryActivityByTitleCount(String title);
    /**
     * 通过活动标题查询活动
     * @param title
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityByTitle(String title, int begin, int pageSize);
    /**
     * 通过发布活动的组织、个人查询活动总数
     * @param team
     * @return 活动总数
     */
    public int queryActivityByTeamCount(String team);
    /**
     * 通过发布活动的组织、个人查询活动
     * @param team
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityByTeam(String team, int begin, int pageSize);
    /**
     * 通过发活动的地点查询活动总数
     * @param place
     * @return 活动总数
     */
    public int queryActivityByPlaceCount(String place);
    /**
     * 通过活动的地点查询活动
     * @param place
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityByPlace(String place, int begin, int pageSize);
    /**
     * 通过发活动的地点查询活动总数
     * @param x
     * @param y
     * @param distance
     * @return 活动总数
     */
    public int queryActivityByPlaceCount(double x, double y, double distance);
    /**
     * 通过活动的地点坐标查询活动
     * @param x
     * @param y
     * @param distance
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityByPlace(double x, double y, double distance, int begin, int pageSize);
    /**
     * 通过动的开始时间查询活动总数
     * @param time
     * @return 活动总数
     */
    public int queryActivityByTimeCount(Timestamp time);
    /**
     * 通过活动的开始时间查询活动
     * @param time
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryActivityByTime(Timestamp time, int begin, int pageSize);

    /**
     * 新增活动
     * @param activity
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int addActivity(Activity activity);

    /**
     * 删除活动
     * @param id
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int deleteActivity(int id);

    /**
     * 更新活动
     * @param activity
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int updateActivity(Activity activity);

    /**
     * 查询活动总数
     * @return 活动总数
     */
    public int queryForPageTotalCount();

    /**
     * 通过 页面第几页和大小查询活动
     * @param begin 页面第几页
     * @param pageSize 页面大小
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Activity> queryForPageItems(int begin, int pageSize);
}
