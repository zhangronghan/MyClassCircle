package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jia.classcircle.R;

/**
 * Created by Administrator on 2017/10/19.
 */

public class VoteViewHolder extends RecyclerView.ViewHolder {
    public TextView mTvNum;
    public TextView mTvTitle;
    public CheckBox mCheckBox;

    public VoteViewHolder(View itemView) {
        super(itemView);

        mTvNum = (TextView) itemView.findViewById(R.id.tv_Num);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);


    }
}
