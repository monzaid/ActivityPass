package com.example.social_practice_activity_server.pojo;

public class ActivityRegister {
    private int t_a_id;
    private String t_u_id;
    private int register;
    private boolean sign_in_pos;
    private boolean sign_in_face;
    private boolean sign_out_pos;
    private boolean sign_out_face;
    private boolean sign_in_2;
    private boolean sing_out_2;
    private boolean running;

    public ActivityRegister(int t_a_id, String t_u_id, int register, boolean sign_in_pos, boolean sign_in_face, boolean sign_out_pos, boolean sign_out_face, boolean sign_in_2, boolean sing_out_2, boolean running) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.register = register;
        this.sign_in_pos = sign_in_pos;
        this.sign_in_face = sign_in_face;
        this.sign_out_pos = sign_out_pos;
        this.sign_out_face = sign_out_face;
        this.sign_in_2 = sign_in_2;
        this.sing_out_2 = sing_out_2;
        this.running = running;
    }

    public ActivityRegister() {
    }

    public ActivityRegister(int t_a_id, String t_u_id) {
        this.t_a_id = t_a_id;
        this.t_u_id = t_u_id;
        this.register = 1;
        this.sign_in_pos = false;
        this.sign_in_face = false;
        this.sign_out_pos = false;
        this.sign_out_face = false;
        this.sign_in_2 = false;
        this.sing_out_2 = false;
    }

    @Override
    public String toString() {
        return "ActivityRegister{" +
                "t_a_id=" + t_a_id +
                ", t_u_id='" + t_u_id + '\'' +
                ", register=" + register +
                ", sign_in_pos=" + sign_in_pos +
                ", sign_in_face=" + sign_in_face +
                ", sign_out_pos=" + sign_out_pos +
                ", sign_out_face=" + sign_out_face +
                ", sign_in_2=" + sign_in_2 +
                ", sing_out_2=" + sing_out_2 +
                ", running=" + running +
                '}';
    }

    public int getT_a_id() {
        return t_a_id;
    }

    public void setT_a_id(int t_a_id) {
        this.t_a_id = t_a_id;
    }

    public String getT_u_id() {
        return t_u_id;
    }

    public void setT_u_id(String t_u_id) {
        this.t_u_id = t_u_id;
    }

    public boolean isSign_in_2() {
        return sign_in_2;
    }

    public void setSign_in_2(boolean sign_in_2) {
        this.sign_in_2 = sign_in_2;
    }

    public boolean isSing_out_2() {
        return sing_out_2;
    }

    public void setSing_out_2(boolean sing_out_2) {
        this.sing_out_2 = sing_out_2;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public boolean isSign_in_pos() {
        return sign_in_pos;
    }

    public void setSign_in_pos(boolean sign_in_pos) {
        this.sign_in_pos = sign_in_pos;
    }

    public boolean isSign_in_face() {
        return sign_in_face;
    }

    public void setSign_in_face(boolean sign_in_face) {
        this.sign_in_face = sign_in_face;
    }

    public boolean isSign_out_pos() {
        return sign_out_pos;
    }

    public void setSign_out_pos(boolean sign_out_pos) {
        this.sign_out_pos = sign_out_pos;
    }

    public boolean isSign_out_face() {
        return sign_out_face;
    }

    public void setSign_out_face(boolean sign_out_face) {
        this.sign_out_face = sign_out_face;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
