package com.nju.test;

/**
 * Created by cun on 2016/3/31.
 */
public class Test {
    public static void main(String args[]) {
        String strs = "a,b,c";
        for (String t : strs.split(",")) {
            System.out.print(t);
        }
    }
}
