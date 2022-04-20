package com.example.social_practice_activity.bean;

import java.sql.Timestamp;

public class myUserDetail {
    public String id;
    public String name;
    public int sex;
    public int age;
    public String province;
    public String city;
    public String district;
    public String telephone;
    public String face;
    public Timestamp face_last_upload_time;

    public myUserDetail() {
    }

    public myUserDetail(String id, String name, int sex, int age, String province, String city, String district, String telephone, String face, Timestamp face_last_upload_time) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.province = province;
        this.city = city;
        this.district = district;
        this.telephone = telephone;
        this.face = face;
        this.face_last_upload_time = face_last_upload_time;
    }

    @Override
    public String toString() {
        return "myUserDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", telephone='" + telephone + '\'' +
                ", face='" + face + '\'' +
                ", face_last_upload_time=" + face_last_upload_time +
                '}';
    }
}