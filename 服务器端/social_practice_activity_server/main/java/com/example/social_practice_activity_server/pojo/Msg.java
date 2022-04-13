package com.example.social_practice_activity_server.pojo;

import java.sql.Timestamp;

public class Msg {
    private int t_a_id;
    private String t_u_id;
    private int type;
    private Timestamp time;

    public Msg(int t_a_id, String t_u_id, int type, Timestamp time) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.type = type;
        this.time = time;
    }

    public Msg(int t_a_id, String t_u_id, int type) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.type = type;
    }

    public Msg() {
    }

    public String getT_u_id() {
        return t_u_id;
    }

    public void setT_u_id(String t_u_id) {
        this.t_u_id = t_u_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getT_a_id() {
        return t_a_id;
    }

    public void setT_a_id(int t_a_id) {
        this.t_a_id = t_a_id;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "t_a_id=" + t_a_id +
                ", t_u_id='" + t_u_id + '\'' +
                ", type=" + type +
                ", time=" + time +
                '}';
    }
}
