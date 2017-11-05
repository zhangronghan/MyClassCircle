package com.example.jia.classcircle.activity.bmobTable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/9/21.
 */

public class HomeWork extends BmobObject{
    private String date;
    private APPUser appUser;//作业的发布者，这里体现的是一对一的关系，该作业属于某个用户
    private List list_homeWorkPath=new ArrayList();
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public APPUser getAppUser() {
        return appUser;
    }

    public void setAppUser(APPUser appUser) {
        this.appUser = appUser;
    }

    public List getList_homeWorkPath() {
        return list_homeWorkPath;
    }

    public void setList_homeWorkPath(List list_homeWorkPath) {
        this.list_homeWorkPath = list_homeWorkPath;
    }


}
