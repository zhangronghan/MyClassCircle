package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.ImageViewPlus;


/**
 * Created by Administrator on 2017/10/16.
 */

public class MyAppraiseDetailViewHolder extends RecyclerView.ViewHolder{
    public ImageViewPlus mIvHeader;
    public TextView mTvName;
    public TextView mTvIntroduce;
    public ImageView mIvAgree;
    public ProgressBar mProgressBar;
    public TextView mTvAgreeNum;
    public TextView tv_totalPercent;

    public MyAppraiseDetailViewHolder(View itemView) {
        super(itemView);

        mIvHeader = (ImageViewPlus) itemView.findViewById(R.id.iv_header);
        mTvName = (TextView) itemView.findViewById(R.id.tv_name);
        mTvIntroduce = (TextView) itemView.findViewById(R.id.tv_introduce);
        mIvAgree = (ImageView) itemView.findViewById(R.id.iv_agree);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        mTvAgreeNum = (TextView) itemView.findViewById(R.id.tv_agreeNum);
        tv_totalPercent= (TextView) itemView.findViewById(R.id.tv_totalPercent);


    }



}
