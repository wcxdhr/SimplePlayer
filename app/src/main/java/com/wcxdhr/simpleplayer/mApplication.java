package com.wcxdhr.simpleplayer;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class mApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
