package com.nju.http.request;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class QueryLimit {
    private int rowId;
    private int limit;
    private String dir;

    public QueryLimit(){}

    public QueryLimit(int rowId, int limit, String dir) {
        this.rowId = rowId;
        this.limit = limit;
        this.dir = dir;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
