package com.mystic.onlineradio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioFragment extends Fragment {

    public static final String RADIO_URL = "Radio link";
    private static final int FIRST_RADIO_POS = 0;
    private static final int SECOND_RADIO_POS = 1;
    private Button firstPlay,secondPlay;
    private PlayerView audioviewone, audioviewtwo;
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
        RadioCompany radioCompany =  new RadioCompany();
        radioStore = radioCompany.getRadioHub();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_radio, container, false);
        definations(view);
        firstPlay.setOnClickListener(view1 -> personalStart(FIRST_RADIO_POS));

        secondPlay.setOnClickListener(view12 -> personalStart(SECOND_RADIO_POS));
        return view;
    }

    private void personalStart(int pos){
        Intent intent = new Intent(getActivity(), RadioService.class);
        RadioBluePrint firstRadio = radioStore.get(pos);
        intent.putExtra(RadioFragment.RADIO_URL,firstRadio.getUrl());
        if(getActivity() != null){
            getActivity().startService(intent);
        }
    }

    private void definations(View view) {
        firstPlay = view.findViewById(R.id.buttonone);
        secondPlay = view.findViewById(R.id.buttontwo);
        firstName = view.findViewById(R.id.radname);
        secondName = view.findViewById(R.id.secondname);
        //audioviewone = view.findViewById(R.id.audio_view);
       // audioviewtwo = view.findViewById(R.id.audio_view_two);
    }
}