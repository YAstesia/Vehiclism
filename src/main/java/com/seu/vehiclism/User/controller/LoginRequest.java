package com.seu.vehiclism.User.controller;

public class LoginRequest {
    private String key; // 可以是邮箱或手机号
    private String pwd;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
