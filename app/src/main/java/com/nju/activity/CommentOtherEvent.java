package com.nju.activity;

import com.nju.model.ContentComment;

/**
 * Created by cun on 2016/4/15.
 */
public class CommentOtherEvent {

    private ContentComment comment;

    public CommentOtherEvent(ContentComment comment) {
        this.comment = comment;
    }

    public ContentComment getComment() {
        return comment;
    }

    public void setComment(ContentComment comment) {
        this.comment = comment;
    }
}
