package com.example.jia.classcircle.activity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

/**
 * Created by Administrator on 2017/10/22.
 */

public class ActivityDetailActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mTvAppraise;
    private TextView mTvActivityVote;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_detail_activity;
    }

    @Override
    protected void initViews() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        mTvAppraise= (TextView) findViewById(R.id.tv_appraise);
        mTvActivityVote= (TextView) findViewById(R.id.tv_activity_vote);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityDetailActivity.this,AppraiseActivity.class));
            }
        });

        mTvActivityVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityDetailActivity.this,VoteActivity.class));
            }
        });


    }

    @Override
    protected void initData() {
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        boolean b=SharePreferenceUtil.getNightMode(this);
        if (b){
            mToolbar.setBackgroundColor(Color.parseColor("#534f4f"));
        }
        Log.e("AAA","SharePreferenceUtil:  "+b);

    }



}
