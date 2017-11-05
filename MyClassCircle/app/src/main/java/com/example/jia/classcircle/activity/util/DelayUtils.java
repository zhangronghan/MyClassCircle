package com.example.jia.classcircle.activity.util;

/**
 * Created by Administrator on 2017/10/23.
 */

public class DelayUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }

}
