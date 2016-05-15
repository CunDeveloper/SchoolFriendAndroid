package com.nju.event;

/**
 * Created by cun on 2016/5/13.
 */
public class MessageDynamicCollectEvent {

    private String imgPath;
    private String text;

    public MessageDynamicCollectEvent(){

    }

    public MessageDynamicCollectEvent(String imgPath) {
        this.imgPath = imgPath;
    }

    public MessageDynamicCollectEvent(String imgPath, String text) {
        this.imgPath = imgPath;
        this.text = text;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
