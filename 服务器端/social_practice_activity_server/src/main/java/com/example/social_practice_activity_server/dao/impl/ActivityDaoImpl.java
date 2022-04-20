package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.dao.ActivityDao;
import com.example.social_practice_activity_server.pojo.Activity;

import java.sql.Timestamp;
import java.util.List;

public class ActivityDaoImpl extends baseDao implements ActivityDao {
    @Override
    public Activity queryActivityById(int id) {
        String sql = "select * from t_activity where `id` = ?";
        return queryForOne(Activity.class, sql, id);
    }

    @Override
    public int queryActivityBySenderIdCount(String id) {
        String sql = "select count(*) from t_activity where `t_u_id` = ?";
        Number count = (Number) queryForSingleValue(sql, id);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityBySenderId(String id, int begin, int pageSize) {
        String sql = "select * from t_activity where `t_u_id` = ? limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, id, begin, pageSize);
    }

    @Override
    public int queryActivityByTitleCount(String title) {
        String sql = "select count(*) from t_activity where `title` like ?";
        String like_title = "%" + title + "%";
        Number count = (Number) queryForSingleValue(sql, like_title);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityByTitle(String title, int begin, int pageSize) {
        String sql = "select * from t_activity where `title` like ? limit ?, ?";
        String like_title = "%" + title + "%";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, like_title, begin, pageSize);
    }

    @Override
    public int queryActivityByTeamCount(String team) {
        String sql = "select count(*) from t_activity where `team` like ?";
        String like_team = "%" + team + "%";
        Number count = (Number) queryForSingleValue(sql, like_team);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityByTeam(String team, int begin, int pageSize) {
        String sql = "select * from t_activity where `team` like ? limit ?, ?";
        String like_team = "%" + team + "%";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, like_team, begin, pageSize);
    }

    @Override
    public int queryActivityByPlaceCount(String place) {
        String sql = "select count(*) from t_activity where `place` like ?";
        String like_place = "%" + place + "%";
        Number count = (Number) queryForSingleValue(sql, like_place);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityByPlace(String place, int begin, int pageSize) {
        String sql = "select * from t_activity where `place` like ? limit ?, ?";
        String like_place = "%" + place + "%";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, like_place, begin, pageSize);
    }

    @Override
    public int queryActivityByPlaceCount(double x, double y, double distance) {
        String sql = "select count(*) from t_activity where `pos_x` >= ? and `pos_x` <= ? and `pos_y` >= ? and `pos_y` <= ?";
        double x_max = x + distance, x_min = x - distance;
        double y_max = y + distance, y_min = y - distance;
        Number count = (Number) queryForSingleValue(sql, x_min, x_max, y_min, y_max);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityByPlace(double x, double y, double distance, int begin, int pageSize) {
        String sql = "select * from t_activity where `pos_x` >= ? and `pos_x` <= ? and `pos_y` >= ? and `pos_y` <= ? limit ?, ?";
        double x_max = x + distance, x_min = x - distance;
        double y_max = y + distance, y_min = y - distance;
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, x_min, x_max, y_min, y_max, begin, pageSize);
    }

    @Override
    public int queryActivityByTimeCount(Timestamp time) {
        String sql = "select count(*) from t_activity where `start_time` >= ?";
        Number count = (Number) queryForSingleValue(sql, time);
        return count.intValue();
    }

    @Override
    public List<Activity> queryActivityByTime(Timestamp time, int begin, int pageSize) {
        String sql = "select * from t_activity where `start_time` >= ? limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, time, begin, pageSize);
    }

//    @Override
//    public List<Activity> queryActivityByTitle(String title) {
//        String sql = "select * from t_activity where `title` like ?";
//        String like_title = "%" + title + "%";
//        return queryForAll(Activity.class, sql, like_title);
//    }
//
//    @Override
//    public List<Activity> queryActivityByTeam(String team) {
//        String sql = "select * from t_activity where `team` like ?";
//        String like_team = "%" + team + "%";
//        return queryForAll(Activity.class, sql, like_team);
//    }
//
//    @Override
//    public List<Activity> queryActivityByPlace(String place) {
//        String sql = "select * from t_activity where `place` like ?";
//        String like_place = "%" + place + "%";
//        return queryForAll(Activity.class, sql, like_place);
//    }
//
//    @Override
//    public List<Activity> queryActivityByTime(Timestamp time) {
//        String sql = "select * from t_activity where `start_time` >= unix_timestamp(?)";
////        Timestamp timestamp = new Timestamp(time.getTimeInMillis());
//        return queryForAll(Activity.class, sql, time);
//    }

    @Override
    public int addActivity(Activity activity) {
        String sql = "insert into t_activity values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return update(sql, null, activity.getT_u_id(), activity.getTitle(), activity.getIntroduction(), activity.getContent(), activity.getImage(),
                activity.getTeam(), activity.getUserName(), activity.getTelephone(), activity.getPlace(), activity.getPeople(), activity.getStart_time(), activity.getEnd_time(), activity.getPos_x(), activity.getPos_y(),
                activity.getAstrict(), activity.getSex(), activity.getMaxAge(), activity.getMinAge(), activity.getDistrict(), activity.getElseAstrict());
    }

    @Override
    public int deleteActivity(int id) {
        String sql = "delete from t_activity where `id` = ?";
        return update(sql, id);
    }

    @Override
    public int updateActivity(Activity activity) {
        String sql = "update t_activity set `title` = ?, `introduction` = ?, `content` = ?, `image` = ?, `team` = ?, `username` = ?, `telephone` = ?, `place` = ?, `people` = ?, `start_time` = ?, `end_time` = ?, `astrict` = ?, `sex` = ?, `maxAge` = ?, `minAge` = ?, `district` = ?, `elseAstrict` = ? where `id` = ?";
        return update(sql, activity.getTitle(), activity.getIntroduction(), activity.getContent(), activity.getImage(),
                activity.getTeam(), activity.getUserName(), activity.getTelephone(), activity.getPlace(), activity.getPeople(), activity.getStart_time(), activity.getEnd_time(),
                activity.getAstrict(), activity.getSex(), activity.getMaxAge(), activity.getMinAge(), activity.getDistrict(), activity.getElseAstrict(), activity.getId());
    }

    @Override
    public int queryForPageTotalCount() {
        String sql = "select count(*) from t_activity";
        Number count = (Number) queryForSingleValue(sql);
        return count.intValue();
    }

    @Override
    public List<Activity> queryForPageItems(int begin, int pageSize) {
        String sql = "select * from t_activity limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(Activity.class, sql, begin, pageSize);
    }
}
