package com.example.social_practice_activity_server.web;

import com.example.social_practice_activity_server.face.dap.Result;
import com.example.social_practice_activity_server.face.pojo.AIFaceUtils;
import com.example.social_practice_activity_server.face.pojo.CreateFace;
import com.example.social_practice_activity_server.face.pojo.CreatePerson;
import com.example.social_practice_activity_server.face.pojo.DeletePersonFromGroup;
import com.example.social_practice_activity_server.face.utils.Base64ConvertUtils;
import com.example.social_practice_activity_server.pojo.UserDetail;
import com.example.social_practice_activity_server.service.UserDetailService;
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

public class UpFaceServlet extends HttpServlet {

    static private final String[] Host = new String[]{"http://192.168.137.1:8080/", "http://159.75.97.188:8080/"};
    static private final int LOCAL = 0;
    static private final int INTERNET = 1;
    static private final String Url = Host[INTERNET] + "UserPhoto/";
    static private final String face_parent_path = "./UserPhoto/";

    UserDetailService userDetailService = new UserDetailServiceImpl();

    AIFaceUtils afsUtils = new AIFaceUtils();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = null;
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
        if (userDetail.getFace().equals("未认证")){
            // 创建人员
            CreatePerson createPerson = CreatePerson.builder().groupId("martinWJ")
                    .personName(id).personId(id)
                    .image(Base64ConvertUtils.getImageStr(face_path)).build();
            Result person = afsUtils.createPerson(createPerson);
            System.out.println(person);
        }else {
            // 删除人员
            DeletePersonFromGroup deletePersonFromGroup = DeletePersonFromGroup.builder().GroupId("martinWJ").personId(id).build();
            Result person = afsUtils.DeletePersonFromGroup(deletePersonFromGroup);
            System.out.println(person);
            // 创建人员
            CreatePerson createPerson = CreatePerson.builder().groupId("martinWJ")
                    .personName(id).personId(id)
                    .image(Base64ConvertUtils.getImageStr(face_path)).build();
            person = afsUtils.createPerson(createPerson);
            System.out.println(person);
        }
        userDetailService.updateByFace(id, id);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        JSONObject o = new JSONObject();
        o.put("ac", "true");
        writer.write(o.toString());
    }
}