package com.wcxdhr.simpleplayer.FloatWindow;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;

public class MyWindowManager {

    private static FloatWindowView mWindowView;

    private static WindowManager.LayoutParams layoutParams;

    private static WindowManager mWindowManager;


    private static int ScreenWidth;

    private static int ScreenHeight;

    public static void createWindow(Context context) {
        setmWindowManager(context);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        ScreenHeight = metrics.heightPixels;
        ScreenWidth = metrics.widthPixels;
        if (mWindowView == null) {
            mWindowView = new FloatWindowView(context);
            if (layoutParams == null) {
                layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                layoutParams.format = PixelFormat.RGBA_8888;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                layoutParams.width = FloatWindowView.viewWidth;
                layoutParams.height = FloatWindowView.viewHeight;
                layoutParams.x = ScreenWidth;
                layoutParams.y = ScreenHeight/2;
            }
            mWindowView.setLayoutParams(layoutParams);
            mWindowManager.addView(mWindowView, layoutParams);
        }
    }

    public static void removeWindow(Context context) {
        if (mWindowView != null) {
            setmWindowManager(context);
            mWindowManager.removeView(mWindowView);
            mWindowView = null;
        }

    }

    public static boolean isWindowShowing() {
        return mWindowView != null;
    }

    private static void setmWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
    }

}
