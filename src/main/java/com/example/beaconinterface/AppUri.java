package com.example.beaconinterface;

import javax.servlet.http.HttpServletRequest;

public class AppUri {

    private final HttpServletRequest request;

    public AppUri(HttpServletRequest request) {
        this.request = request;
    }

    public String getUri(){
        if (request.getServerPort() != 443){
            return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        } else {
            return "https://" + request.getServerName() + request.getContextPath();
        }
    }

}
