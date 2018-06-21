package com.fxi.opn.model;

import java.util.List;

/**
 * Created by seki on 18/6/20.
 */
public class SearchCriteria {
    private String fpId ;

    private String ip;

    private String city;

    private boolean init;

    public List<Integer> topics;

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

    public void setTopics(List<Integer> topics) {
        this.topics = topics;
    }

    public List<Integer> getTopics() {
        return topics;
    }
}
