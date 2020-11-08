package com.mystic.onlineradio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.util.Util;

public class RadioService extends Service {

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra(RadioFragment.RADIO_URL);
        player = new SimpleExoPlayer.Builder(this).build();
        if(url != null){
            MediaItem mediaItem = MediaItem.fromUri(url);
            player.setMediaItem(mediaItem);
            Log.d("Services",url);
            initializePlayer();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }

    }

    private void initializePlayer() {
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
