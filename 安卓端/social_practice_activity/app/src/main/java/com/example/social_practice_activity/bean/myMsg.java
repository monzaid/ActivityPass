package com.example.social_practice_activity.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class myMsg {
    static String pattern = "yyyy-MM-dd HH:mm:ss";
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public int t_a_id;
    public String t_u_id;
    public int type;
    public Timestamp time;
    public String title, content;

    public myMsg(int t_a_id, String t_u_id, int type, Timestamp time, String title, String content) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.type = type;
        this.time = time;
        this.title = title;
        this.content = content;
    }

    public myMsg(int t_a_id, String t_u_id, int type) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.type = type;
    }

    public myMsg() {
    }

    @Override
    public String toString() {
        return "myMsg{" +
                "t_a_id=" + t_a_id +
                ", t_u_id='" + t_u_id + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getTime() {
        return simpleDateFormat.format(time);
    }
}
