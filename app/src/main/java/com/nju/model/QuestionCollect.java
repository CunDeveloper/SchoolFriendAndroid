package com.nju.model;

/**
 * Created by cun on 2016/5/12.
 */
public class QuestionCollect {
    private int id;
    private String date;
    private AlumniQuestion alumniQuestion;
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

    public AlumniQuestion getAlumniQuestion() {
        return alumniQuestion;
    }

    public void setAlumniQuestion(AlumniQuestion alumniQuestion) {
        this.alumniQuestion = alumniQuestion;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
