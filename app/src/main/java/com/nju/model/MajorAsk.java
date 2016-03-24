package com.nju.model;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class MajorAsk extends BaseEntity {

    private String problem;
    private String description;
    private String imgPaths;
    private AuthorInfo author;
    private int replayCount;
    private boolean isSolved;
    private int whoScan;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(String imgPaths) {
        this.imgPaths = imgPaths;
    }

    public AuthorInfo getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInfo author) {
        this.author = author;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }

    public int getWhoScan() {
        return whoScan;
    }

    public void setWhoScan(int whoScan) {
        this.whoScan = whoScan;
    }
}
