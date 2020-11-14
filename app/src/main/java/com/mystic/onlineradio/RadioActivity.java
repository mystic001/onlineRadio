package com.mystic.onlineradio;


import androidx.fragment.app.Fragment;

public class RadioActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

       return RadioFragment.newInstance();

    }

}