package com.nju.http.request;

/**
 * Created by cun on 2016/4/23.
 */
public class QueryLimitLabel extends QueryLimit {
    private String label;

    public QueryLimitLabel(){}

    public QueryLimitLabel(int rowId, int limit, String dir,String label) {
        super(rowId,limit,dir);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
