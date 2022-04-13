package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.pojo.UserDetail;
import com.example.social_practice_activity_server.service.UserDetailService;
import com.example.social_practice_activity_server.service.UserService;
import com.example.social_practice_activity_server.service.impl.UserDetailServiceImpl;
import com.example.social_practice_activity_server.service.impl.UserServiceImpl;
import com.example.social_practice_activity_server.utils.WebUtils;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends BaseServlet{

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/", "http://159.75.97.188:8080/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "myImage/activityImage/";

    UserService userService = new UserServiceImpl();
    UserDetailService userDetailService = new UserDetailServiceImpl();

    protected void find(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数==封装成为Activity对象
        String id = req.getParameter("id");
        //2 调用BookService.updateBook( book );修改图书
        UserDetail userDetail = userDetailService.find(id);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        o.put("username", userDetail.getName());
        o.put("sex", userDetail.getSex());
        o.put("age", userDetail.getAge());
        o.put("province", userDetail.getProvince());
        o.put("city", userDetail.getCity());
        o.put("district", userDetail.getDistrict());
        o.put("telephone", userDetail.getTelephone());
        o.put("face", userDetail.getFace());
        o.put("face_last_upload_time", userDetail.getFace_last_upload_time().toString());
        writer.write(o.toString());
    }


    protected void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数==封装成为Activity对象
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        if (userService.login(new User(id, password)) == null){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }
        //2 调用BookService.updateBook( book );修改图书
        userService.update(new User(id, newPassword));
        o.put("ac", "true");
        writer.write(o.toString());
    }

    protected void updateDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数==封装成为Activity对象
        UserDetail userDetail = WebUtils.copyParamToBean(req.getParameterMap(),new UserDetail());
        //2 调用BookService.updateBook( book );修改图书
        userDetailService.update(userDetail);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }
}