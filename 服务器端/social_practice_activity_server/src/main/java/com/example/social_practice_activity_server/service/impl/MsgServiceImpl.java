package com.example.social_practice_activity_server.service.impl;

import com.example.social_practice_activity_server.dao.impl.MsgDaoImpl;
import com.example.social_practice_activity_server.pojo.Msg;
import com.example.social_practice_activity_server.pojo.Page;
import com.example.social_practice_activity_server.service.MsgService;

import java.util.List;

public class MsgServiceImpl implements MsgService {
    private MsgDaoImpl msgDao = new MsgDaoImpl();

    @Override
    public void add(Msg msg) {
        msgDao.saveMsg(msg);
    }

    @Override
    public Page<Msg> pageById(String id, int begin, int pageSize) {
        Page<Msg> page = new Page<Msg>();

        // 设置每页显示的数量
        page.setPageSize(pageSize);
        // 求总记录数
        int pageTotalCount = msgDao.queryMsgByIdCount(id);
        // 设置总记录数
        page.setPageTotalCount(pageTotalCount);
        // 求总页码
        page.setPageTotalByPageTotalCount();

        // 设置当前页码
        page.setPageNo(begin);

        // 求当前页数据
        List<Msg> items = msgDao.queryMsgById(id, begin,pageSize);
        // 设置当前页数据
        page.setItems(items);

        return page;
    }
}
