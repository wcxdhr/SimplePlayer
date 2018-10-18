package com.wcxdhr.simpleplayer.Log;

import android.util.Log;

public class LogUtil {

    public static final int DEBUG = 2;

    public static int level =DEBUG;

    public static void d(String msg){
        if (level<=DEBUG) {
            Log.d("wcxdhr_test", msg);
        }
    }
}
