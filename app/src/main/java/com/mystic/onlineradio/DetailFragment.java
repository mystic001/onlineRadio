package com.mystic.onlineradio;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Radio model";
    public static final String PLAYING ="com.mystic.onlineradio.PLAYING";
    private PlayerView audio;
    private IntentFilter filter;
    private RadioService radioService;
    private ProgressBar bar;
    private boolean bound = false;
    private SimpleExoPlayer player;


    // TODO: Rename and change types of parameters
    private RadioBluePrint radioBluePrint;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getActivity() != null ){
            if(!radioBluePrint.isRunning()){
                radioBluePrint.setRunning(true);
                Intent intent = new Intent(getActivity(), RadioService.class);
                intent.putExtra(RadioFragment.RADIO,radioBluePrint);
                getActivity().startService(intent);
            }

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(bound){
            bound = false;
        }
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

        filter = new IntentFilter();
        filter.addAction(PLAYING);
        assert getActivity() != null ;
        getActivity().registerReceiver(broadcastReceiver,filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Objects.equals(intent.getAction(), PLAYING)){
                String val = intent.getStringExtra("Test");
                Toast.makeText(getActivity(),val,Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        audio = view.findViewById(R.id.audio_view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        assert  getActivity() != null ;
        getActivity().unregisterReceiver(broadcastReceiver);
    }





}