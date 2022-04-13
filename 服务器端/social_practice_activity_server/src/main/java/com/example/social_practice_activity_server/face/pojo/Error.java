package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: Error
 * @Description: TODO 响应错误信息
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
public class Error {
    /** 状态码 */
    private String code;

    /** 信息 */
    private String message;
}