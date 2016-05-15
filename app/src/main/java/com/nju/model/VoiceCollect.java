package com.nju.model;

/**
 * Created by cun on 2016/5/12.
 */
public class VoiceCollect implements Comparable<VoiceCollect> {
    private int id;
    private String date;
    private AlumniVoice alumnusVoice;
    private int authorId;
    private int check = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AlumniVoice getAlumnusVoice() {
        return alumnusVoice;
    }

    public void setAlumnusVoice(AlumniVoice alumnusVoice) {
        this.alumnusVoice = alumnusVoice;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public int compareTo(VoiceCollect another) {
        if (this.getId() > another.getId()){
            return 1;
        } else if (this.getId() < another.getId()) {
            return -1;
        }
        return 0;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
