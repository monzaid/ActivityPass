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
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

public class SaveUserImageServlet extends HttpServlet {

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/", "http://159.75.97.188:8080/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "myImage/activityImage/";

    ActivityService activityService = new ActivityServiceImpl();
    MsgService msgService = new MsgServiceImpl();
    ActivityRegisterService activityRegisterService = new ActivityRegisterServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = null;
        Activity activity = new Activity();
        activity.setImage("default.jpg");
        if (ServletFileUpload.isMultipartContent(req)){
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                List<FileItem> list = servletFileUpload.parseRequest(req);
                for (FileItem fileItem: list){
                    if (fileItem.isFormField()){
                        System.out.println("name="+fileItem.getFieldName());
                        System.out.println("value="+fileItem.getString("UTF-8"));
                        if (fileItem.getFieldName().equals("type")){
                            type = new String(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("id")){
                            activity.setId(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("t_u_id")){
                            activity.setT_u_id(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("title")){
                            activity.setTitle(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("introduction")){
                            activity.setIntroduction(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("start_time")){
                            activity.setStart_time(Timestamp.valueOf(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("end_time")){
                            activity.setEnd_time(Timestamp.valueOf(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("people")){
                            activity.setPeople(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("place")){
                            activity.setPlace(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("userName")){
                            activity.setUserName(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("telephone")){
                            activity.setTelephone(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("team")){
                            activity.setTeam(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("astrict")){
                            activity.setAstrict(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("sex")){
                            activity.setSex(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("minAge")){
                            activity.setMinAge(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("maxAge")){
                            activity.setMaxAge(Integer.parseInt(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("district")){
                            activity.setDistrict(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("elseAstrict")){
                            activity.setElseAstrict(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("content")){
                            activity.setContent(fileItem.getString("UTF-8"));
                        }else if (fileItem.getFieldName().equals("pos_x")){
                            activity.setPos_x(Double.parseDouble(fileItem.getString("UTF-8")));
                        }else if (fileItem.getFieldName().equals("pos_y")){
                            activity.setPos_y(Double.parseDouble(fileItem.getString("UTF-8")));
                        }
                    }else{
                        System.out.println("name="+fileItem.getFieldName());
                        System.out.println("filename="+fileItem.getName());
                        fileItem.write(new File("C:\\myImage\\activityImage\\" + fileItem.getName()));
                        activity.setImage(fileItem.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println(activity);
        if (type.equals("create")) {
            activityService.addActivity(activity);
        }else if (type.equals("update")){
            activityService.updateActivity(activity);
            Page<ActivityRegister> page = activityRegisterService.pageById(activity.getId(), 1, 9999);
            for (ActivityRegister activityRegister : page.getItems()) {
                Msg msg = new Msg(activity.getId(), activityRegister.getT_u_id(), -2);
                msgService.add(msg);
            }
        }
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }
}
