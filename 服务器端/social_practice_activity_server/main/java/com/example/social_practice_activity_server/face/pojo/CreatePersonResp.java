package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: CreatePersonResp
 * @Description: TODO 创建人员响应
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
public class CreatePersonResp extends Response {

    private String faceId;
}