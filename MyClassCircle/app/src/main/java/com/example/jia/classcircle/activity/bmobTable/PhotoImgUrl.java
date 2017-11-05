package com.example.jia.classcircle.activity.bmobTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jia on 2017/11/3.
 */

public class PhotoImgUrl implements Serializable {
    private List<String> imgUrl;
    private List<String> getInternetImgUrlID;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getGetInternetImgUrlID() {
        return getInternetImgUrlID;
    }

    public void setGetInternetImgUrlID(List<String> getInternetImgUrlID) {
        this.getInternetImgUrlID = getInternetImgUrlID;
    }
}
