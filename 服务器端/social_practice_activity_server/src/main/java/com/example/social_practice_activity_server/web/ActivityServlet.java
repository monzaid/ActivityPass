package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.pojo.Activity;
import com.example.social_practice_activity_server.pojo.ActivityRegister;
import com.example.social_practice_activity_server.pojo.Msg;
import com.example.social_practice_activity_server.pojo.Page;
import com.example.social_practice_activity_server.service.ActivityRegisterService;
import com.example.social_practice_activity_server.service.ActivityService;
import com.example.social_practice_activity_server.service.MsgService;
import com.example.social_practice_activity_server.service.impl.ActivityRegisterServiceImpl;
import com.example.social_practice_activity_server.service.impl.ActivityServiceImpl;
import com.example.social_practice_activity_server.service.impl.MsgServiceImpl;
import com.example.social_practice_activity_server.utils.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;

public class ActivityServlet extends BaseServlet{
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/", "http://159.75.97.188:8080/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "myImage/activityImage/";

    ActivityService activityService = new ActivityServiceImpl();
    ActivityRegisterService activityRegisterService = new ActivityRegisterServiceImpl();
    MsgService msgService = new MsgServiceImpl();

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByTitle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        String title = req.getParameter("content");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.pageByTitle(title,pageNo,pageSize);

        outputDetail(resp, page);
    }

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByTeam(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        String team = req.getParameter("content");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.pageByTeam(team,pageNo,pageSize);

        outputDetail(resp, page);
    }

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.page(pageNo,pageSize);

        outputDetail(resp, page);
    }

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByPlace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        String place = req.getParameter("content");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.pageByPlace(place,pageNo,pageSize);

        outputDetail(resp, page);
    }
    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByTime(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        Timestamp start_time = Timestamp.valueOf(req.getParameter("content"));
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.pageByTime(start_time,pageNo,pageSize);

        outputDetail(resp, page);
    }

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByPos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        double x = Double.valueOf(req.getParameter("pos_x"));
        double y = Double.valueOf(req.getParameter("pos_y"));
        double distance = Double.valueOf(req.getParameter("distance"));
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        String isCount = req.getParameter("pageCount");

        Page<Activity> page = activityService.pageByPlace(x, y, distance, pageNo, pageSize);

        outputDetail(resp, page);
    }

    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Activity activity = WebUtils.copyParamToBean(req.getParameterMap(),new Activity());
        activityService.addActivity(activity);

        outputAC(resp);
    }



    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        Page<ActivityRegister> page = activityRegisterService.pageById(id, 1, 9999);
        for (ActivityRegister activityRegister : page.getItems()) {
            String userId = activityRegister.getT_u_id();
            int activityId = activityRegister.getT_a_id();
            activityRegisterService.delete(activityId, userId);
            Msg msg = new Msg(id, activityRegister.getT_u_id(), -1);
            msgService.add(msg);
        }
        activityService.deleteActivity(id);

        outputAC(resp);
    }



    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Activity activity = WebUtils.copyParamToBean(req.getParameterMap(),new Activity());
        activityService.updateActivity(activity);
        Page<ActivityRegister> page = activityRegisterService.pageById(activity.getId(), 1, 9999);
        for (ActivityRegister activityRegister : page.getItems()) {
            Msg msg = new Msg(activity.getId(), activityRegister.getT_u_id(), -2);
            msgService.add(msg);
        }

        outputAC(resp);
    }



    protected void getActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        System.out.println(id);
        Activity activity = activityService.queryActivityById(id);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        if (activity == null){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }
        o.put("ac", "true");
        JSONObject result = new JSONObject();
        result.put("id", activity.getId());
        result.put("t_u_id", activity.getT_u_id());
        result.put("username", activity.getUserName());
        result.put("content", activity.getContent());
        result.put("team", activity.getTeam());
        result.put("telephone", activity.getTelephone());
        result.put("end_time", activity.getEnd_time().toString());
        result.put("image", Url + activity.getImage());
        result.put("title", activity.getTitle());
        result.put("introduction", activity.getIntroduction());
        result.put("place", activity.getPlace());
        result.put("start_time", activity.getStart_time().toString());
        result.put("people", activity.getPeople());
        result.put("pos_x", activity.getPos_x());
        result.put("pos_y", activity.getPos_y());
        result.put("astrict", activity.getAstrict());
        if (activity.getAstrict() == 1){
            result.put("sex", activity.getSex());
            result.put("maxAge", activity.getMaxAge());
            result.put("minAge", activity.getMinAge());
            result.put("district", activity.getDistrict());
            result.put("elseAstrict", activity.getElseAstrict());
        }
        o.element("activity", result);
        writer.write(o.toString());
    }

    private void outputAC(HttpServletResponse resp) throws IOException{
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }

    private void outputDetail(HttpServletResponse resp, Page<Activity> page) throws IOException{
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        o.put("pageCount", page.getPageTotal());
        JSONArray jsonArray = new JSONArray();
        for (Activity a : page.getItems()){
            JSONObject result = new JSONObject();
            result.put("id", a.getId());
            result.put("image", Url + a.getImage());
            result.put("title", a.getTitle());
            result.put("introduction", a.getIntroduction());
            result.put("place", a.getPlace());
            result.put("start_time", a.getStart_time().toString());
            result.put("people", a.getPeople());
            jsonArray.add(result);
        }
        o.element("data", jsonArray);
        writer.write(o.toString());
    }
}
