package com.fxi.opn.model;

import java.util.List;

/**
 * Created by seki on 18/6/20.
 */
public class AjaxResponseBody<E> {
    String msg;
    List<E> result;

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
}
