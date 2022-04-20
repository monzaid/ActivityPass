package com.example.social_practice_activity_server.face.utils;

/**
 * @author:
 * @description: TODO 请求类型枚举
 * @date:
 * @version:
 */
public enum HttpMethodEnum {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");
    ;

    private String name;

    HttpMethodEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


