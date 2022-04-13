package com.example.social_practice_activity_server.service;

import com.example.social_practice_activity_server.pojo.Msg;
import com.example.social_practice_activity_server.pojo.Page;

public interface MsgService {

    /**
     * 用户登录
     * @param msg
     * @return 返回 null 说明没有这个用户， 否则返回一个用户对象
     */
    public void add(Msg msg);

    /**
     * 通过活动标题查询活动
     * @param id
     * @param begin
     * @param pageSize
     * @return 返回 null 说明没有这个活动， 否则返回页面对象
     */
    public Page<Msg> pageById(String id, int begin, int pageSize);
}