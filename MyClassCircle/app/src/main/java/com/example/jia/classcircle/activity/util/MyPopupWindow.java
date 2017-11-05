package com.example.jia.classcircle.activity.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jia.classcircle.R;

/**
 * Created by Administrator on 2017/10/28.
 */

public class MyPopupWindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tvConfirm;
    private TextView tvCancel;

    public MyPopupWindow(Context mContext,View.OnClickListener itemsClick) {
        super(mContext);
        this.view= LayoutInflater.from(mContext).inflate(R.layout.popupwindow_item,null);

        tvConfirm= (TextView) view.findViewById(R.id.tv_confirm);
        tvCancel= (TextView) view.findViewById(R.id.tv_cancel);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 设置按钮监听
        tvConfirm.setOnClickListener(itemsClick);
        tvCancel.setOnClickListener(itemsClick);

        //设置外部可点击
        this.setOutsideTouchable(true);

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height=view.findViewById(R.id.linearLayout).getTop();

                int y= (int) event.getY();
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(y < height){
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setContentView(this.view);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        //设置弹出窗口可点击
        this.setFocusable(true);
        ColorDrawable dw=new ColorDrawable(0x000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popupwindow_anim);
    }


}

