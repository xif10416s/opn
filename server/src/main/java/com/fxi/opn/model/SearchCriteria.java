package com.fxi.opn.model;

/**
 * Created by seki on 18/6/20.
 */
public class SearchCriteria {
    private String fpId ;

    private String ip;

    private String city;

    private boolean init;

    public void setFpId(String fpId) {
        this.fpId = fpId;
    }

    public String getFpId() {
        return fpId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public boolean isInit() {
        return init;
    }
}
