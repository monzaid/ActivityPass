package com.example.social_practice_activity_server.test;

import com.example.social_practice_activity_server.dao.ActivityDao;
import com.example.social_practice_activity_server.dao.impl.ActivityDaoImpl;
import com.example.social_practice_activity_server.pojo.Activity;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

public class ActivityDaoImplTest {

    ActivityDao activityDao = new ActivityDaoImpl();

    @Test
    public void queryActivityByTitleCount() {
        System.out.println(activityDao.queryActivityByTitleCount("da"));

    }

    @Test
    public void queryActivityByTitle() {
        List<Activity> activities = activityDao.queryActivityByTitle("da", 2, 2);
        for (Activity activity : activities){
            System.out.println(activity);
        }
    }

    @Test
    public void queryActivityByTeamCount() {
        System.out.println(activityDao.queryActivityByTeamCount("sa"));
    }

    @Test
    public void queryActivityByTeam() {
        List<Activity> activities = activityDao.queryActivityByTeam("sa", 3, 2);
        for (Activity activity : activities){
            System.out.println(activity);
        }
    }

    @Test
    public void queryActivityByPlaceCount() {
        System.out.println(activityDao.queryActivityByPlaceCount("格"));
        System.out.println("---------");
        System.out.println(activityDao.queryActivityByPlaceCount(112.4792640000000000, 23.0985140000000000, 1));
    }

    @Test
    public void queryActivityByPlace() {
        List<Activity> activities = activityDao.queryActivityByPlace("格", 1, 2);
        for (Activity activity : activities){
            System.out.println(activity);
        }
        System.out.println("---------");
        List<Activity> activitiess = activityDao.queryActivityByPlace(112.4792640000000000, 23.0985140000000000, 1, 1, 6);
        for (Activity activity : activitiess){
            System.out.println(activity);
        }
    }

    @Test
    public void queryActivityByTimeCount() {
        System.out.println(activityDao.queryActivityByTimeCount(Timestamp.valueOf("2018-02-02 00:00:00")));
    }

    @Test
    public void queryActivityByTime() {
        List<Activity> activities = activityDao.queryActivityByTime(Timestamp.valueOf("2018-02-02 00:00:00"), 1, 20);
        for (Activity activity : activities){
            System.out.println(activity);
        }
    }

    @Test
    public void queryActivityById() {
        System.out.println(activityDao.queryActivityById(2).toString());
    }

//    @Test
//    public void queryActivityByTitle() {
//        List<Activity> activities = activityDao.queryActivityByTitle("dad");
//        for (Activity activity : activities){
//            System.out.println(activity);
//        }
//    }
//
//    @Test
//    public void queryActivityByTeam() {
//        List<Activity> activities = activityDao.queryActivityByTeam("的");
//        for (Activity activity : activities){
//            System.out.println(activity);
//        }
//
//    }
//
//    @Test
//    public void queryActivityByPlace() {
//        List<Activity> activities = activityDao.queryActivityByPlace("的");
//        System.out.println(activities);
//        System.out.println(activityDao.queryActivityByPlace("的"));
//        for (Activity activity : activities){
//            System.out.println(activity);
//        }
//    }
//
//    @Test
//    public void queryActivityByTime() {
//        System.out.println(activityDao.queryActivityByTime(Timestamp.valueOf("2018-02-02 00:00:00")));
//        List<Activity> activities = activityDao.queryActivityByTime(Timestamp.valueOf("2018-02-02 00:00:00"));
//        for (Activity activity : activities){
//            System.out.println(activity);
//        }
//    }

    @Test
    public void addActivity() {
//        activityDao.addActivity(new Activity(1, "43", "sdfsad", "sdfdaaa", "sdfsdf", "cxzvzs", "sdatwa", "sadfsg", "cdfgbs", "asg", 444, Timestamp.valueOf("2018-02-02 00:00:00"), Timestamp.valueOf("2018-02-02 01:00:00"), 112.9792640000000000, 23.9985140000000000));
    }

    @Test
    public void deleteActivity() {
        activityDao.deleteActivity(7);
    }

    @Test
    public void updateActivity() {
//        activityDao.updateActivity(new Activity(6, "43", "sdfsad", "qqqqq", "sdfsdf", "cxzvzs", "sdatwa", "sadfsg", "cdfgbs", "asg", 444, Timestamp.valueOf("2018-02-02 00:00:00"), Timestamp.valueOf("2018-02-02 01:00:00"), 112.9792640000000000, 23.9985140000000000));
    }

    @Test
    public void queryForPageTotalCount(){
        System.out.println(activityDao.queryForPageTotalCount());
    }

    @Test
    public void queryForPageItems(){
        List<Activity> activities = activityDao.queryForPageItems(1, 3);
        for (Activity activity : activities){
            System.out.println(activity);
        }
    }
}