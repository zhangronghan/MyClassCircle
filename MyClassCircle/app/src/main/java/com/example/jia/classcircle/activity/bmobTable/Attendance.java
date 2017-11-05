package com.example.jia.classcircle.activity.bmobTable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/10/16.
 */

public class Attendance extends BmobObject {
    private String className;
    private String userName;
    private String weekday;
    private String identity;
    private List<ContentAndTime> contentList;
    private List<StuAboutCheck> stuAboutCheckList;
    public List<StuAboutCheck> getStuAboutCheckList() {
        return stuAboutCheckList;
    }

    public void setStuAboutCheckList(List<StuAboutCheck> stuAboutCheckList) {
        this.stuAboutCheckList = stuAboutCheckList;
    }


    public List<ContentAndTime> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentAndTime> contentList) {
        this.contentList = contentList;
    }



    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }


}
