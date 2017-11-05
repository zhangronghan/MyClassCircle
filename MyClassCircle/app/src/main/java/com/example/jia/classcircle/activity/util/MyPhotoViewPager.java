package com.example.jia.classcircle.activity.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jia on 2017/11/4.
 */

public class MyPhotoViewPager extends ViewPager{//解决缩小出现崩溃的问题
    public MyPhotoViewPager(Context context) {
        super(context);
    }
    public MyPhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
