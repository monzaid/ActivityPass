package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.dao.ActivityRegisterDao;
import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.ActivityRegister;

import java.util.List;

public class ActivityRegisterDaoImpl extends baseDao implements ActivityRegisterDao {
    @Override
    public int queryActivityRegisterByIdCount(int id) {
        String sql = "select count(*) from t_activity_register where `t_a_id` = ?";
        Number count = (Number) queryForSingleValue(sql, id);
        return count.intValue();
    }

    @Override
    public List<ActivityRegister> queryActivityRegisterById(int id, int begin, int pageSize) {
        String sql = "select * from t_activity_register where `t_a_id` = ? limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(ActivityRegister.class, sql, id, begin, pageSize);
    }

    @Override
    public int queryActivityRegisterByIdCount(String id) {
        String sql = "select count(*) from t_activity_register where `t_u_id` = ?";
        Number count = (Number) queryForSingleValue(sql, id);
        return count.intValue();
    }

    @Override
    public List<ActivityRegister> queryActivityRegisterById(String id, int begin, int pageSize) {
        String sql = "select * from t_activity_register where `t_u_id` = ? limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(ActivityRegister.class, sql, id, begin, pageSize);
    }

    @Override
    public ActivityRegister queryActivityRegisterByActivityIdAndUserId(int ActivityId, String userId) {
        String sql = "select * from t_activity_register where `t_a_id` = ? and `t_u_id` = ?";
        return queryForOne(ActivityRegister.class, sql, ActivityId, userId);
    }

    @Override
    public int saveActivityRegister(ActivityRegister activityRegister) {
        String sql = "insert into t_activity_register values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return update(sql, activityRegister.getT_a_id(), activityRegister.getT_u_id(), activityRegister.getRegister(), activityRegister.isSign_in_face(), activityRegister.isSign_out_face(),
                activityRegister.isSign_in_pos(), activityRegister.isSign_out_pos(), activityRegister.isSign_in_2(), activityRegister.isSing_out_2());
    }

    @Override
    public int updateActivityRegister(ActivityRegister activityRegister) {
        String sql = "update t_activity_register set `register` = ?, `sign_in_face` = ?, `sign_out_face` = ?, `sign_in_pos` = ?, `sign_out_pos` = ?, `sign_in_2` = ?, `sign_out_2` = ? where `t_a_id` = ? and `t_u_id` = ?";
        return update(sql, activityRegister.getRegister(), activityRegister.isSign_in_face(), activityRegister.isSign_out_face(), activityRegister.isSign_in_pos(),
                activityRegister.isSign_out_pos(), activityRegister.isSign_in_2(), activityRegister.isSing_out_2(), activityRegister.getT_a_id(), activityRegister.getT_u_id());
    }

    @Override
    public int deleteActivityRegister(int id, String u_id) {
        String sql = "delete from t_activity_register where `t_a_id` = ? and `t_u_id` = ?";
        return update(sql, id, u_id);
    }
}
