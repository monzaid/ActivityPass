package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: GetPersonBaseInfoResp
 * @Description: TODO
 * @author:
 * @createDate:
 */
@Data
public class GetPersonBaseInfoResp {

    private String personName;

    private String gender;

    private String[] faceIds;
}