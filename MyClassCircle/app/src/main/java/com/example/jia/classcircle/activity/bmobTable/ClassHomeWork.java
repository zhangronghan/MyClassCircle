package com.example.jia.classcircle.activity.bmobTable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/9/21.
 */

public class ClassHomeWork extends BmobObject{

    private String className;
    private String userName;
    private String homeworlUrl;//
    private String time;
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

    public String getHomeworlUrl() {
        return homeworlUrl;
    }

    public void setHomeworlUrl(String homeworlUrl) {
        this.homeworlUrl = homeworlUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
