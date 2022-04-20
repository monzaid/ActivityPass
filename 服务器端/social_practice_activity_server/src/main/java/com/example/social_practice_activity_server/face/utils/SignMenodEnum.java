package com.example.social_practice_activity_server.face.utils;


/**
 * @author:
 * @description: TODO 签名方法
 * @date:
 * @version:
 */
public enum SignMenodEnum {

    HMACSHA1("HmacSHA1"),
    HMACSHA256("HmacSHA256"),
    TC3_HMAC_SHA256("TC3-HMAC-SHA256");

    private String mendoName;

    SignMenodEnum(String mendoName) {
        this.mendoName = mendoName;
    }

    public String getMendoName() {
        return mendoName;
    }

    public void setMendoName(String mendoName) {
        this.mendoName = mendoName;
    }

}