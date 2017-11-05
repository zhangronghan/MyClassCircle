package com.example.jia.classcircle.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

public class WelcomeActivity extends AppCompatActivity {
    //用于判断是否首次安装，如果是首次则进入引导界面，否则进入登录界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        judgeIsFirst();
    }

    private void judgeIsFirst() {
        //第一次运行，跳转引导页
        if(SharePreferenceUtil.getIsFirstRun(WelcomeActivity.this)){
            startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
            SharePreferenceUtil.setFirstRun(WelcomeActivity.this,false);

        }else {
            new Handler().postDelayed(new Runnable() {//2秒后跳转主界面
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                }
            },2000);
        }
    }
}
