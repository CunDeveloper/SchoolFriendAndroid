package com.nju.model;

/**
 * Created by cun on 2016/3/28.
 */
public class RespPraise extends BaseEntity {
    private AuthorInfo praiseAuthor;
    private int contentId;

    public AuthorInfo getPraiseAuthor() {
        return praiseAuthor;
    }

    public void setPraiseAuthor(AuthorInfo praiseAuthor) {
        this.praiseAuthor = praiseAuthor;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
