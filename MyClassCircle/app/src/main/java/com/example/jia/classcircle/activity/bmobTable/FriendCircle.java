package com.example.jia.classcircle.activity.bmobTable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/11/3.
 */

//朋友圈
public class FriendCircle extends BmobObject{
    private String username;       //发表的人
//    private byte[] userImage;  //发表的人头像
    private String className;  //班级名
    private String content;        //发表的内容
    private List<String> mStringList;      //别人的评价  要在评价的内容前添加姓名

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

/*    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }*/

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getStringList() {
        return mStringList;
    }

    public void setStringList(List<String> stringList) {
        mStringList = stringList;
    }
}
