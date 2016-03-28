package com.nju.model;

import com.nju.fragment.BaseFragment;

/**
 * Created by cun on 2016/3/27.
 */
public class ContentComment extends BaseEntity {
    private AuthorInfo commentAuthor;
    private AuthorInfo commentedAuthor;
    private String content;
    private int contentId;

    public AuthorInfo getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(AuthorInfo commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public AuthorInfo getCommentedAuthor() {
        return commentedAuthor;
    }

    public void setCommentedAuthor(AuthorInfo commentedAuthor) {
        this.commentedAuthor = commentedAuthor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
