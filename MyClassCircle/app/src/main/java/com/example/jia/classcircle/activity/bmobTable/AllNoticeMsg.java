package com.example.jia.classcircle.activity.bmobTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/10/14.
 */

public class AllNoticeMsg extends BmobObject {

    private APPUser managerUser;//
    private String  className;
    private String  noticeContent;
    private String  noticeTime;
    public APPUser getManagerUser() {
        return managerUser;
    }

    public void setManagerUser(APPUser managerUser) {
        this.managerUser = managerUser;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }



}
