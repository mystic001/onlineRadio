package com.mystic.onlineradio;

import java.util.ArrayList;
import java.util.List;

public class RadioCompany {
    private static RadioCompany sRadioCompany ;
    List<RadioBluePrint> radioHub = new ArrayList<>();


    private RadioCompany() {
        addRadio();
    }

    public static RadioCompany get(){

        if(sRadioCompany == null ){
            sRadioCompany = new RadioCompany();
        }
        return sRadioCompany;

    }
    private void addRadio() {
        RadioBluePrint freshFM = new RadioBluePrint("http://20323.live.streamtheworld.com/ASPEN_SC", "Fresh Fm");
        radioHub.add(freshFM);

        RadioBluePrint rickFM = new RadioBluePrint("http://voa28.akacast.akamaistream.net/7/325/437810/v1/ibb.akacast.akamaistream.net/voa28","Cool fm");
        radioHub.add(rickFM);
    }


    public List<RadioBluePrint> getRadioHub(){
        return radioHub;
    }



}
