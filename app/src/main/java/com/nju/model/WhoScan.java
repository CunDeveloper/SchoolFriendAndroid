package com.nju.model;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2015/12/2.
 */
public class WhoScan {
    private String bigLabel;
    private String smallLabel;
    private String chooseLabel;
    private ArrayList<ChildItem> childItems;

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

    public String getChooseLabel() {
        return chooseLabel;
    }

    public void setChooseLabel(String chooseLabel) {
        this.chooseLabel = chooseLabel;
    }

    public ArrayList<ChildItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(ArrayList<ChildItem> childItems) {
        this.childItems = childItems;
    }

    public static class ChildItem {
        private String childBigLabel;
        private String childSmallLabel;

        public ChildItem() {

        }

        public String getChildBigLabel() {
            return childBigLabel;
        }

        public void setChildBigLabel(String childBigLabel) {
            this.childBigLabel = childBigLabel;
        }

        public String getChildSmallLabel() {
            return childSmallLabel;
        }

        public void setChildSmallLabel(String childSmallLabel) {
            this.childSmallLabel = childSmallLabel;
        }

    }
}
