package com.nju.activity;

import com.nju.model.ContentComment;

/**
 * Created by cun on 2016/4/16.
 */
public class DeleteCommentEvent {
    private ContentComment comment;

    public DeleteCommentEvent(ContentComment comment) {
        this.comment = comment;
    }

    public ContentComment getComment() {
        return comment;
    }

    public void setComment(ContentComment comment) {
        this.comment = comment;
    }
}
