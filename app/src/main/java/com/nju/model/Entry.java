package com.nju.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/2.
 */
public class Entry {

    private String bigLabel;
    private String samllLabel;
    private Drawable drawable;
    private ArrayList<Entry> childItems;
    public String getBigLabel() {
        return bigLabel;
    }

    public void setBigLabel(String bigLabel) {
        this.bigLabel = bigLabel;
    }

    public String getSamllLabel() {
        return samllLabel;
    }

    public void setSamllLabel(String samllLabel) {
        this.samllLabel = samllLabel;
    }

    public ArrayList<Entry> getChildItems() {
        return childItems;
    }

    public void setChildItems(ArrayList<Entry> childItems) {
        this.childItems = childItems;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
