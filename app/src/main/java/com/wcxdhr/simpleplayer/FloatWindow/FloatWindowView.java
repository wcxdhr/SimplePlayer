package com.wcxdhr.simpleplayer.FloatWindow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.wcxdhr.simpleplayer.Activity.VideoPlayerActivity;
import com.wcxdhr.simpleplayer.ExoPlayer.ExoPlayerVideoHandler;
import com.wcxdhr.simpleplayer.R;
import com.wcxdhr.simpleplayer.db.Video;

import java.lang.reflect.Field;

public class FloatWindowView extends RelativeLayout {

    public static int viewWidth;

    public static int viewHeight;

    private static int statusBarHeight;

    private WindowManager windowManager;

    private WindowManager.LayoutParams layoutParams;

    private float xInView;

    private float yInView;

    private float xInScreen;

    private float yInScreen;

    private float xInDownScreen;

    private float yInDownScreen;

    private PlayerView playerView;

    public FloatWindowView(Context context, Video video) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_layout, this);
        View view = findViewById(R.id.float_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        ImageButton floatWindowCancelBtn = (ImageButton) findViewById(R.id.cancel_window_btn);
        //ImageButton openPlayerBtn = (ImageButton) findViewById(R.id.open_player_btn);
        playerView = (PlayerView) findViewById(R.id.player_view);
        playerView.setPlayer(ExoPlayerVideoHandler.getInstance().getPlayer());
        playerView.hideController();
        playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                playerView.hideController();
            }
        });
        /*playerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        playerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xInView = event.getX();
                        yInView = event.getY();
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY() - getStatusBarHeight();
                        xInDownScreen = xInScreen;
                        yInDownScreen = yInScreen;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY() - getStatusBarHeight();
                        updateViewPosition();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (yInDownScreen == yInScreen && xInDownScreen == xInScreen) {
                            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                            intent.putExtra("video_data", video);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            getContext().startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri(context, Uri.parse(video.getPath()), playerView);
        ExoPlayerVideoHandler.getInstance().goToForeground();
        floatWindowCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
                MyWindowManager.removeWindow(getContext());

            }
        });
        /*openPlayerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                xInDownScreen = xInScreen;
                yInDownScreen = yInScreen;
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    private void updateViewPosition() {
        layoutParams.x = (int)(xInScreen - xInView);
        layoutParams.y = (int)(yInScreen - yInView);
        windowManager.updateViewLayout(this, layoutParams);
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                statusBarHeight = getResources().getDimensionPixelSize((Integer)field.get(o));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }


}
