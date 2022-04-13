package com.example.social_practice_activity_server.dao.impl;

import com.example.social_practice_activity_server.dao.UserDetailDao;
import com.example.social_practice_activity_server.pojo.UserDetail;

import java.sql.Timestamp;

public class UserDetailDaoImpl extends baseDao implements UserDetailDao {
    @Override
    public UserDetail queryUserDetailById(String id) {
        String sql = "select * from t_user_details where `id` = ?";
        return queryForOne(UserDetail.class, sql, id);
    }

    @Override
    public int saveUserDetail(UserDetail userDetail) {
        String sql = "insert into t_user_details values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return update(sql, userDetail.getId(), userDetail.getName(), userDetail.getSex(), userDetail.getAge(), userDetail.getProvince(),
                userDetail.getCity(), userDetail.getDistrict(), userDetail.getTelephone(), userDetail.getFace(), userDetail.getFace_last_upload_time());
    }

    @Override
    public int updateUserDetail(UserDetail userDetail) {
        String sql = "update t_user_details set `name` = ?, `sex` = ?, `age` = ?, `province` = ?, `city` = ?, `district` = ?, `telephone` = ? where `id` = ?";
        return update(sql, userDetail.getName(), userDetail.getSex(), userDetail.getAge(), userDetail.getProvince(),
                userDetail.getCity(), userDetail.getDistrict(), userDetail.getTelephone(), userDetail.getId());
    }

    @Override
    public int updateUserDetailByFace(String face, String id) {
        String sql = "update t_user_details set `face` = ?, `face_last_upload_time` = ? where `id` = ?";
        return update(sql, face == "未认证" ? "本月还可以上传1次" : "本月还可以上传0次", new Timestamp(System.currentTimeMillis()), id);
    }
}