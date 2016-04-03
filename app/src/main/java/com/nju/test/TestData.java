package com.nju.test;

import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniVoice;
import com.nju.model.AuthorInfo;
import com.nju.model.ContentComment;
import com.nju.model.DynamicCollect;
import com.nju.model.RecommendWork;
import com.nju.util.StringBase64;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by xiaojuzhang on 2016/3/15.
 */
public class TestData {

    public static HashSet<String> voiceTypes(){
        HashSet<String> sets = new HashSet<>();
        sets.add("工作");
        sets.add("学习");
        sets.add("实习");
        sets.add("求职");
        sets.add("兼职");
        sets.add("接项目");
        return sets;
    }

    public static String[] getMajorType(){
        String[] types = new String[6];
        types[0]="C++";
        types[1]= "C";
        types[2] = "JAVA";
        types[3] = "VC";
        types[4] = "Python";
        types[5] = "JavaScript";
        return types;
    }
    public static HashSet<String> getUndergraduateCollege() {
        HashSet<String> sets = new HashSet<>();
        sets.add("文学系");
        sets.add("外国语学院");
        sets.add("哲学系（宗教学系）");
        sets.add("历史学院");
        sets.add("物理学院");
        sets.add("数学系");
        sets.add("天文与空间科学学院");
        sets.add("地球科学与工程学院");
        sets.add("大气科学学院");
        sets.add("地理与海洋科学学院");
        sets.add("计算机科学与技术系");
        sets.add("电子科学与工程学院");
        sets.add("现代工程与应用科学学院");
        sets.add("新闻传播学院");
        sets.add("商学院");
        sets.add("社会学院");
        sets.add("社会学系");
        sets.add("马克思主义学院");
        sets.add("行政管理学系");
        sets.add("模式动物研究所");
        sets.add("中华文化研究院");
        sets.add("创新创业学院");
        sets.add("海外教育学院");
        sets.add("河仁社会慈善学院");
        return sets;
    }

    public static ArrayList<AlumniQuestion> getQlumniQuestions() {
        ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
        AlumniQuestion alumniQuestion = new AlumniQuestion();
        alumniQuestion.setProblem(StringBase64.encode("如何改变Toggle Button的背景颜色在Android里,当用户点击时"));
        alumniQuestion.setDescription(StringBase64.encode("i tried to change the background " +
                "color of the toggle button using XML file as white color but " +
                "the toggle button is totally damaged it look like all the button" +
                " was covered with white. There is no indication of ON or OFF on the " +
                "toggle button when i have changed the color of the toggle button to" +
                " white. Can any one please tell me, Is there is any other way to change the background" +
                " which will not damage the indication of the toggle button ."));
        alumniQuestion.setDate("2016-03-16");
        alumniQuestion.setReplayCount(9);
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("Jimmy");
        alumniQuestion.setAuthor(authorInfo);
        alumniQuestions.add(alumniQuestion);
        return alumniQuestions;
    }

    public static ArrayList<AlumniVoice> getVoicesData() {
        ArrayList<AlumniVoice> voices = new ArrayList<>();
        AlumniVoice voice = new AlumniVoice();
        voice.setCommentCount(1);
        voice.setPraiseCount(1);
        voice.setTitle(StringBase64.encode("大四学生就业建议"));
        voice.setContent(StringBase64.encode("1.树立自己就业的信心。\n" +
                "大学生数目的增多，只能说明自己所处的平台更高、更宽，应该对自己就业有信心，\n" +
                "坚信自己一定能找到适合自己的企业和岗位。 　\n" +
                "2.树立找到适合自己职业发展机会和平台的理念。适合的是最好的。 　\n" +
                "3．应该树立良好的就业观念，品德素质要足够重视，不应该受冷落，品行兼修，并且都优秀。 　　\n" +
                "4.多参与用人单位就业讲座或者其它就业讲座建立双方更流畅、有效的沟通交流渠道。大学生对用人单位不了解，\n" +
                "用人单位的人才要求不能畅通、有效的让大学生了解，这其中缺乏有效沟通的平台。 　\n" +
                "5.学校邀请就业比较成功的大学生定期回校进行就业交流沟通.多让部分有社会经验和工作经验的人士回校讲课,演讲,或者成为授课教师。(特别是营销类,管理类)。 " +
                "\n6.学校尽量建立企业的HR、管理层来校..."));
        voice.setDate("2015-03-20");
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("张秦");
        authorInfo.setLabel("南京大学 软件学院");
        voice.setAuthorInfo(authorInfo);
        voices.add(voice);
        return voices;
    }

