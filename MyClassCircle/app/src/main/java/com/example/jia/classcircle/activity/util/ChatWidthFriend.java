package com.example.jia.classcircle.activity.util;

import java.io.Serializable;

/**
 * Created by jia on 2017/10/25.
 */

public class ChatWidthFriend  implements Serializable {
    private String name;
    private byte[] imgByte;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }


}
