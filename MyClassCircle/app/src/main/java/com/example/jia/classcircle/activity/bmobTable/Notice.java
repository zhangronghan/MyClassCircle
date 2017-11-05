package com.example.jia.classcircle.activity.bmobTable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/10/11.
 */

public class Notice extends BmobObject{//用来实现发布通知
    private APPUser user;//管理者的ID
    private String className;
    private List<ContentAndTime> noticeContentList;
    private List<FilePathAndTime> fileList;
    public APPUser getUser() {
        return user;
    }

    public void setUser(APPUser user) {
        this.user = user;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ContentAndTime> getNoticeContentList() {
        return noticeContentList;
    }

    public void setNoticeContentList(List<ContentAndTime> noticeContentList) {
        this.noticeContentList = noticeContentList;
    }

    public List<FilePathAndTime> getFileList() {
        return fileList;
    }

    public void setFileList(List<FilePathAndTime> fileList) {
        this.fileList = fileList;
    }
}
