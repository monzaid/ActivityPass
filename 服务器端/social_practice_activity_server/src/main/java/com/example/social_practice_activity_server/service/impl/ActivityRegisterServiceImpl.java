package com.example.social_practice_activity_server.service.impl;

import com.example.social_practice_activity_server.dao.ActivityDao;
import com.example.social_practice_activity_server.dao.ActivityRegisterDao;
import com.example.social_practice_activity_server.dao.impl.ActivityDaoImpl;
import com.example.social_practice_activity_server.dao.impl.ActivityRegisterDaoImpl;
import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.ActivityRegister;
import com.example.social_practice_activity_server.pojo.Page;
import com.example.social_practice_activity_server.service.ActivityRegisterService;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegisterServiceImpl implements ActivityRegisterService {
    ActivityRegisterDao activityRegisterDao = new ActivityRegisterDaoImpl();
    ActivityDao activityDao = new ActivityDaoImpl();

    @Override
    public Page<ActivityRegister> pageById(int id, int begin, int pageSize) {
        Page<ActivityRegister> page = new Page<ActivityRegister>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityRegisterDao.queryActivityRegisterByIdCount(id);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<ActivityRegister> items = activityRegisterDao.queryActivityRegisterById(id, begin, pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }

    @Override
    public Page<ActivityRegister> pageByT_u_id(String id, int begin, int pageSize) {
        Page<ActivityRegister> page = new Page<ActivityRegister>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = activityRegisterDao.queryActivityRegisterByIdCount(id);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<ActivityRegister> items = activityRegisterDao.queryActivityRegisterById(id, begin, pageSize);
        // 设置当前页数据
        page.setItems(items);
        return page;
    }

    @Override
    public Page<Activity> getActivityByActivityRegister(Page<ActivityRegister> page) {
        Page<Activity> page1 = new Page<Activity>();
        page1.setPageSize(page.getPageSize());
        page1.setPageTotalCount(page.getPageTotalCount());
        page1.setPageTotal(page.getPageTotal());
        page1.setPageNo(page.getPageNo());
        List<Activity> items = new ArrayList<Activity>();
        for (ActivityRegister activityRegister : page.getItems()){
            Activity activity = activityDao.queryActivityById(activityRegister.getT_a_id());
            items.add(activity);
        }
        page1.setItems(items);
        return page1;
    }

    @Override
    public ActivityRegister queryByActivityIdAndUserId(int ActivityId, String userId) {
        return activityRegisterDao.queryActivityRegisterByActivityIdAndUserId(ActivityId, userId);
    }

    @Override
    public void join(ActivityRegister activityRegister) {
        activityRegisterDao.saveActivityRegister(activityRegister);
    }

    @Override
    public void update(ActivityRegister activityRegister) {
        activityRegisterDao.updateActivityRegister(activityRegister);
    }

    @Override
    public void delete(int id, String u_id) {
        activityRegisterDao.deleteActivityRegister(id, u_id);
    }
}
