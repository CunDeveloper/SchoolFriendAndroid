package com.nju.activity;

import com.nju.model.AuthorInfo;

/**
 * Created by cun on 2016/4/2.
 */
public class PersonInfoEvent {
    private AuthorInfo authorInfo;

    public PersonInfoEvent(AuthorInfo authorInfo ) {
        this.authorInfo = authorInfo;
     }

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

}
