package com.fxi.opn.model;

import java.util.List;

/**
 * Created by seki on 18/6/20.
 */
public class AjaxResponseBody<E> {
    String msg;
    List<E> result;
    public List<Integer> subTopics;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }

    public List<E> getResult() {
        return result;
    }

    public void setSubTopics(List<Integer> subTopics) {
        this.subTopics = subTopics;
    }

    public List<Integer> getSubTopics() {
        return subTopics;
    }
}
