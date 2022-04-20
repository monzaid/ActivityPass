package com.example.social_practice_activity_server.face.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.social_practice_activity_server.face.dap.Result;
import com.example.social_practice_activity_server.face.dap.SystemConstants;
import com.example.social_practice_activity_server.face.utils.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author:
 * @description: TODO 人脸识别工具类
 * @date:
 * @version: 1.0
 */
@Component
public class AIFaceUtils {

    private String secretId = FaceUtils.get("tencent.secretId");

    private String secretKey = FaceUtils.get("tencent.secretKey");

    private String SERVER_NAME_API = FaceUtils.get("tencent.SERVER_NAME_API");


    /**
     * 创建人员库
     *
     * @param createGroup
     * @return
     */
    public Result createGroup(CreateGroup createGroup) {
        //构建签名参数
        TreeMap treeMap = createPublicMap("CreateGroup", "2018-03-01");
        treeMap.put("GroupName", createGroup.getGroupName());
        treeMap.put("GroupId", createGroup.getGroupId());
        if (!StringUtils.isEmpty(createGroup.getGroupExDescriptions01())) {
            treeMap.put("GroupExDescriptions.0", createGroup.getGroupId());
        }
        if (!StringUtils.isEmpty(createGroup.getGroupExDescriptions02())) {
            treeMap.put("GroupExDescriptions.1", createGroup.getGroupId());
        }
        if (!StringUtils.isEmpty(createGroup.getGroupExDescriptions03())) {
            treeMap.put("GroupExDescriptions.2", createGroup.getGroupId());
        }
        if (!StringUtils.isEmpty(createGroup.getTag())) {
            treeMap.put("Tag", createGroup.getTag());
        }
        try {
            treeMap.put("Signature", SignUtils.sign(treeMap, HttpMethodEnum.GET, SignMenodEnum.HMACSHA1, JSON.toJSONString(createGroup),
                    SERVER_NAME_API, secretKey, ContentTypeEnum.JSON));
        } catch (Exception e) {
            System.out.println("签名异常:{}" + e.getMessage());
            Result.error("签名异常").setCode(SystemConstants.SERVER_ERROR_CODE);
        }
        String reqUrl = null;
        try {
            reqUrl = SignUtils.getUrl(treeMap, SERVER_NAME_API);
        } catch (UnsupportedEncodingException e) {
            System.out.println("URL编码异常:{}" + e.getMessage());
            return Result.error("URL编码异常").setCode(SystemConstants.SERVER_ERROR_CODE);
        }

        try {
            String s = HttpUtil.httpGet(reqUrl);
            JSONObject jsonObject = JSON.parseObject(s);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                return Result.ok(null, "创建成功");
            }

        } catch (Exception e) {
            System.out.println("创建失败:{}" + e.getMessage());
            return Result.error("创建失败").setCode(SystemConstants.SERVER_ERROR_CODE);
        }

    }


    /**
     * 创建人员
     *
     * @param createPerson
     * @return
     */
    public Result createPerson(CreatePerson createPerson) {
        //构建签名参数map
        TreeMap treeMap = createPublicMap("CreatePerson", "2018-03-01");
        //构建JSON格式参数
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PersonName", createPerson.getPersonName());
        hashMap.put("PersonId", createPerson.getPersonId());
        hashMap.put("GroupId", createPerson.getGroupId());
        hashMap.put("Image", createPerson.getImage());
        hashMap.put("Gender", createPerson.getGender() == null ? 0 : createPerson.getGender());
        if (!StringUtils.isEmpty(createPerson.getPersonExDescription01())) {
            hashMap.put("PersonExDescriptionInfos.0.PersonExDescriptionIndex", 0);
            hashMap.put("PersonExDescriptionInfos.0.PersonExDescription", createPerson.getPersonExDescription01());
        }
        if (!StringUtils.isEmpty(createPerson.getPersonExDescription02())) {
            hashMap.put("PersonExDescriptionInfos.1.PersonExDescriptionIndex", 1);
            hashMap.put("PersonExDescriptionInfos.1.PersonExDescription", createPerson.getPersonExDescription02());
        }
        if (!StringUtils.isEmpty(createPerson.getPersonExDescription03())) {
            hashMap.put("PersonExDescriptionInfos.2.PersonExDescriptionIndex", 2);
            hashMap.put("PersonExDescriptionInfos.2.PersonExDescription", createPerson.getPersonExDescription03());
        }
        String sign = null;
        try {
            sign = SignUtils.sign(treeMap, HttpMethodEnum.POST, SignMenodEnum.TC3_HMAC_SHA256, JSON.toJSONString(hashMap)
                    , SERVER_NAME_API, secretKey, ContentTypeEnum.JSON);
        } catch (Exception e) {
            System.out.println("签名异常:{}" + e.getMessage());
            return Result.error("签名异常");
        }
        try {
            String respJson = HttpUtil.httpPost(SERVER_NAME_API, JSON.parseObject(sign, Map.class), hashMap);
            JSONObject jsonObject = JSON.parseObject(respJson);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                return Result.ok(null, "创建成功");
            }

        } catch (Exception e) {
            System.out.println("创建失败:{}" + e.getMessage());
            return Result.error("创建失败");
        }
    }

    public Result DeletePersonFromGroup(DeletePersonFromGroup deletePersonFromGroup) {
        //构建签名参数map
        TreeMap treeMap = createPublicMap("DeletePersonFromGroup", "2018-03-01");
        //构建JSON格式参数
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PersonId", deletePersonFromGroup.getPersonId());
        hashMap.put("GroupId", deletePersonFromGroup.getGroupId());
        String haerdsJson = null;
        try {
            haerdsJson = SignUtils.sign(treeMap, HttpMethodEnum.POST, SignMenodEnum.TC3_HMAC_SHA256, JSON.toJSONString(hashMap),
                    SERVER_NAME_API, secretKey, ContentTypeEnum.JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发起post请求
        try {
            String respJson = HttpUtil.httpPost(SERVER_NAME_API, JSON.parseObject(haerdsJson, TreeMap.class), hashMap);
            JSONObject jsonObject = JSON.parseObject(respJson);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                return Result.ok("新增成功");
            }

        } catch (Exception e) {
            System.out.println("新增失败:{}" + e.getMessage());
            return Result.error("新增失败:" + e.getMessage());
        }
    }


    /**
     * 查询人员列表
     *
     * @param getPersonBaseInfo
     * @return
     */
    public Result getPersonBaseInfo(GetPersonBaseInfo getPersonBaseInfo) {
        //封装公共map签名
        TreeMap treeMap = createPublicMap("GetPersonBaseInfo", "2018-03-01");
        //HashMap<String,Object> hashMap = new HashMap<>();
        //封装请求参数
        treeMap.put("PersonId", getPersonBaseInfo.getPersonId());
        String sign = null;
        try {
            sign = SignUtils.sign(treeMap, HttpMethodEnum.GET, SignMenodEnum.HMACSHA1, null, SERVER_NAME_API, secretKey, ContentTypeEnum.JSON);
        } catch (Exception e) {
            System.out.println("签名异常" + e.getMessage());
            return Result.error("签名异常");
        }
        treeMap.put("Signature", sign);

        try {
            String url = SignUtils.getUrl(treeMap, SERVER_NAME_API);
            String respJson = HttpUtil.httpGet(url);
            JSONObject jsonObject = JSON.parseObject(respJson);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                return Result.ok(JSON.parseObject(response, GetPersonBaseInfoResp.class));
            }

        } catch (Exception e) {
            System.out.println("查询失败:{}" + e.getMessage());
            return Result.error("查询失败:" + e.getMessage());
        }
    }

    /**
     * 创建人脸
     *
     * @param createFace
     * @return
     */
    public Result createFace(CreateFace createFace) {
        //封装公共请求参数
        TreeMap treeMap = createPublicMap("CreateFace", "2018-03-01");
        //封装请求参数
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PersonId", createFace.getPersonId());
        ArrayList<String> images = new ArrayList<>();
        if (!StringUtils.isEmpty(createFace.getImg01())) {
            images.add(createFace.getImg01());
        }
        if (!StringUtils.isEmpty(createFace.getImg02())) {
            images.add(createFace.getImg02());
        }
        if (!StringUtils.isEmpty(createFace.getImg03())) {
            images.add(createFace.getImg03());
        }
        if (!StringUtils.isEmpty(createFace.getImg04())) {
            images.add(createFace.getImg04());
        }
        if (!StringUtils.isEmpty(createFace.getImg05())) {
            images.add(createFace.getImg05());
        }
        hashMap.put("Images", images);
        //获取请求头JSON数据
        String haerdsJson = null;
        try {
            haerdsJson = SignUtils.sign(treeMap, HttpMethodEnum.POST, SignMenodEnum.TC3_HMAC_SHA256, JSON.toJSONString(hashMap),
                    SERVER_NAME_API, secretKey, ContentTypeEnum.JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发起post请求
        try {
            String respJson = HttpUtil.httpPost(SERVER_NAME_API, JSON.parseObject(haerdsJson, TreeMap.class), hashMap);
            JSONObject jsonObject = JSON.parseObject(respJson);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                return Result.ok("新增成功");
            }

        } catch (Exception e) {
            System.out.println("新增失败:{}" + e.getMessage());
            return Result.error("新增失败:" + e.getMessage());
        }
    }

    public Result verifyFace(CreatePerson createPerson) {
        //封装公共请求参数
        TreeMap treeMap = createPublicMap("VerifyFace", "2018-03-01");
        //封装请求参数
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PersonId", createPerson.getPersonId());
        hashMap.put("Image", createPerson.getImage());
        //获取请求头JSON数据
        String haerdsJson = null;
        try {
            haerdsJson = SignUtils.sign(treeMap, HttpMethodEnum.POST, SignMenodEnum.TC3_HMAC_SHA256, JSON.toJSONString(hashMap),
                    SERVER_NAME_API, secretKey, ContentTypeEnum.JSON);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("签名异常");
        }
        //发起post请求
        try {
            String respJson = HttpUtil.httpPost(SERVER_NAME_API, JSON.parseObject(haerdsJson, TreeMap.class), hashMap);
            JSONObject jsonObject = JSON.parseObject(respJson);
            String response = jsonObject.getString("Response");
            JSONObject error = (JSONObject) JSON.parseObject(response).get("Error");
            if (Objects.nonNull(error)) {
                return Result.error(String.valueOf(error.get("Message"))).setCode(SystemConstants.SERVER_ERROR_CODE);
            } else {
                VerifyFaceResp verifyFaceResp = JSON.parseObject(response, VerifyFaceResp.class);
                if (verifyFaceResp.getIsMatch()) {
                    return Result.ok(null, "识别成功,相识度" + verifyFaceResp.getScore() + "%");
                } else {
                    return Result.ok(null, "识别不成功,相识度" + verifyFaceResp.getScore() + "%");
                }
            }
        } catch (Exception e) {
            System.out.println("识别失败:{}" + e.getMessage());
            return Result.error("识别失败:" + e.getMessage());
        }
    }

    /**
     * 获取当前时间戳，单位秒
     *
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 封装请求参数
     *
     * @param action
     * @param version
     * @return
     */
    public TreeMap createPublicMap(String action, String version) {

        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("Action", action);
        treeMap.put("Version", version);
        treeMap.put("Timestamp", getCurrentTimestamp());
        treeMap.put("Nonce", new Random().nextInt(Integer.MAX_VALUE));
        treeMap.put("SecretId", secretId);
        return treeMap;
    }
}