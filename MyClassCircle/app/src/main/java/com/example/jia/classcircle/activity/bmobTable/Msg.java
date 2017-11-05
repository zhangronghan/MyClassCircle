package com.example.jia.classcircle.activity.bmobTable;

import android.graphics.Bitmap;

/**
 * Created by jia on 2017/10/25.
 */

public class Msg {//用于显示一对一聊天消息
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private String content;
    private String friendName;
    private Bitmap friednImg;
    private String mineName;
    private Bitmap MineImg;

    private int type;

    public Msg(String content, int type) {
        this.content=content;
        this.type=type;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Bitmap getFriednImg() {
        return friednImg;
    }

    public void setFriednImg(Bitmap friednImg) {
        this.friednImg = friednImg;
    }

    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    public Bitmap getMineImg() {
        return MineImg;
    }

    public void setMineImg(Bitmap mineImg) {
        MineImg = mineImg;
    }
}
