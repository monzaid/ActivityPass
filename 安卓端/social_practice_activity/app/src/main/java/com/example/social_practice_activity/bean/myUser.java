package com.example.social_practice_activity.bean;

public class myUser {
    public String id;
    public String password;

    public myUser(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public myUser() {
    }

    @Override
    public String toString() {
        return "myUser{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
