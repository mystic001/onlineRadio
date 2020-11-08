package com.mystic.onlineradio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.OptionalDataException;
import java.util.List;


public class RadioService extends Service {

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private List<RadioBluePrint> listofRadio;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listofRadio = RadioCompany.get().getRadioHub();
        RadioBluePrint radio = (RadioBluePrint) intent.getSerializableExtra(RadioFragment.RADIO);
        player = new SimpleExoPlayer.Builder(this).build();
        checkRunningState();
        if( radio != null){
            MediaItem mediaItem = MediaItem.fromUri(radio.getUrl());
            player.setMediaItem(mediaItem);
            if(!radio.isRunning()){
                initializePlayer(radio);
            }else{
                radio.setRunning(false);
                releasePlayer();
            }
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
            releasePlayer();
    }

   /* private void initializePlayer() {
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
    }*/

    private void initializePlayer(RadioBluePrint radioBluePrint) {
        radioBluePrint.setRunning(true);
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

    private void releasePlayer(RadioBluePrint radioBluePrint) {
        if (player != null) {
            radioBluePrint.setRunning(false);
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            //player = null;
        }
    }

    public void checkRunningState(){
        for(int i = 0 ; i < listofRadio.size() ; i++){
            RadioBluePrint radio = listofRadio.get(i);
            if(radio.isRunning()){
                releasePlayer(radio);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
