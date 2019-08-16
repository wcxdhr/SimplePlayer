package com.wcxdhr.simpleplayer.FloatWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;
import com.wcxdhr.simpleplayer.data.Video;

public class MyWindowManager {

    private static FloatWindowView mWindowView;

    private static WindowManager.LayoutParams layoutParams;

    private static WindowManager mWindowManager;


    private static int ScreenWidth;

    private static int ScreenHeight;

    public static void createWindow(Context context, Video video) {
        setmWindowManager(context);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        float scale = metrics.density;
        ScreenHeight = (int)(metrics.heightPixels/scale + 0.5f);
        ScreenWidth = (int)(metrics.widthPixels/scale + 0.5f);
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        if (mWindowView == null) {
            mWindowView = new FloatWindowView(context, video);
            if (layoutParams == null) {
                layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                layoutParams.format = PixelFormat.RGBA_8888;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                layoutParams.width = FloatWindowView.viewWidth;
                layoutParams.height = FloatWindowView.viewHeight;
                layoutParams.x = ScreenWidth/2 - FloatWindowView.viewWidth/2;
                layoutParams.y = ScreenHeight/2 - FloatWindowView.viewHeight/2;
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
            ExoPlayerVideoHandler.getInstance().goToBackground();
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
