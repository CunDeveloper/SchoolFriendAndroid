package com.nju.event;

/**
 * Created by cun on 2016/4/24.
 */
public class RecommendWorkTypeEvent {
    private String type;

    public RecommendWorkTypeEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
