package com.example.beacon.interfac.infra;

import javax.servlet.http.HttpServletRequest;

public class AppUri {

    private final HttpServletRequest request;

    public AppUri(HttpServletRequest request) {
        this.request = request;
    }

    public String getUri(){
        if (request.getServerPort() != 443 && request.getServerPort() != 80){
            return request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        } else {
            return request.getServerName() + request.getContextPath();
        }
    }
}
