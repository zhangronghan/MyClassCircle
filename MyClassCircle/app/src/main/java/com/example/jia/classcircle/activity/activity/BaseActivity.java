package com.example.jia.classcircle.activity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/12.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Toast mToast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        initViews();
        initListener();
        initData();
    }
    protected abstract int getLayoutRes();
    protected abstract void initViews();
    protected abstract void initListener();
    protected abstract void initData();

    public void showToast(String msg){
        if(mToast ==null) {
            mToast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
