package com.nju.model;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/2.
 */
public class Entry {

    private String bigLabel;
    private String smallLabel;
    private String drawable;
    private ArrayList<Entry> childItems;

    public String getBigLabel() {
        return bigLabel;
    }

    public void setBigLabel(String bigLabel) {
        this.bigLabel = bigLabel;
    }

    public String getSmallLabel() {
        return smallLabel;
    }

    public void setSmallLabel(String smallLabel) {
        this.smallLabel = smallLabel;
    }

    public ArrayList<Entry> getChildItems() {
        return childItems;
    }

    public void setChildItems(ArrayList<Entry> childItems) {
        this.childItems = childItems;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }
}
