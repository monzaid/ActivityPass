package com.example.social_practice_activity_server.service;

import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.ActivityRegister;
import com.example.social_practice_activity_server.pojo.Page;

public interface ActivityRegisterService {

    public Page<ActivityRegister> pageById(int id, int begin, int pageSize);

    public Page<ActivityRegister> pageByT_u_id(String id, int begin, int pageSize);

    public Page<Activity> getActivityByActivityRegister(Page<ActivityRegister> page);

    public ActivityRegister queryByActivityIdAndUserId(int ActivityId, String userId);

    public void join(ActivityRegister activityRegister);

    public void update(ActivityRegister activityRegister);

    public void delete(int id, String u_id);
}