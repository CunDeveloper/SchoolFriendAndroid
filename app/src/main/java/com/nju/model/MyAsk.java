package com.nju.model;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/25.
 */
public class MyAsk implements Comparable {
    private EntryDate entryDate;
    private ArrayList<AlumniQuestion> alumniQuestions;

    @Override
    public int compareTo(Object another) {
        MyAsk that = (MyAsk) another;
        return this.getEntryDate().compareTo(that.getEntryDate());
    }

    public EntryDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(EntryDate entryDate) {
        this.entryDate = entryDate;
    }

    public ArrayList<AlumniQuestion> getAlumniQuestions() {
        return alumniQuestions;
    }

    public void setAlumniQuestions(ArrayList<AlumniQuestion> alumniQuestions) {
        this.alumniQuestions = alumniQuestions;
    }
}
