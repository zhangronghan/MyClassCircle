package com.example.jia.classcircle.activity.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.ActivityVote;
import com.example.jia.classcircle.activity.bmobTable.ActivityVoteGroup;
import com.example.jia.classcircle.activity.bmobTable.GetImagePath;
import com.example.jia.classcircle.activity.bmobTable.MyIntentData;
import com.example.jia.classcircle.activity.util.BaseFunction;
import com.example.jia.classcircle.activity.util.ImageViewPlus;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteCreateActivity extends BaseActivity {
    private static final int GET_IMAGE = 1001;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvFinishVote;
    private ImageViewPlus mIvVoteImage;
    private EditText mEdtJoinActivityName;
    private EditText mEdtJoinActivityTitle;
    private EditText mEdtContent;
    private Button mBtnNext;
    private String imageUrl;
    private String title;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote_create;
    }

    @Override
    protected void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvFinishVote = (TextView) findViewById(R.id.tv_finish_vote);
        mIvVoteImage = (ImageViewPlus) findViewById(R.id.iv_voteImage);
        mEdtJoinActivityName = (EditText) findViewById(R.id.edt_joinActivityName);
        mEdtJoinActivityTitle = (EditText) findViewById(R.id.edt_joinActivityTitle);
        mEdtContent = (EditText) findViewById(R.id.edt_content);
        mBtnNext = (Button) findViewById(R.id.btn_next);

    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvVoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_IMAGE);
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(VoteCreateActivity.this, "执行中....");
                dialog.show();

                final String name = mEdtJoinActivityName.getText().toString();
                final String joinTitle = mEdtJoinActivityTitle.getText().toString();
                final String content = mEdtContent.getText().toString();
                if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(joinTitle) && !TextUtils.isEmpty(content)) {
                    final BmobFile bmobFile = new BmobFile(new File(imageUrl));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ActivityVote activityVote = new ActivityVote();
                                activityVote.setJoinActivityName(name);
                                activityVote.setActivityTitle(title);
                                activityVote.setJoinActivityTitle(joinTitle);
                                activityVote.setContent(content);
                                activityVote.setImageHeader(bmobFile.getFileUrl());

                                activityVote.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            showToast("保存成功,请添加下一个");
                                            dialog.dismiss();

                                            Intent intent = new Intent(VoteCreateActivity.this, VoteCreateActivity.class);
                                            intent.putExtra(MyIntentData.VOTE_TITLE, title);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showToast("保存失败");
                                            dialog.dismiss();
                                        }
                                    }
                                });

                            } else {
                                Log.e("AAA", "BmobFile:" + e.getMessage());
                            }


                        }
                    });

                } else {
                    showToast("请填写完整");
                    dialog.dismiss();
                }

            }
        });

        mTvFinishVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(VoteCreateActivity.this, "执行中....");
                dialog.show();

                final String name = mEdtJoinActivityName.getText().toString();
                final String joinTitle = mEdtJoinActivityTitle.getText().toString();
                final String content = mEdtContent.getText().toString();
                if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(joinTitle) && !TextUtils.isEmpty(content)) {
                    final BmobFile bmobFile = new BmobFile(new File(imageUrl));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ActivityVote activityVote = new ActivityVote();
                                activityVote.setJoinActivityName(name);
                                activityVote.setActivityTitle(title);
                                activityVote.setJoinActivityTitle(joinTitle);
                                activityVote.setContent(content);
                                activityVote.setImageHeader(bmobFile.getFileUrl());

                                activityVote.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            execureCreateOperate(dialog);
                                            finish();
                                        } else {
                                            showToast("创建失败");
                                            Log.e("AAA", "SAVE" + e.getMessage());
                                            dialog.dismiss();
                                        }
                                    }
                                });

                            } else {
                                Log.e("AAA", "上传图片：" + e.getMessage());
                            }

                        }
                    });

                } else {
                    showToast("请填写完整");
                    dialog.dismiss();
                }

            }
        });


    }


    @Override
    protected void initData() {
        title = getIntent().getStringExtra(MyIntentData.VOTE_TITLE);
        mTvTitle.setText("活动："+title);


    }

    /**
     * 执行创建操作
     */
    private void execureCreateOperate(final Dialog dialog) {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        ActivityVoteGroup activityVoteGroup = new ActivityVoteGroup();
        activityVoteGroup.setCreateName(bmobUser.getUsername());
        activityVoteGroup.setActivityTitle(title);
        activityVoteGroup.setVoteNum(0);

        activityVoteGroup.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    showToast("创建成功");
                    dialog.dismiss();
                } else {
                    showToast("创建失败");
                    dialog.dismiss();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    mIvVoteImage.setImageBitmap(bitmap);
                    imageUrl = GetImagePath.getPath(VoteCreateActivity.this, data.getData());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
