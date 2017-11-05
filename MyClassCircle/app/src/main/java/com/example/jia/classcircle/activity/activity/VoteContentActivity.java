package com.example.jia.classcircle.activity.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.ActivityVote;
import com.example.jia.classcircle.activity.bmobTable.MyIntentData;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;


/**
 * Created by Administrator on 2017/10/18.
 */

public class VoteContentActivity extends BaseActivity{
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView mTvContent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote_content;
    }

    @Override
    protected void initViews() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        tvTitle= (TextView) findViewById(R.id.tv_title);
        mTvContent= (TextView) findViewById(R.id.tv_content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            mToolbar.setBackgroundColor(Color.parseColor("#534f4f"));
        }
        ActivityVote activityVote= (ActivityVote) getIntent().getSerializableExtra(MyIntentData.VOTE_CONTENT);

        tvTitle.setText(activityVote.getJoinActivityTitle());
        mTvContent.setText(activityVote.getContent());

    }
}
