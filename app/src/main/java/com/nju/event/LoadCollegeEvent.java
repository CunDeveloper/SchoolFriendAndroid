package com.nju.event;

/**
 * Created by cun on 2016/5/16.
 */
public class LoadCollegeEvent {
    private String ok;
    public LoadCollegeEvent(String okMsg) {
        this.ok = okMsg;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}
