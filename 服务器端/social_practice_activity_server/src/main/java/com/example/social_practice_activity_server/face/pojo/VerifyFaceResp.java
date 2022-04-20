package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: VerifyFaceResp
 * @Description: TODO
 * @author:
 * @createDate:
 */
@Data
public class VerifyFaceResp extends Response {

    private String score;

    private Boolean isMatch;

}