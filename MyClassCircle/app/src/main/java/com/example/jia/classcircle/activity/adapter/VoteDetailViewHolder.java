package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.ImageViewPlus;


/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteDetailViewHolder extends RecyclerView.ViewHolder{
    public ImageViewPlus mIvImageheader;
    public TextView mTvTitle;
    public TextView mTvWriter;
    public ImageView mIvAgree;
    public ProgressBar mProgressBar;
    public TextView mTvAgreeNum;
    public TextView mTvPercent;


    public VoteDetailViewHolder(View itemView) {
        super(itemView);

        mIvImageheader = (ImageViewPlus) itemView.findViewById(R.id.iv_imageheader);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mTvWriter = (TextView) itemView.findViewById(R.id.tv_writer);
        mIvAgree = (ImageView) itemView.findViewById(R.id.iv_agree);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        mTvAgreeNum = (TextView) itemView.findViewById(R.id.tv_agreeNum);
        mTvPercent= (TextView) itemView.findViewById(R.id.tv_percent);

    }

}
