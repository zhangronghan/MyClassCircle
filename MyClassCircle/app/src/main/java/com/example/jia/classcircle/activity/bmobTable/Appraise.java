package com.example.jia.classcircle.activity.bmobTable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/15.
 */


//个人
public class Appraise extends BmobObject{
    private String appraiseTitle;  //评优的标题
    private String appraisedName;  //被评优的同学
    private String imageHeader;    //被评优同学的头像
    private String introduce;      //介绍
    private List<String> agreeName;      //赞成的名字
    private int agreeNum;          //赞成的数目

    public String getAppraiseTitle() {
        return appraiseTitle;
    }

    public void setAppraiseTitle(String appraiseTitle) {
        this.appraiseTitle = appraiseTitle;
    }

    public String getAppraisedName() {
        return appraisedName;
    }

    public void setAppraisedName(String appraisedName) {
        this.appraisedName = appraisedName;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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
}
