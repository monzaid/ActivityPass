package com.example.social_practice_activity_server.test;

import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.service.ActivityService;
import com.example.social_practice_activity_server.service.impl.ActivityServiceImpl;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class ActivityServiceTest {
    ActivityService activityService = new ActivityServiceImpl();
    @Test
    public void queryActivityById() {
        System.out.println(activityService.queryActivityById(7));
    }

    @Test
    public void pageByTitle() {
    }

    @Test
    public void pageByTeam() {
    }

    @Test
    public void pageByPlace() {
        System.out.println(activityService.pageByPlace(112.9792640000000000, 23.9985140000000000, 1, 1, 6));
    }

    @Test
    public void pageByTime() {
    }

    @Test
    public void addActivity() {
//        activityService.addActivity(new Activity(1, "43", "sdfsad", "sdfdaaa", "sdfsdf", "cxzvzs", "sdatwa", "sadfsg", "cdfgbs", "asg", 444, Timestamp.valueOf("2018-02-02 00:00:00"), Timestamp.valueOf("2018-02-02 01:00:00"), 112.9792640000000000, 23.9985140000000000));
    }

    @Test
    public void deleteActivity() {
        activityService.deleteActivity(18);
    }

    @Test
    public void updateActivity() {
//        activityService.updateActivity(new Activity(18, "43", "sdfsad", "sdfdaaa", "sdfsdf", "cxzvzs", "sdatwa", "11111", "cdfgbs", "asg", 444, Timestamp.valueOf("2018-02-02 00:00:00"), Timestamp.valueOf("2018-02-02 01:00:00"), 112.9792640000000000, 23.9985140000000000));
    }

    @Test
    public void page() {
        System.out.println(activityService.page(2, 5));
    }
}