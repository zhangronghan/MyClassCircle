package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jia.classcircle.R;


/**
 * Created by Administrator on 2017/10/16.
 */

public class MyAppraiseViewHolder extends RecyclerView.ViewHolder{
    public TextView tvFirstWord;
    public TextView tvTitle;
    public CheckBox mCheckBox;

    public MyAppraiseViewHolder(View itemView) {
        super(itemView);
        tvFirstWord= (TextView) itemView.findViewById(R.id.tv_firstWord);
        tvTitle= (TextView) itemView.findViewById(R.id.tv_title);
        mCheckBox= (CheckBox) itemView.findViewById(R.id.checkbox);
    }

}
