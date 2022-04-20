package com.example.social_practice_activity.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class myActivity {
    static String pattern = "yy-MM-dd HH:mm";
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public int id;
    public String t_u_id;
    public String userName;
    public String title;
    public String introduction;
    public String content;
    public String image;
    public String team;
    public String telephone;
    public String place;
    public int people;
    public Timestamp start_time;
    public Timestamp end_time;
    public double pos_x;
    public double pos_y;
    public int astrict;
    public int sex;
    public int maxAge, minAge;
    public String district;
    public String elseAstrict;

    public myActivity() {
    }

    public myActivity(int id, String t_u_id, String userName, String title, String introduction, String content, String image, String team, String telephone, String place, int people, Timestamp start_time, Timestamp end_time, double pos_x, double pos_y, int astrict, int sex, int maxAge, int minAge, String district, String elseAstrict) {
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

    public myActivity(int id, String title, String introduction, String image, String place, int people, Timestamp start_time) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.image = image;
        this.place = place;
        this.people = people;
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "myActivity{" +
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

    public String getStart_time() {
        return simpleDateFormat.format(start_time);
    }

    public String getEnd_time() {
        return simpleDateFormat.format(end_time);
    }
}
