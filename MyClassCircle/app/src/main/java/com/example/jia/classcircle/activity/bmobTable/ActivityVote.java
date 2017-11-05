package com.example.jia.classcircle.activity.bmobTable;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/17.
 */

//个人
public class ActivityVote extends BmobObject implements Serializable{
    private String activityTitle;     //活动名
    private String joinActivityTitle;    //参与活动的标题
    private String joinActivityName;     //参与活动的同学名
    private String imageHeader;          //参与活动同学的头像
    private String content;               //内容
    private List<String> agreeName;       //赞成的名字
    private int agreeNum;                 //赞成的数目

    public String getJoinActivityTitle() {
        return joinActivityTitle;
    }

    public void setJoinActivityTitle(String joinActivityTitle) {
        this.joinActivityTitle = joinActivityTitle;
    }

    public String getJoinActivityName() {
        return joinActivityName;
    }

    public void setJoinActivityName(String joinActivityName) {
        this.joinActivityName = joinActivityName;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAgreeName() {
        return agreeName;
    }

    public void setAgreeName(List<String> agreeName) {
        this.agreeName = agreeName;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }


    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }
}
