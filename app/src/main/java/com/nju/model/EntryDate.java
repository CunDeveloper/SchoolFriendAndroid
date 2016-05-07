package com.nju.model;

/**
 * Created by cun on 2016/4/6.
 */
public class EntryDate implements Comparable {
    private String month;
    private String day;

    public EntryDate(String month, String day) {
        this.month = month;
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntryDate)) {
            return false;
        } else {
            EntryDate that = (EntryDate) obj;
            return this.day.equals(that.day) &&
                    this.month.equals(that.month);
        }
    }

    @Override
    public int hashCode() {
        int hash = this.day.hashCode();
        hash = hash * 31 + this.month.hashCode();
        return hash;
    }

    @Override
    public int compareTo(Object another) {
        EntryDate that = (EntryDate) another;
        int compare = this.month.compareTo(that.month);
        if (compare != 0) {
            return compare;
        } else {
            return this.day.compareTo(that.day);
        }
    }
}
