package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.pojo.User;
import com.example.social_practice_activity_server.pojo.UserDetail;
import com.example.social_practice_activity_server.service.UserDetailService;
import com.example.social_practice_activity_server.service.UserService;
import com.example.social_practice_activity_server.service.impl.UserDetailServiceImpl;
import com.example.social_practice_activity_server.service.impl.UserServiceImpl;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private UserDetailService userDetailService = new UserDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        req.setCharacterEncoding("utf-8");
        System.out.println("host = "+req.getRemoteHost() + " Method = "+req.getMethod() + " Servlet = " + this.getClass());
        String id = req.getParameter("id");
        String password = req.getParameter("password");

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        if (!userService.isExistId(id)){
            System.out.println("登录事件：用户id " + id + " 已存在！");
            o.put("ac", "false");
            o.put("type", "noUser");
            writer.write(o.toString());
            return;
        }

        User user = userService.login(new User(id, password));

        if (user == null){
            System.out.println("登录事件：用户id " + id + " 用户名错误 或者是 密码错误！");
            o.put("ac", "false");
            o.put("type", "error");
            writer.write(o.toString());
            return;
        }
        UserDetail userDetail = userDetailService.find(id);
        System.out.println("登录事件：用户id " + id + " 登录成功！");
        o.put("ac", "true");
        o.put("username", userDetail.getName());
        writer.write(o.toString());
    }
}