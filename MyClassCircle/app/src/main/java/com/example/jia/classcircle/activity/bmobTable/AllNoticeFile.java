package com.example.jia.classcircle.activity.bmobTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/10/14.
 */

public class AllNoticeFile extends BmobObject {


    private String  className;
    private String  noticeFileUrl;
    private String  noticeTime;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNoticeFileUrl() {
        return noticeFileUrl;
    }

    public void setNoticeFileUrl(String noticeFileUrl) {
        this.noticeFileUrl = noticeFileUrl;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

}
