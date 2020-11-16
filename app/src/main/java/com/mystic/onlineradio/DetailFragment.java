package com.mystic.onlineradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Radio model";
    private PlayerView audio;
    private RadioService radioService;
    private ProgressBar bar;
    private boolean bound = false;
    private SimpleExoPlayer player;


    // TODO: Rename and change types of parameters
    private RadioBluePrint radioBluePrint;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RadioService.RadioServiceBinder radioServiceBinder = (RadioService.RadioServiceBinder) iBinder;
            radioService = radioServiceBinder.getRadioService();
            bound = true;
            player = radioService.getPlayer();
            connectionPoint();
            progressBarDet();
            Toast.makeText(getActivity(),"Am seeing tins"+bound,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };
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
                getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
                intent.putExtra(RadioFragment.RADIO,radioBluePrint);
                getActivity().startService(intent);
                Log.d("Detail","Log is workin");
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        audio = view.findViewById(R.id.audio_view);
//The error is happening on this line we are trying communicate between the service and the fragment but at the time we called on the oncreateview method
        //the player was sttill null so we could not get the player we needed
        audio.setPlayer(player);
        bar = view.findViewById(R.id.progress_bar);
        return view;
    }


    private void connectionPoint(){
        if(bound && radioService != null ){
            SimpleExoPlayer player = radioService.getPlayer();
            audio.setPlayer(new SimpleExoPlayer.Builder(getActivity()).build());
        }
    }


    //This method determines if the progress bar shows or not
    private void progressBarDet() {
        if (bound) {
            int off = radioService.getBardetOff();
            int show = radioService.getBardet();
            if (off == 2) {
                bar.setVisibility(View.VISIBLE);
            }

            if (show == 3) {
                bar.setVisibility(View.GONE);
            }
        }
    }

}