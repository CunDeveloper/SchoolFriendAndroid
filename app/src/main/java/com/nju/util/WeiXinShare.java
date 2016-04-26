package com.nju.util;

import android.graphics.Bitmap;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created by cun on 2016/4/26.
 */
public class WeiXinShare {
    private static final int THUMB_SIZE = 150;

    public static void text(String content,String description,int ascene,IWXAPI api){
        WXTextObject textObject = new WXTextObject();
        textObject.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = ascene;
        api.sendReq(req);
    }

    public static void pic(Bitmap bitmap,int ascene,IWXAPI api){
        WXImageObject imageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,THUMB_SIZE,THUMB_SIZE,true);
        bitmap.recycle();
        msg.thumbData = ShareUtil.bmpToByteArray(thumbBmp,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = ascene;
        api.sendReq(req);
    }

    public static void music(String url,String title,String description,Bitmap thumb,int ascene,IWXAPI api){
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        Bitmap aThumb = thumb;
        msg.thumbData = ShareUtil.bmpToByteArray(aThumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = ascene;

        api.sendReq(req);
    }

    public static void video(String url,String title,String description,Bitmap thumb,int ascene,IWXAPI api){
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = url;

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;

        Bitmap aThumb = thumb;
        msg.thumbData = ShareUtil.bmpToByteArray(aThumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = ascene;

        api.sendReq(req);
    }

    public static void webPage(String url,String title,String description,Bitmap thumb,int ascene,IWXAPI api){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        Bitmap aThumb = thumb;
        msg.thumbData = ShareUtil.bmpToByteArray(aThumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = ascene;

        api.sendReq(req);
    }



    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
