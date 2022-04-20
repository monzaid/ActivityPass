package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.pojo.*;
import com.example.social_practice_activity_server.service.ActivityService;
import com.example.social_practice_activity_server.service.MsgService;
import com.example.social_practice_activity_server.service.impl.ActivityServiceImpl;
import com.example.social_practice_activity_server.service.impl.MsgServiceImpl;
import com.example.social_practice_activity_server.utils.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MsgServlet extends HttpServlet {
    private MsgService msgService = new MsgServiceImpl();
    private ActivityService activityService = new ActivityServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        System.out.println("host = "+req.getRemoteHost() + " Method = "+req.getMethod() + " Servlet = " + this.getClass());
        String id = req.getParameter("id");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Msg> page = msgService.pageById(id, pageNo, pageSize);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        o.put("ac", "true");
        o.put("pageCount", page.getPageTotal());
        JSONArray jsonArray = new JSONArray();
        for (Msg msg : page.getItems()){
            Activity activity = activityService.queryActivityById(msg.getT_a_id());
            JSONObject result = new JSONObject();
            result.put("t_a_id", msg.getT_a_id());
            result.put("title", activity.getTitle());
            result.put("type", msg.getType());
            result.put("time", msg.getTime().toString());
            jsonArray.add(result);
        }
        o.element("data", jsonArray);
        writer.write(o.toString());
    }
}