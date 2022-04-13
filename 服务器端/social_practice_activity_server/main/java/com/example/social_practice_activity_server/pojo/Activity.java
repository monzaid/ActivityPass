package com.example.social_practice_activity_server.pojo;

import java.sql.Timestamp;
import java.util.Calendar;

public class Activity {
    private int id;
    private String t_u_id;
    private String userName;
    private String title;
    private String introduction;
    private String content;
    private String image;
    private String team;
    private String telephone;
    private String place;
    private int people;
    private Timestamp start_time;
    private Timestamp end_time;
    private double pos_x, pos_y;
    private int astrict;
    private int sex;
    private int maxAge, minAge;
    private String district;
    private String elseAstrict;

    public String getElseAstrict() {
        return elseAstrict;
    }

    public void setElseAstrict(String elseAstrict) {
        this.elseAstrict = elseAstrict;
    }

    public int getAstrict() {
        return astrict;
    }

    public void setAstrict(int astrict) {
        this.astrict = astrict;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getPos_x() {
        return pos_x;
    }

    public void setPos_x(double pos_x) {
        this.pos_x = pos_x;
    }

    public double getPos_y() {
        return pos_y;
    }

    public void setPos_y(double pos_y) {
        this.pos_y = pos_y;
    }

    public Activity(int id, String t_u_id, String userName, String title, String introduction, String content, String image, String team, String telephone, String place, int people, Timestamp start_time, Timestamp end_time, double pos_x, double pos_y, int astrict, int sex, int maxAge, int minAge, String district, String elseAstrict) {
        this.id = id;
        this.t_u_id = t_u_id;
        this.userName = userName;
        this.title = title;
        this.introduction = introduction;
        this.content = content;
        this.image = image;
        this.team = team;
        this.telephone = telephone;
        this.place = place;
        this.people = people;
        this.start_time = start_time;
        this.end_time = end_time;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.astrict = astrict;
        this.sex = sex;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.district = district;
        this.elseAstrict = elseAstrict;
    }

    public Activity() {
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", t_u_id='" + t_u_id + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", team='" + team + '\'' +
                ", telephone='" + telephone + '\'' +
                ", place='" + place + '\'' +
                ", people=" + people +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", pos_x=" + pos_x +
                ", pos_y=" + pos_y +
                ", astrict=" + astrict +
                ", sex=" + sex +
                ", maxAge=" + maxAge +
                ", minAge=" + minAge +
                ", district='" + district + '\'' +
                ", elseAstrict='" + elseAstrict + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getT_u_id() {
        return t_u_id;
    }

    public void setT_u_id(String userId) {
        this.t_u_id = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }
}
