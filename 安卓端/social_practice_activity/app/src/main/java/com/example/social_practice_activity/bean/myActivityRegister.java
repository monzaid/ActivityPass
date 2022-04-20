package com.example.social_practice_activity.bean;

public class myActivityRegister {
    public int Id;
    public String userId;
    public int register;
    public boolean sign_in_pos;
    public boolean sign_in_face;
    public boolean sign_out_pos;
    public boolean sign_out_face;
    public boolean sign_in_2;
    public boolean sing_out_2;
    public boolean running;
    public String username, telephone, stute;

    public myActivityRegister(int id, String userId, int register, boolean sign_in_pos, boolean sign_in_face, boolean sign_out_pos, boolean sign_out_face, boolean sign_in_2, boolean sing_out_2, boolean running) {
        Id = id;
        this.userId = userId;
        this.register = register;
        this.sign_in_pos = sign_in_pos;
        this.sign_in_face = sign_in_face;
        this.sign_out_pos = sign_out_pos;
        this.sign_out_face = sign_out_face;
        this.sign_in_2 = sign_in_2;
        this.sing_out_2 = sing_out_2;
        this.running = running;
    }

    public myActivityRegister() {
    }

    public myActivityRegister(int id, String userId) {
        Id = id;
        this.userId = userId;
        this.register = 0;
        this.sign_in_pos = false;
        this.sign_in_face = false;
        this.sign_out_pos = false;
        this.sign_out_face = false;
        this.sign_in_2 = false;
        this.sing_out_2 = false;
    }

    @Override
    public String toString() {
        return "myActivityRegister{" +
                "Id=" + Id +
                ", userId='" + userId + '\'' +
                ", register=" + register +
                ", sign_in_pos=" + sign_in_pos +
                ", sign_in_face=" + sign_in_face +
                ", sign_out_pos=" + sign_out_pos +
                ", sign_out_face=" + sign_out_face +
                ", sign_in_2=" + sign_in_2 +
                ", sing_out_2=" + sing_out_2 +
                ", running=" + running +
                ", username='" + username + '\'' +
                ", telephone='" + telephone + '\'' +
                ", stute='" + stute + '\'' +
                '}';
    }
}
