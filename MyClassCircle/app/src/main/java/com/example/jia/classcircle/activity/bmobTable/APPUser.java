package com.example.jia.classcircle.activity.bmobTable;

import android.graphics.Bitmap;

import java.sql.Blob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jia on 2017/9/17.
 */

public class APPUser extends BmobUser {

    private String IDNum;
    private String ClassName;
    private String headImgPath;
    private String phoneNumber;
    private String indentity;
    private boolean ifJoinClass;//管理员无需判断
    private byte[] img_msg;
    public boolean isIfJoinClass() {
        return ifJoinClass;
    }

    public void setIfJoinClass(boolean ifJoinClass) {
        this.ifJoinClass = ifJoinClass;
    }




    public byte[] getImg_msg() {
        return img_msg;
    }

    public void setImg_msg(byte[] img_msg) {
        this.img_msg = img_msg;
    }
    public String getIDNum() {
        return IDNum;
    }

    public void setIDNum(String IDNum) {
        this.IDNum = IDNum;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getHeadImgPath() {
        return headImgPath;
    }

    public void setHeadImgPath(String headImgPath) {
        this.headImgPath = headImgPath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIndentity() {
        return indentity;
    }

    public void setIndentity(String indentity) {
        this.indentity = indentity;
    }




}
