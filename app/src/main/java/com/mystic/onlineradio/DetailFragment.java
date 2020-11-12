package com.mystic.onlineradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private StyledPlayerControlView audio;
    private RadioService radioService;
    private boolean bound = false;


    // TODO: Rename and change types of parameters
    private RadioBluePrint radioBluePrint;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RadioService.RadioServiceBinder radioServiceBinder = (RadioService.RadioServiceBinder) iBinder;
            radioService = radioServiceBinder.getRadioService();
            bound = true;
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
            Intent intent = new Intent(getActivity(), RadioService.class);
            getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            intent.putExtra(RadioFragment.RADIO,radioBluePrint);
            getActivity().startService(intent);
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
        TextView textView = view.findViewById(R.id.radionamedetail);
        textView.setText(radioBluePrint.getName());
        audio = view.findViewById(R.id.audio_view);
        connectionPoint();
        return view;
    }


    private void connectionPoint(){
        if(bound && radioService != null ){
            SimpleExoPlayer player = radioService.getPlayer();
            audio.setPlayer(player);
        }
    }
}