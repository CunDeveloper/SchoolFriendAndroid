package com.nju.model;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/25.
 */
public class MyVoice implements Comparable<MyVoice> {

    private EntryDate entryDate;
    private ArrayList<AlumniVoice> alumniVoices;

    @Override
    public int compareTo(MyVoice another) {
        return this.getEntryDate().compareTo(another.getEntryDate());
    }

    public EntryDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(EntryDate entryDate) {
        this.entryDate = entryDate;
    }

    public ArrayList<AlumniVoice> getAlumniVoices() {
        return alumniVoices;
    }

    public void setAlumniVoices(ArrayList<AlumniVoice> alumniVoices) {
        this.alumniVoices = alumniVoices;
    }
}
