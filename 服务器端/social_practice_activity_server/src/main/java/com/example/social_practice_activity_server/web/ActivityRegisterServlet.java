package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.pojo.*;
import com.example.social_practice_activity_server.service.ActivityRegisterService;
import com.example.social_practice_activity_server.service.ActivityService;
import com.example.social_practice_activity_server.service.MsgService;
import com.example.social_practice_activity_server.service.UserDetailService;
import com.example.social_practice_activity_server.service.impl.ActivityRegisterServiceImpl;
import com.example.social_practice_activity_server.service.impl.ActivityServiceImpl;
import com.example.social_practice_activity_server.service.impl.MsgServiceImpl;
import com.example.social_practice_activity_server.service.impl.UserDetailServiceImpl;
import com.example.social_practice_activity_server.utils.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ActivityRegisterServlet extends BaseServlet{
    static private final String[] Host = new String[]{"http://192.168.137.1:8080/", "http://159.75.97.188:8080/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "myImage/activityImage/";

    ActivityService activityService = new ActivityServiceImpl();
    ActivityRegisterService activityRegisterService = new ActivityRegisterServiceImpl();
    UserDetailService userDetailService = new UserDetailServiceImpl();
    MsgService msgService = new MsgServiceImpl();

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageByUserId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        String userId = req.getParameter("userId");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        System.out.println(userId + " " + pageNo + " " + pageSize);

        Page<ActivityRegister> page = activityRegisterService.pageByT_u_id(userId,pageNo,pageSize);
        Page<Activity> page1 = activityRegisterService.getActivityByActivityRegister(page);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        o.put("pageCount", page.getPageTotal());
        JSONArray jsonArray = new JSONArray();
        for (Activity a : page1.getItems()){
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

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void pageBySenderId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        String userId = req.getParameter("userId");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);

        Page<Activity> page = activityService.pageBySenderId(userId, pageNo, pageSize);

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

    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void manage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        int ActivityId = Integer.parseInt(req.getParameter("ActivityId"));
        String userId = req.getParameter("userId");
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);

        Page<ActivityRegister> page = activityRegisterService.pageById(ActivityId, pageNo, pageSize);


        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        o.put("pageCount", page.getPageTotal());
        JSONArray jsonArray = new JSONArray();

        for (ActivityRegister activityRegister : page.getItems()){
            System.out.println(activityRegister);
            JSONObject result = new JSONObject();
            result.put("id", activityRegister.getT_u_id());
            result.put("register", activityRegister.getRegister());
            result.put("sign_in_pos", activityRegister.isSign_in_pos());
            result.put("sign_in_face", activityRegister.isSign_in_face());
            result.put("sign_out_pos", activityRegister.isSign_out_pos());
            result.put("sign_out_face", activityRegister.isSign_out_face());
            result.put("sign_in_2", activityRegister.isSign_in_2());
            result.put("sing_out_2", activityRegister.isSing_out_2());
            UserDetail userDetail = userDetailService.find(activityRegister.getT_u_id());
            System.out.println(userDetail);
            result.put("username", userDetail.getName());
            result.put("telephone", userDetail.getTelephone());
            if (activityRegister.getRegister() == 1){
                result.put("stute", "等待审核");
            }else if (activityRegister.getRegister() == 2){
                if (activityRegister.isSign_in_pos() && activityRegister.isSign_in_face()){
                    result.put("stute", "已签到");
                }else{
                    result.put("stute", "待签到");
                }
            }else if (activityRegister.getRegister() == 3){
                if (activityRegister.isSign_out_pos() && activityRegister.isSign_out_face()){
                    result.put("stute", "已签退");
                }else{
                    result.put("stute", "待签退");
                }
            }else if (activityRegister.getRegister() == 4){
                result.put("stute", "已完成");
            }
            jsonArray.add(result);
        }
        o.element("data", jsonArray);
        writer.write(o.toString());
    }

    protected void getTime(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        int pageNo = 1;
        int pageSize = 9999;
        System.out.println(userId + " " + pageNo + " " + pageSize);

        Page<ActivityRegister> page = activityRegisterService.pageByT_u_id(userId,pageNo,pageSize);
        Page<Activity> page1 = activityRegisterService.getActivityByActivityRegister(page);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        for (Activity a : page1.getItems()){
            if (!(page.getItems().get(i).getRegister() == 2 || page.getItems().get(i).getRegister() == 3)){
                i++;
                continue;
            }
            JSONObject result = new JSONObject();
            result.put("start_time", a.getStart_time().toString());
            result.put("end_time", a.getEnd_time().toString());
            result.put("title", a.getTitle());
            result.put("activityId", a.getId());
            jsonArray.add(result);
            i++;
        }
        o.element("data", jsonArray);
        writer.write(o.toString());

    }


    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数活动编号
        int activityId = WebUtils.parseInt(req.getParameter("activityId"), 0);
        String userId = req.getParameter("userId");
        String actionId = req.getParameter("actionId");

        activityRegisterService.delete(activityId, userId);
        if (actionId.equals(userId)){
            Activity activity = activityService.queryActivityById(activityId);
            Msg msg = new Msg(activityId, activity.getT_u_id(), 0);
            msgService.add(msg);
        }else {
            Msg msg = new Msg(activityId, userId, 0);
            msgService.add(msg);
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }



    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数==封装成为Activity对象
        ActivityRegister activityRegister = WebUtils.copyParamToBean(req.getParameterMap(),new ActivityRegister());

        activityRegisterService.update(activityRegister);
        if (activityRegister.getRegister() == 2 && !activityRegister.isSign_in_face() && !activityRegister.isSign_in_pos()) {
            Msg msg = new Msg(activityRegister.getT_a_id(), activityRegister.getT_u_id(), activityRegister.getRegister());
            msgService.add(msg);
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }

    protected void joinActivity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数活动编号
        int activityId = WebUtils.parseInt(req.getParameter("activityId"), 0);
        String userId = req.getParameter("userId");

        boolean ac = true;
        Activity activity = activityService.queryActivityById(activityId);
        UserDetail userDetail = null;
        if (activity.getAstrict() == 1){
            userDetail = userDetailService.find(userId);
            if (activity.getSex() != 2 && activity.getSex() != userDetail.getSex()){
                ac = false;
            }else if (activity.getMinAge() != -1 && userDetail.getAge() < activity.getMinAge()){
                ac = false;
            }else if (activity.getMaxAge() != -1 && userDetail.getAge() > activity.getMaxAge()){
                ac = false;
            }else if (!activity.getDistrict().equals("无限制")){
                if (activity.getDistrict().equals("本省范围内") && !activity.getPlace().contains(userDetail.getProvince())){
                    ac = false;
                }else if (activity.getDistrict().equals("本市范围内") && !activity.getPlace().contains(userDetail.getCity())){
                    ac = false;
                }else if (activity.getDistrict().equals("本区范围内") && !activity.getPlace().contains(userDetail.getDistrict())){
                    ac = false;
                }
            }else if (userDetail.getFace().equals("未认证")){
                ac = false;
            }
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        System.out.println(ac);

        if (!ac){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }

        ActivityRegister activityRegister = new ActivityRegister(activityId, userId);
        activityRegisterService.join(activityRegister);
        Msg msg = new Msg(activityId, activity.getT_u_id(), 1);
        msgService.add(msg);
        o.put("ac", "true");
        writer.write(o.toString());
    }

    protected void getActivityRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数活动编号
        int activityId = WebUtils.parseInt(req.getParameter("activityId"), 0);
        String userId = req.getParameter("userId");

        ActivityRegister activityRegister = activityRegisterService.queryByActivityIdAndUserId(activityId, userId);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        if (activityRegister == null){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }
        o.put("ac", "true");
        o.put("register", activityRegister.getRegister());
        o.put("sign_in_pos", activityRegister.isSign_in_pos());
        o.put("sign_in_face", activityRegister.isSign_in_face());
        o.put("sign_out_pos", activityRegister.isSign_out_pos());
        o.put("sign_out_face", activityRegister.isSign_out_face());
        o.put("sign_in_2", activityRegister.isSign_in_2());
        o.put("sing_out_2", activityRegister.isSing_out_2());
//        if (activityRegister.getRegister() == 1){
//            Activity activity = activityService.queryActivityById(activityId);
//            o.put("start_time", activity.getStart_time());
//            o.put("end_time", activity.getEnd_time());
//        }
        writer.write(o.toString());
    }
}
