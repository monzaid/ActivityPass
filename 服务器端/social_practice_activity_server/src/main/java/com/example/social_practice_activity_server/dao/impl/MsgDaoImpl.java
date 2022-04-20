package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.dao.MsgDao;
import com.example.social_practice_activity_server.dao.UserDao;
import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.Msg;
import com.example.social_practice_activity_server.pojo.User;

import java.sql.Timestamp;
import java.util.List;

public class MsgDaoImpl extends baseDao implements MsgDao {
    @Override
    public int saveMsg(Msg msg) {
        String sql = "insert into t_msg values(?, ?, ?, ?)";
        return update(sql, msg.getT_a_id(), msg.getT_u_id(), msg.getType(), new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public int queryMsgByIdCount(String id) {
        String sql = "select count(*) from t_msg where `t_u_id` = ?";
        Number count = (Number) queryForSingleValue(sql, id);
        return count.intValue();
    }

    @Override
    public List<Msg> queryMsgById(String id, int begin, int pageSize) {
        String sql = "select * from t_msg where `t_u_id` = ? order by `time` desc limit ?, ?";
        begin = (begin - 1) * pageSize;
        return queryForAll(Msg.class, sql, id, begin, pageSize);
    }
}