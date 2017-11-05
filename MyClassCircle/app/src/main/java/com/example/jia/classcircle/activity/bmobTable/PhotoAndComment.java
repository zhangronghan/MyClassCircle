package com.example.jia.classcircle.activity.bmobTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/9/21.
 */

public class PhotoAndComment extends BmobObject {//相册

    private String className;
    private String ClientName;
    private byte[] img_msg;
    private String commentContent;
    private String commentTime;
    private String photo;
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public byte[] getImg_msg() {
        return img_msg;
    }

    public void setImg_msg(byte[] img_msg) {
        this.img_msg = img_msg;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



}
