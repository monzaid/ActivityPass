package com.example.social_practice_activity_server.pojo;

import java.sql.Timestamp;

public class UserDetail {
    private String id;
    private String name;
    private int sex;
    private int age;
    private String province;
    private String city;
    private String district;
    private String telephone;
    private String face;
    private Timestamp face_last_upload_time;

    public Timestamp getFace_last_upload_time() {
        return face_last_upload_time;
    }

    public void setFace_last_upload_time(Timestamp face_last_upload_time) {
        this.face_last_upload_time = face_last_upload_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public UserDetail() {
    }

    public UserDetail(String id) {
        this.id = id;
        this.sex = 2;
        this.age = 0;
        this.name = new String("未命名");
        this.province = "-请选择-";
        this.city = "-请选择-";
        this.district = "-请选择-";
        this.telephone = "";
        this.face = "未认证";
        this.face_last_upload_time = new Timestamp(System.currentTimeMillis());
    }

    public UserDetail(String id, String name, int sex, int age, String province, String city, String district, String telephone, String face, Timestamp face_last_upload_time) {
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
        return "UserDetail{" +
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
