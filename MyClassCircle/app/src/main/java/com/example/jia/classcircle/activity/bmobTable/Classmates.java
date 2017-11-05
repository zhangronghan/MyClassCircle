package com.example.jia.classcircle.activity.bmobTable;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by jia on 2017/10/20.
 */

public class Classmates implements Serializable{//用于展示朋友页的同班同学
    private Bitmap headImg;
    private String userName;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    private String identity;

    public Bitmap getHeadImg() {
        return headImg;
    }

    public void setHeadImg(Bitmap headImg) {
        this.headImg = headImg;
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
