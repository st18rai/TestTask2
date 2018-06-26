package com.st18apps.testtask;

import android.app.Application;


import com.st18apps.testtask.model.User;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static App instance;
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        userList = new ArrayList<>();
    }

    public static App getInstance() {
        return instance;
    }
}
