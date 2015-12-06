package com.nju.model;

/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class User {

    private int id;
    private String name;
    private String higherSchool;
    private String xueYuan;
    private String startYear;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHigherSchool() {
        return higherSchool;
    }

    public void setHigherSchool(String higherSchool) {
        this.higherSchool = higherSchool;
    }

    public String getXueYuan() {
        return xueYuan;
    }

    public void setXueYuan(String xueYuan) {
        this.xueYuan = xueYuan;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }
}
