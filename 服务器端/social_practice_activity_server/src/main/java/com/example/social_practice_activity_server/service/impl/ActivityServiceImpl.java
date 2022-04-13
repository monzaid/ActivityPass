package com.example.social_practice_activity_server.service.impl;

import com.example.social_practice_activity_server.dao.impl.ActivityDaoImpl;
import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.Page;
import com.example.social_practice_activity_server.service.ActivityService;

import java.sql.Timestamp;
import java.util.List;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDaoImpl activityDao = new ActivityDaoImpl();
    @Override
    public Activity queryActivityById(int id) {
        return activityDao.queryActivityById(id);
    }

    @Override
    public Page<Activity> pageBySenderId(String id, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityBySenderIdCount(id);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityBySenderId(id, begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Activity> pageByTitle(String title, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityByTitleCount(title);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityByTitle(title,begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Activity> pageByTeam(String team, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityByTeamCount(team);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityByTeam(team,begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Activity> pageByPlace(String place, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityByPlaceCount(place);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityByPlace(place,begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Activity> pageByPlace(double x, double y, double distance, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityByPlaceCount(x, y, distance);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityByPlace(x, y, distance, begin, pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Activity> pageByTime(Timestamp time, int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryActivityByTimeCount(time);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryActivityByTime(time,begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public void addActivity(Activity activity) {
        activityDao.addActivity(activity);
    }

    @Override
    public void deleteActivity(int id) {
        activityDao.deleteActivity(id);
    }

    @Override
    public void updateActivity(Activity activity) {
        activityDao.updateActivity(activity);
    }

    @Override
    public Page<Activity> page(int begin, int pageSize) {
        Page<Activity> page = new Page<Activity>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityDao.queryForPageTotalCount();
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Activity> items = activityDao.queryForPageItems(begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }
}
