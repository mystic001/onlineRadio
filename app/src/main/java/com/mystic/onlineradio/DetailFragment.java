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
    private int show;
    private int off;
    private boolean bound = false;


    // TODO: Rename and change types of parameters
    private RadioBluePrint radioBluePrint;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RadioService.RadioServiceBinder radioServiceBinder = (RadioService.RadioServiceBinder) iBinder;
            radioService = radioServiceBinder.getRadioService();
            bound = true;
            Toast.makeText(getActivity(),"Am seeing tins"+bound,Toast.LENGTH_LONG).show();
            Log.d("Detailfrag",""+bound);
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
        Toast.makeText(getActivity(),"Oncreate na me"+bound,Toast.LENGTH_LONG).show();
        Log.d("Detailfrag","Nust work");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        audio = view.findViewById(R.id.audio_view);
        bar = view.findViewById(R.id.progress_bar);
        connectionPoint();
        progressBarDet();
        return view;
    }


    private void connectionPoint(){
        if(bound && radioService != null ){
            Toast.makeText(getActivity(),"connectionpoint"+bound,Toast.LENGTH_LONG).show();
            SimpleExoPlayer player = radioService.getPlayer();
            audio.setPlayer(player);
            Log.d("Detailfrag","I am connecting");
        }
    }


    //This method determines if the progress bar shows or not
    private void progressBarDet() {
        if (bound) {

        off = radioService.getBardetOff();
        show = radioService.getBardet();
            if (off == 2) {
                bar.setVisibility(View.VISIBLE);
            }

            if (show == 3) {
                bar.setVisibility(View.GONE);
            }
        }
    }

}