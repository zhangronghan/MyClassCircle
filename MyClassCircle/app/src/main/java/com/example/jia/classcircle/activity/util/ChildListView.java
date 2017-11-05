package com.example.jia.classcircle.activity.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/11/3.
 */

public class ChildListView extends ListView {
    public ChildListView(Context context) {
        super(context);
    }

    public ChildListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
