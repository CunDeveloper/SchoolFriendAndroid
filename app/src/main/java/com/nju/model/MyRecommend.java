package com.nju.model;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/25.
 */
public class MyRecommend implements Comparable {
    private EntryDate entryDate;
    private ArrayList<RecommendWork> recommendWorks;

    public MyRecommend(EntryDate entryDate, ArrayList<RecommendWork> recommendWorks) {
        this.entryDate = entryDate;
        this.recommendWorks = recommendWorks;
    }

    public MyRecommend() {

    }


    public EntryDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(EntryDate entryDate) {
        this.entryDate = entryDate;
    }

    public ArrayList<RecommendWork> getRecommendWorks() {
        return recommendWorks;
    }

    public void setRecommendWorks(ArrayList<RecommendWork> recommendWorks) {
        this.recommendWorks = recommendWorks;
    }

    @Override
    public int compareTo(Object another) {
        MyRecommend that = (MyRecommend) another;
        return this.getEntryDate().compareTo(that.getEntryDate());
    }
}
