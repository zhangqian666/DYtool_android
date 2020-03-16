package com.zq.dytool.adbmodel;

import android.app.Application;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/3 6:46 PM
 * @Package com.zq.dytool
 **/
public class MApplication extends Application {

    public static int base_x = 0;
    public static int base_y = 0;
    public static boolean isRoot = false;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
