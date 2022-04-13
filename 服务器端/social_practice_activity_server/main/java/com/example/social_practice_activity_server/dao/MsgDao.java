package com.example.social_practice_activity_server.dao;

import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.Msg;

import java.util.List;

public interface MsgDao {
    /**
     * 保存用户信息
     * @param msg 用户对象
     * @return 返回 -1 说明保存失败， 否则返回 1
     */
    public int saveMsg(Msg msg);
    /**
     * 通过活动标题查询活动总数
     * @param id
     * @return 活动总数
     */
    public int queryMsgByIdCount(String id);
    /**
     * 通过活动标题查询活动
     * @param id
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回活动对象
     */
    public List<Msg> queryMsgById(String id, int begin, int pageSize);
}