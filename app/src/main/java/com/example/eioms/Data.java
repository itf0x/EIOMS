package com.example.eioms;

import android.app.Application;

//全局变量保存
public class Data extends Application {
    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
