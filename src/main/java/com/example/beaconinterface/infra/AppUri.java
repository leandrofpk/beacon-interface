package com.example.beaconinterface.infra;

import javax.servlet.http.HttpServletRequest;

public class AppUri {

    private final HttpServletRequest request;

    public AppUri(HttpServletRequest request) {
        this.request = request;
    }

    public String getUri(){
        System.out.println(request.getServerPort());

        if ((request.getServerPort() != 443) || (request.getServerPort() != 80)){
            return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        } else {
            return "https://" + request.getServerName() + request.getContextPath();
        }
    }

}
