package com.example.jia.classcircle.activity.bmobTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/9/21.
 */

public class Comment extends BmobObject {
    private String content;//评论内容
    private APPUser user;//评论的用户，Pointer类型，一对一关系
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public APPUser getUser() {
        return user;
    }

    public void setUser(APPUser user) {
        this.user = user;
    }



}
