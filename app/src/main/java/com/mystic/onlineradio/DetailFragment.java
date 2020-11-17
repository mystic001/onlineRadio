package com.mystic.onlineradio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "Radio model";
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private RadioBluePrint radioBluePrint;
    private ProgressBar bar;

    // TODO: Rename and change types of parameters



    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!radioBluePrint.isRunning()){
            initializePlayer(radioBluePrint);
            radioBluePrint.setRunning(true);
        }

    }


    @Override
    public void onStop() {
        super.onStop();
       // releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }



    public static DetailFragment newInstance(RadioBluePrint radioBluePrint) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, radioBluePrint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            radioBluePrint = (RadioBluePrint) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        bar = view.findViewById(R.id.progress_bar);
        playerView = view.findViewById(R.id.audio_view);
        return view;
    }

    private void initializePlayer(RadioBluePrint radioBluePrint) {
        assert getActivity() != null;
        player = new SimpleExoPlayer.Builder(getActivity()).build();
        MediaItem mediaItem = MediaItem.fromUri(radioBluePrint.getUrl());
        player.setMediaItem(mediaItem);
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if( state == Player.STATE_BUFFERING ){
                    bar.setVisibility(View.VISIBLE);
                }
                if(state == Player.STATE_READY){
                    bar.setVisibility(View.GONE);
                }
            };

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if(!isPlaying){
                    radioBluePrint.setRunning(false);
                } else{
                    radioBluePrint.setRunning(true);
                }

            }
        }

        );
    };



    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }

    }

}