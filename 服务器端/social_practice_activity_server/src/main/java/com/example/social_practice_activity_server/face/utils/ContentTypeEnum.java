package com.example.social_practice_activity_server.face.utils;

/**
 * @author:
 * @description: TODO 请求内容类型
 * @date:
 * @version:
 */
public enum ContentTypeEnum {

    JSON("application/json; charset=utf-8"),
    MULTIPART("multipart/form-data");

    private String name;

    ContentTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}