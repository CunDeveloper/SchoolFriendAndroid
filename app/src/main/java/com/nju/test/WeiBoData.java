package com.nju.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.nju.activity.R;
import com.nju.model.Comment;
import com.nju.model.FriendWeibo;
import com.nju.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class WeiBoData {

    public static List<FriendWeibo> weiBos(Context context){
        List<FriendWeibo> weibos = new ArrayList<>();
        FriendWeibo friendWeibo;
        ArrayList<Bitmap> list;
        User user;
        List<Comment> comments;
        Comment comment;
        Resources resources = context.getResources();
        /**
         * test1
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("sers expect a modern UI to transition smoothly between states." +
                " UI elements fade in and out instead of appearing and disappearing. Motions begin" +
                " and end smoothly instead of starting and stopping abruptly.");
        list = new ArrayList<>();
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.cheese_2);
        list.add(bitmap);
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.cheese_1);
        list.add(bitmap);
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.cheese_3);
        list.add(bitmap);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setImages(list);
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        weibos.add(friendWeibo);

        /**
         * test2
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("0I am drawing the table dynamically in my code." +
                " The tablerow I get from layout which I am just hardcoding in the XML." +
                " I dont know why I am getting this error and am completely out of ideas.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);


        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        weibos.add(friendWeibo);

        /**
         * test3
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("A document is a written," +
                " drawn, presented or recorded representation of thoughts." +
                " Originating from the Latin Documentum meaning lesson - the verb doceō means to teach, " +
                "and is pronounced similarly, in the past it was usually used as a term for a written proof " +
                "used as evidence. In the computer age, a document is usually used to describe a primarily " +
                "textual file,along with its structure and design, such as fonts, colors and additional images.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("");
        weibos.add(friendWeibo);


        /**
         * test4
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("Aviation to many seems an esoteric subject. It covers an intricate combination of people, machines and environment. Because of the technological façade that predominates aviation, people are almost unaware of its aesthetic make-up - particularly that of flight. This has serious implications for aviation education. Even the professionals handling the safety of flights and passengers are sort of[clarification needed] realization of literary values of aviation that basically deals with flights. Scholarly intervention for educating youths and aviation professionals on matters of safety, too, is rare. The object of research may be the aviation documents dedicated to the safety of flights and of the human beings who travel by air, which contains a plethora of concrete and symbolic indications of physical and mental phenomena that it[clarification needed] has recorded");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        weibos.add(friendWeibo);


        /**
         * test5
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("The page layout of a document is the manner in which information is graphically arranged in the document space");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("");
        weibos.add(friendWeibo);


        /**
         * test6
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("Traditionally, the medium of a document was paper and the information was applied to it as ink, either by hand (to make a hand-written document) or by a mechanical process (such as a printing press or, more recently, a laser printer).\n" +
                "\n" +
                "Through time, documents have also been written with ink on papyrus (starting in ancient Egypt) or parchment; scratched as runes or carved on stone using a sharp apparatus (such as the Tablets of Stone described in the bible); stamped or cut into clay and then baked to make clay tablets (e.g., in the Sumerian and other Mesopotamian civilisations). The paper, papyrus or parchment might be rolled up as a scroll or cut into sheets and bound into a book. Today short documents might also consist of sheets of paper stapled together.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        weibos.add(friendWeibo);


        /**
         * test7
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("Briet, S. (1951). Qu'est-ce que la documentation? Paris: Documentaires Industrielles et Techniques.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        weibos.add(friendWeibo);

        /**
         * test8
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("<h1>hello world</h1>");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("");
        weibos.add(friendWeibo);


        /**
         * test9
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("1I am drawing the table dynamically in my code." +
                " The tablerow I get from layout which I am just hardcoding in the XML." +
                " I dont know why I am getting this error and am completely out of ideas.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("");
        weibos.add(friendWeibo);


        /**
         * test10
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("2I am drawing the table dynamically in my code." +
                " The tablerow I get from layout which I am just hardcoding in the XML." +
                " I dont know why I am getting this error and am completely out of ideas.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);


        friendWeibo.setPraiseUserName("张飞");
        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("上海.德国中心");
        weibos.add(friendWeibo);

        /**
         * test11
         */
        friendWeibo = new FriendWeibo();
        friendWeibo.setContent("3I am drawing the table dynamically in my code." +
                " The tablerow I get from layout which I am just hardcoding in the XML." +
                " I dont know why I am getting this error and am completely out of ideas.");
        list = new ArrayList<>();
        friendWeibo.setImages(list);

        user = new User();
        user.setHigherSchool("南京大学");
        user.setName("张小军");
        user.setXueYuan("软件学院");
        user.setStartYear("2014");
        friendWeibo.setUser(user);

        comments = new ArrayList<>();
        comment = new Comment();
        comment.setcUserName("张飞龙");
        comment.setContent("在上海ebay");
        comments.add(comment);
        comment = new Comment();
        comment.setcUserName("张小军");
        comment.setContent("very good!");
        comments.add(comment);
        friendWeibo.setComments(comments);

        friendWeibo.setHeadIcon(resources.getDrawable(R.drawable.head2));
        friendWeibo.setPublishDate("1小时前");
        friendWeibo.setLocation("");
        weibos.add(friendWeibo);

        return  weibos;
    }
}
