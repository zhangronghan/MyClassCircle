package com.example.jia.classcircle.activity.activity;

import android.app.Dialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.FriendCircle;
import com.example.jia.classcircle.activity.util.BaseFunction;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/11/3.
 */

public class PublishActivity extends BaseActivity {
    private Toolbar mToolbar;
    private Button mBtnPublish;
    private EditText mEdtContent;
    private Dialog dialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnPublish = (Button) findViewById(R.id.btn_publish);
        mEdtContent = (EditText) findViewById(R.id.edt_content);


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

        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=mEdtContent.getText().toString().trim();
                if( !TextUtils.isEmpty(content)){
                    dialog.show();
                    APPUser appUser= BmobUser.getCurrentUser(APPUser.class);
                    FriendCircle friendCircle=new FriendCircle();
                    friendCircle.setUsername(appUser.getUsername());
                    friendCircle.setContent(content);
                    friendCircle.setClassName(appUser.getClassName());

                    friendCircle.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                showToast("发布成功");
                                dialog.dismiss();
                                finish();
                            } else {
                                dialog.dismiss();
                                showToast("出现异常");
                                Log.e("AAA","发布出现异常: "+e.getMessage());
                            }
                        }
                    });
                } else{
                    showToast("内容不能为空");
                }
            }
        });



    }

    @Override
    protected void initData() {
        dialog= BaseFunction.showProgressDialog(PublishActivity.this,"发布中....");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
