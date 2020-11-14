package com.mystic.onlineradio;


import android.app.FragmentManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class RadioActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
       return RadioFragment.newInstance();

    }

}