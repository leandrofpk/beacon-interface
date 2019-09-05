package com.example.beacon.shared;

public class CipherSuiteBuilder {

    public static final ICipherSuite build(int suite){
        if (suite==0){
            return new CipherSuiteZero();
        } else {
            throw new CipherSuiteAlgoritmException("Invalid suite");
        }
    }

}
