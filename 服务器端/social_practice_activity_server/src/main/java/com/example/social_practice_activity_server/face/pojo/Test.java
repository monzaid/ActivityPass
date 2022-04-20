package com.example.social_practice_activity_server.face.pojo;

import com.example.social_practice_activity_server.face.dap.Result;
import com.example.social_practice_activity_server.face.utils.Base64ConvertUtils;

/**
 * @ClassName: Test
 * @Description: TODO
 * @author:
 * @createDate:
 */
public class Test {
    public static void main(String[] args) {
        AIFaceUtils afsUtils = new AIFaceUtils();
        // 创建人员库
//        Result group = afsUtils.createGroup(CreateGroup.builder().groupName("Social_activity").groupId("Social_activity")
//                .groupExDescriptions01("123456789").build());
//        System.out.println(group);

//         创建人员
//        CreatePerson createPerson = CreatePerson.builder().groupId("Social_activity")
//                .personName("梦泪").personId("menglei").gender(1)
//                .image(Base64ConvertUtils.getImageStr("F:\\face_recognition\\home\\aistudio\\face_recognition\\images\\menglei.png")).build();
//        Result person = afsUtils.createPerson(createPerson);
//        System.out.println(person);

        // 人脸识别验证
//        Result r = afsUtils.verifyFace(CreatePerson.builder().personId("menglei")
//                .image(Base64ConvertUtils.getImageStr("C:\\Users\\ymxyl\\Pictures\\777.jpg")).build());
//        System.out.println(r);

        // 创建人脸
//        CreateFace confectFace = CreateFace.builder().personId("Social_activity")
//                .img01("C:\\Users\\martin\\Pictures\\test.jpg")
//                .img02("").img03("").img04("").img05("").build();
//        Result face = afsUtils.createFace(confectFace);
//        System.out.println(face);
    }
}