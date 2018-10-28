package com.wcxdhr.simpleplayer.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.wcxdhr.simpleplayer.Activity.VideoPlayerActivity;
import com.wcxdhr.simpleplayer.Log.LogUtil;
import com.wcxdhr.simpleplayer.db.Video;
import com.wcxdhr.simpleplayer.db.VideoDao;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int id = 1;
        //Toast.makeText(context,"onReceive - " + intent.getAction(), Toast.LENGTH_SHORT).show();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String video_id = bundle.getString(JPushInterface.EXTRA_EXTRA);
                        try {
                JSONArray jsonArray = new JSONArray(video_id);
                id = Integer.parseInt(jsonArray.getJSONObject(0).getString("id"));
            }catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context,"获取的附加字段： "+video_id, Toast.LENGTH_SHORT).show();
            VideoDao videoDao = new VideoDao(context);
            Cursor cursor = videoDao.findVideoById(id);
            if (cursor.moveToFirst()) {
                Video video = new Video();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                video.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                video.setPath(cursor.getString(cursor.getColumnIndexOrThrow("path")));
                video.setCategory(cursor.getInt(cursor.getColumnIndexOrThrow("category")));
                video.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                video.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("count")));
                Intent newIntent = new Intent(context,VideoPlayerActivity.class);
                newIntent.putExtra("video_data", video);
                //LogUtil.d("传递的video id： "+video.getId());
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(newIntent);
            }
        }
    }
}
