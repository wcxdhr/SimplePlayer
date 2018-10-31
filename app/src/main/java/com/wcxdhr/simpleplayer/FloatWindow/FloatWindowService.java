package com.wcxdhr.simpleplayer.FloatWindow;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.db.Video;

import java.util.ArrayList;
import java.util.List;

public class FloatWindowService extends Service {

    private Handler handler = new Handler();



    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("服务开始运行");
        if (!MyWindowManager.isWindowShowing()){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Video video = (Video)intent.getParcelableExtra("video_data");
                    MyWindowManager.createWindow(getApplicationContext(), video);
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d("服务Destroy");
        MyWindowManager.removeWindow(getApplicationContext());
        super.onDestroy();
    }

    public boolean isHome() {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri: resolveInfos ) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
