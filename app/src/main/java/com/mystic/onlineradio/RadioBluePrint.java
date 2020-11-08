package com.mystic.onlineradio;

import java.io.Serializable;

public class RadioBluePrint implements Serializable {

    private String Url;
    private String name;
    private boolean running;

    public RadioBluePrint(String url,String name) {
        this.name = name;
        running = false;
        Url = url;
    }

    public void setUrl(String url){
        Url = url ;
    }

    public String getUrl(){
        return Url ;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
