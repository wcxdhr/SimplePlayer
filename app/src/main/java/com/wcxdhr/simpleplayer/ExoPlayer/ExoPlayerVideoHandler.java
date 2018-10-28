package com.wcxdhr.simpleplayer.ExoPlayer;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerVideoHandler {

    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance() {
        if (instance == null) {
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer player;

    private Uri player_uri;

    private boolean isPlayerPlaying;

    private boolean isNewPlay;

    private ExoPlayerVideoHandler(){}

    public boolean prepareExoPlayerForUri(Context context, Uri uri, PlayerView playerView) {
        isNewPlay = false;
        if (context != null && uri != null && playerView != null) {
            if (!uri.equals(player_uri) || player == null) {
                player_uri = uri;
                player = ExoPlayerFactory.newSimpleInstance(context);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,Util.getUserAgent(context,"SimplePlayer"));
                MediaSource videoSource =  new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                player.prepare(videoSource);
                isPlayerPlaying = true;
                isNewPlay = true;
            }
            player.clearVideoSurface();
            player.setVideoSurfaceView((SurfaceView) playerView.getVideoSurfaceView());
            //player.seekTo(player.getCurrentPosition()+1);
        }
        return isNewPlay;
    }

    public Player getPlayer() {
        return player;
    }

    public void releaseVideoPlayer() {
        if (player != null) {
            player.release();
        }
        player = null;
    }

    public void goToBackground() {
        if (player != null) {
            isPlayerPlaying = player.getPlayWhenReady();
            //player.setPlayWhenReady(false);
        }
    }

    public void goToForeground() {
        if (player != null) {
            player.setPlayWhenReady(isPlayerPlaying);
        }
    }

}
