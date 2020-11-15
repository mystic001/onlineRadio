package com.mystic.onlineradio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioFragment extends Fragment {

    public static final String RADIO = "Radio";
    public static final String STOP = "STOP";
    public  static  final String PLAY = "PLAY";
    private static final int FIRST_RADIO_POS = 0;
    private static final int SECOND_RADIO_POS = 1;
    private Button firstPlay,secondPlay;
    private TextView firstName , secondName;


    private List<RadioBluePrint> radioStore;



    public RadioFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RadioFragment newInstance() {
        return  new RadioFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioStore = RadioCompany.get().getRadioHub();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_radio, container, false);
        definations(view);
        firstName.setText(radioStore.get(0).getName());
        secondName.setText(radioStore.get(1).getName());

        firstPlay.setOnClickListener(view12 -> {
            if (!isNetwortConnectedAvailable()) {
                Toast.makeText(getActivity(),"Not connected",Toast.LENGTH_SHORT).show();
                return;
            }


                DetailFragment fragment = DetailFragment.newInstance(radioStore.get(FIRST_RADIO_POS));
                firstPlay.setText(STOP);
                if(getActivity() != null){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .commit();


            }


        });

        secondPlay.setOnClickListener(view1 -> {
            if (!isNetwortConnectedAvailable()) {
                Toast.makeText(getActivity(),"Not connected",Toast.LENGTH_SHORT).show();
                return;
            }
            DetailFragment fragment = DetailFragment.newInstance(radioStore.get(SECOND_RADIO_POS));
            if(getActivity() != null){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commit();

            }

        });

        //firstPlay.setOnClickListener(view1 -> personalStart(FIRST_RADIO_POS,firstPlay));
        //secondPlay.setOnClickListener(view12 -> personalStart(SECOND_RADIO_POS,secondPlay));
        return view;
    }

   /* private void personalStart(int pos, Button pressed){
        if (!isNetwortConnectedAvailable()) {
            Toast.makeText(getActivity(),"Not connected",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), RadioService.class);
        RadioBluePrint realRadio = radioStore.get(pos);
        intent.putExtra(RadioFragment.RADIO,realRadio);
        if(!realRadio.isRunning() && getActivity() != null ){
            pressed.setText(STOP);
            realRadio.setRunning(true);
            getActivity().startService(intent);
        } else{
            pressed.setText(PLAY);
            realRadio.setRunning(false);
            if(getActivity() != null){
                getActivity().stopService(intent);
            }
        }

    }*/



    private boolean isNetwortConnectedAvailable(){
        if(getActivity() != null){
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
            return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        }

        return false;
    }



    private void definations(View view) {
        firstPlay = view.findViewById(R.id.buttonone);
        secondPlay = view.findViewById(R.id.buttontwo);
        firstName = view.findViewById(R.id.radname);
        secondName = view.findViewById(R.id.secondname);
    }
}