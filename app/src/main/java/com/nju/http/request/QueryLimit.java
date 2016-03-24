package com.nju.http.request;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class QueryLimit {

    private int offset;
    private int total;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString(){
        return "{offest:"+offset+",total:"+total+"}";
    }
}
