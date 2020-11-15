package com.mystic.onlineradio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;



public class RadioService extends Service {

    private static final String CHANNEL_ID = "ChannelID";
    private static final int NOTIFICATION_ID = 50;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private final Binder binder = new RadioServiceBinder();
    private RadioBluePrint radio;
    private int bar_det_show = 0;
    private int bar_det_off = 0;

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        radio = (RadioBluePrint) intent.getSerializableExtra(RadioFragment.RADIO);
        player = new SimpleExoPlayer.Builder(this).build();
        if( radio != null){
            MediaItem mediaItem = MediaItem.fromUri(radio.getUrl());
            player.setMediaItem(mediaItem);
            initializePlayer();
            showNotif();
        }

        return START_STICKY;
    }

    public int getBardet() {
        return bar_det_show;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            releasePlayer();
    }

   private void initializePlayer() {
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if(state == Player.STATE_BUFFERING){
                   bar_det_show = state;
                }

                if(state == Player.STATE_READY){
                    bar_det_off = state;
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.stop();
            player.release();
            player = null;
        }
    }

    public int getBardetOff() {
        return bar_det_off;
    }

    public class RadioServiceBinder extends Binder {
        public RadioService getRadioService(){
            return RadioService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotif(){
        createNotificationChannel();;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.exo_icon_circular_play)
                .setContentTitle("Radio is playing")
                .setContentText(radio.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[] {0, 1000})
                .setAutoCancel(true);


        Intent actionIntent = new Intent(this, RadioActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(actionPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}
