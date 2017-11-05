package com.example.jia.classcircle.activity.bmobTable;

/**
 * Created by jia on 2017/10/16.
 */

public class StuAboutCheck {//这个类是为了记录attendance表而服务的

    private String stuName;
    private String Time;
    private String managerTime;
    private boolean ifCheck=false;
    public String getManagerTime() {
        return managerTime;
    }

    public void setManagerTime(String managerTime) {
        this.managerTime = managerTime;
    }


    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public boolean isIfCheck() {
        return ifCheck;
    }

    public void setIfCheck(boolean ifCheck) {
        this.ifCheck = ifCheck;
    }
}