    public static ArrayList<RecommendWork> getRecommendWorks() {
        ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
        RecommendWork recommendWork = new RecommendWork();
        recommendWork.setTitle(StringBase64.encode("百度质量部-百度云测试开发工程师"));
        recommendWork.setContent(StringBase64.encode("（Android/iOS、server方向）。参与百度云服务器、客户端的质量保证以及质量工具、平台开发 \n" +
                "要求： \n" +
                "（1）计算机相关专业本科以上、实习时间长者优先（至少6个月） \n" +
                "（2）熟悉基本数据结构和算法设计，精通C/C++、Java、PHP, shell中至少一门编程语言 \n" +
                "（3） 熟悉Android/IOS自动化测试工具的使用和编写，如junit、instrumentation、robotium、monkey、monkeyrunner等 、熟悉java、object-C相关经验者优先 \n" +
                "\n" +
                "请将简历投递至: "));
        recommendWork.setEmail("wen_twinkle@126.com");
        recommendWork.setCommentCount(2);
        recommendWork.setType(0);
        recommendWork.setDate("2016-03-10");
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorId(1);
        authorInfo.setAuthorName("张小军");
        authorInfo.setLabel("南京大学软件学院2014");
        recommendWork.setAuthor(authorInfo);
        recommendWorks.add(recommendWork);
        return recommendWorks;
    }

    public static ArrayList<DynamicCollect> getDynamicCollects() {
        ArrayList<DynamicCollect> dynamicCollects = new ArrayList<>();
        DynamicCollect dynamicCollect = new DynamicCollect();
        dynamicCollect.setContent(StringBase64.encode("你咋不上天啦！"));
        dynamicCollect.setDate("今天");
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("刘玲");
        authorInfo.setLabel("南京大学商学院2013");
        dynamicCollect.setAuthorInfo(authorInfo);
        dynamicCollects.add(dynamicCollect);
        dynamicCollect = new DynamicCollect();
        dynamicCollect.setContent(StringBase64.encode("奋战在毕业设计第一线！"));
        dynamicCollect.setDate("2016-2-23");
        authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("刘倩倩");
        authorInfo.setLabel("南京大学软件学院2014");
        dynamicCollect.setAuthorInfo(authorInfo);
        dynamicCollects.add(dynamicCollect);

        dynamicCollect = new DynamicCollect();
        dynamicCollect.setDate("2016-1-23");
        authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("张小小");
        authorInfo.setLabel("南京大学软件学院2014");
        dynamicCollect.setAuthorInfo(authorInfo);
        dynamicCollects.add(dynamicCollect);
        return dynamicCollects;
    }

    public static ArrayList<ContentComment> getComments(){
        ArrayList<ContentComment> contentComments = new ArrayList<>();
        ContentComment contentComment = new ContentComment();
        AuthorInfo commentAuthor = new AuthorInfo();
        commentAuthor.setAuthorName("嘉兴西");
        contentComment.setCommentAuthor(commentAuthor);
        contentComment.setContent(StringBase64.encode("具体在什么时候什么地点招聘呀！"));
        contentComment.setDate("2016-3-26 12:34:56");
        contentComments.add(contentComment);
        ContentComment contentComment1 = new ContentComment();
        AuthorInfo commentAuthor1 = new AuthorInfo();
        commentAuthor1.setAuthorName("程冠希");
        contentComment1.setCommentAuthor(commentAuthor);
        contentComment1.setContent(StringBase64.encode("具体在什么时候什么地点招聘呀！"));
        contentComment1.setDate("2016-3-27 12:04:56");
        contentComments.add(contentComment1);
        return contentComments;

    }
}
