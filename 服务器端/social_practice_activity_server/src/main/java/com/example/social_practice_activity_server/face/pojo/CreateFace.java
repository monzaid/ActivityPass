package com.example.social_practice_activity_server.face.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: CreateFace
 * @Description: TODO 创建人脸请求参数 https://cloud.tencent.com/document/product/867/32794
 * @author:
 * @createDate:
 */
@Data
@Builder
public class CreateFace {
    /** 父类Id */
    private String personId;

    private String img01;

    private String img02;

    private String img03;

    private String img04;

    private String img05;
}
