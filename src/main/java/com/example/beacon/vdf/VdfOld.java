package com.example.beacon.vdf;

import com.example.beacon.vdf.util.HashUtil;

import java.util.List;

public class VdfOld {

    private List<String> seed;

    public Pp setup(String random, int minutes){

        return new Pp();
    }

    public String[] eval(Pp pp, String inputX){

        for (int i = 0; i < 10; i++) {
            // Fazer hash recursivamente
            HashUtil.getDigest("");

        }

        String[] doReturn = {"y", "proof"};
        return doReturn;
    }

    public boolean verify(Pp pp, String x, String y, String proof){
        return true;
    }

}
