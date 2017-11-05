package com.example.jia.classcircle.activity.bmobTable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/17.
 */

public class ActivityVoteGroup extends BmobObject implements Serializable{
    private String CreateName;       //创建活动的人
    private String activityTitle;     //活动名
    private int voteNum;             //投票总数

    public String getCreateName() {
        return CreateName;
    }

    public void setCreateName(String createName) {
        CreateName = createName;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
