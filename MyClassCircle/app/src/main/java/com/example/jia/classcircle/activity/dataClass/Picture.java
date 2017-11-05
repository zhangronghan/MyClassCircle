package com.example.jia.classcircle.activity.dataClass;

/**
 * Created by jia on 2017/9/19.
 */

public class Picture {//用于存九宫格图和名
    private String title;
    private int imageId;
    public  Picture(String title,int imageId){
        this.title=title;
        this.imageId=imageId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


}
