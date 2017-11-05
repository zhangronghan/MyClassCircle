package com.example.jia.classcircle.activity.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.BaseFunction;
import com.example.jia.classcircle.activity.util.MyToggleButton;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

import static cn.bmob.newim.core.BmobIMClient.getContext;

/**
 * Created by Administrator on 2017/10/27.
 */

public class SettingActivity extends BaseActivity{
    private LinearLayout mLinearLayout;
    private Toolbar mToolbar;
    private MyToggleButton mToggleButton;
    private CardView mQuitNumber;
    private CardView mUpdateApp;
    private TextView mTvUpdateState;
    private Dialog dialog;
    private Handler mHandler;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToggleButton = (MyToggleButton) findViewById(R.id.toggleButton);
        mQuitNumber = (CardView) findViewById(R.id.cv_QuitNumber);
        mUpdateApp= (CardView) findViewById(R.id.rl_update);
        mTvUpdateState= (TextView) findViewById(R.id.tv_updateState);
        mLinearLayout= (LinearLayout) findViewById(R.id.linearLayout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListener() {
        mQuitNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("确认退出登录吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut();
                        BmobIM.getInstance().disConnect();

                        EMClient.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.e("AAA","环信退出成功");
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.e("AAA","环信退出失败   "+s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();

            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.putExtra("currentIndex", 2);
                startActivity(intent);
                finish();
            }
        });

        mUpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog= BaseFunction.showProgressDialog(SettingActivity.this,"查询中....");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            Message message=new Message();
                            message.what=1;
                            mHandler.sendMessage(message);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });



    }

    @Override
    protected void initData() {
        //根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            mToolbar.setBackgroundColor(Color.parseColor("#534f4f"));
            mLinearLayout.setBackgroundColor(Color.parseColor("#423737"));
        }



        initToggleButton();

        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==1){
                    dialog.hide();
                    mTvUpdateState.setText("已经是最新版本");
                }
                return true;
            }
        });


    }

    private void initToggleButton() {
        boolean isNight = SharePreferenceUtil.getNightMode(getContext());
        mToggleButton.setToggleOn(isNight);

        mToggleButton.setOnToggleChanged(new MyToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                SharePreferenceUtil.setNightMode(getContext(), on);

                getDelegate().setDefaultNightMode(on? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animo_alph_start, R.anim.animo_alph_close);
                finish();



            }
        });


    }



}
