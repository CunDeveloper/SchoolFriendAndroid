package com.nju.model;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/26.
 */
public class MyDynamic implements Comparable<MyDynamic> {

    private EntryDate entryDate;
    private ArrayList<AlumniTalk> alumniTalks;


    public EntryDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(EntryDate entryDate) {
        this.entryDate = entryDate;
    }

    public ArrayList<AlumniTalk> getAlumniTalks() {
        return alumniTalks;
    }

    public void setAlumniTalks(ArrayList<AlumniTalk> alumniTalks) {
        this.alumniTalks = alumniTalks;
    }

    @Override
    public int compareTo(MyDynamic another) {
        return this.getEntryDate().compareTo(another.getEntryDate());
    }
}
