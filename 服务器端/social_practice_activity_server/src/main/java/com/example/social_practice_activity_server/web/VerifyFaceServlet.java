package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.face.dap.Result;
import com.example.social_practice_activity_server.face.pojo.AIFaceUtils;
import com.example.social_practice_activity_server.face.pojo.CreatePerson;
import com.example.social_practice_activity_server.face.utils.Base64ConvertUtils;
import com.example.social_practice_activity_server.pojo.ActivityRegister;
import com.example.social_practice_activity_server.pojo.UserDetail;
import com.example.social_practice_activity_server.service.ActivityRegisterService;
import com.example.social_practice_activity_server.service.UserDetailService;
import com.example.social_practice_activity_server.service.impl.ActivityRegisterServiceImpl;
import com.example.social_practice_activity_server.service.impl.UserDetailServiceImpl;
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
import java.util.List;

public class VerifyFaceServlet extends HttpServlet {

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/social_practice_activity_server/", "http://159.75.97.188:8080/social_practice_activity_server/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserPhoto/";
    static private final String face_parent_path = "./UserPhoto/";

    UserDetailService userDetailService = new UserDetailServiceImpl();
    ActivityRegisterService activityRegisterService = new ActivityRegisterServiceImpl();

    AIFaceUtils afsUtils = new AIFaceUtils();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = null;
        int ActivityId = 0;
        String face_path = null;

        if (ServletFileUpload.isMultipartContent(req)){
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                List<FileItem> list = servletFileUpload.parseRequest(req);
                for (FileItem fileItem: list){
                    if (fileItem.isFormField()){
                        System.out.println("name="+fileItem.getFieldName());
                        System.out.println("value="+fileItem.getString("UTF-8"));
                        if (fileItem.getFieldName().equals("id")){
                            id = fileItem.getString("UTF-8");
                        }else if (fileItem.getFieldName().equals("activityId")) {
                            ActivityId = Integer.parseInt(fileItem.getString("UTF-8"));
                        }
                    }else{
                        System.out.println("name="+fileItem.getFieldName());
                        System.out.println("filename="+fileItem.getName());
//                        face_path = face_parent_path + fileItem.getName();
                        face_path = "C:\\myImage\\acitivityFace\\" + fileItem.getName();
                        fileItem.write(new File(face_path));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        UserDetail userDetail = userDetailService.find(id);
        System.out.println(userDetail);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();

        if (userDetail.getFace().equals("未认证")){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }
        // 人脸识别验证
        Result r = afsUtils.verifyFace(CreatePerson.builder().personId(id)
                .image(Base64ConvertUtils.getImageStr(face_path)).build());
        System.out.println(r);
        if (r.getMsg().contains("识别不成功")){
            o.put("ac", "false");
            writer.write(o.toString());
            return;
        }

        ActivityRegister activityRegister = activityRegisterService.queryByActivityIdAndUserId(ActivityId, id);
        if (activityRegister.getRegister() == 2){
            activityRegister.setSign_in_face(true);
            activityRegister.setRegister(activityRegister.getRegister()+1);
        }else if (activityRegister.getRegister() == 3){
            activityRegister.setSign_out_face(true);
            activityRegister.setRegister(activityRegister.getRegister()+1);
        }
        activityRegisterService.update(activityRegister);
        o.put("ac", "true");
        writer.write(o.toString());
    }
}