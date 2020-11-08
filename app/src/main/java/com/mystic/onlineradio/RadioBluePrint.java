package com.mystic.onlineradio;

public class RadioBluePrint {

    private String Url;

    public RadioBluePrint(String url) {
        Url = url;
    }

    public void setUrl(String url){
        Url = url ;
    }

    public String getUrl(){
        return Url ;
    }
}
