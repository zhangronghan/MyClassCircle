package com.example.jia.classcircle.activity.bmobTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/10/13.
 */

public class GetClientsTalk extends BmobObject {
    private String className;
    private String userName;
    private String speakContent;
    private String speakTime;
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

    public String getSpeakContent() {
        return speakContent;
    }

    public void setSpeakContent(String speakContent) {
        this.speakContent = speakContent;
    }

    public String getSpeakTime() {
        return speakTime;
    }

    public void setSpeakTime(String speakTime) {
        this.speakTime = speakTime;
    }


}
