package com.nju.model;

/**
 * Created by cun on 2016/5/12.
 */
public class VoiceCollect {
    private int id;
    private String date;
    private AlumniVoice alumnusVoice;
    private int authorId;

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
}
