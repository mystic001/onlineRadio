package com.mystic.onlineradio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
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
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioFragment extends Fragment {

    public static final String RADIO = "Radio";
    private static final int FIRST_RADIO_POS = 0;
    private static final int SECOND_RADIO_POS = 1;
    private Button firstPlay,secondPlay;
    private TextView firstName , secondName;
    private static final String CHANNEL_ID = "ChannelID";
    private static final int NOTIFICATION_ID = 50;


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
                if(getActivity() != null){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .commit();
                    showNotif(radioStore.get(FIRST_RADIO_POS));

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
                showNotif(radioStore.get(SECOND_RADIO_POS));
            }

        });

        //firstPlay.setOnClickListener(view1 -> personalStart(FIRST_RADIO_POS,firstPlay));
        //secondPlay.setOnClickListener(view12 -> personalStart(SECOND_RADIO_POS,secondPlay));
        return view;
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
            assert getActivity()  != null;
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showNotif(RadioBluePrint radio){
        createNotificationChannel();;
        assert getActivity() != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.exo_icon_circular_play)
                .setContentTitle("Radio is playing")
                .setContentText(radio.getName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[] {0, 1000})
                .setAutoCancel(true);


        Intent actionIntent = new Intent(getActivity(), RadioActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(getActivity(), 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(actionPendingIntent);
        assert getActivity() != null;
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


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