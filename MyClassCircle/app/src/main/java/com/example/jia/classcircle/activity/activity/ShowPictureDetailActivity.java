package com.example.jia.classcircle.activity.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.PhotoAndComment;
import com.example.jia.classcircle.activity.bmobTable.PhotoImgUrl;
import com.example.jia.classcircle.activity.fragments.PictureSlideFragment;
import com.example.jia.classcircle.activity.util.MyPhotoViewPager;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ShowPictureDetailActivity extends AppCompatActivity {
    private MyPhotoViewPager viewpagerPicture;
    private TextView tv_indicator;
    private List<String> urlList=new ArrayList<>();
    private ArrayList<String> list=new ArrayList<>();
    private Toolbar toolbar_photo_detail_show;
   // private Button btn_delete_photo;
    private int selectPhoto;
    private LinearLayout photo_layout;
    private List<String> getInternetImgUrlID=new ArrayList<>();//用来存删除的ID
    private APPUser user=BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;
    private   PictureSlidePagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture_detial);
        initDataAndView();
        onClickEvent();




    }



    private void onClickEvent() {
        toolbar_photo_detail_show.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       /* if(user.getIndentity().equals("学生")){
            btn_delete_photo.setVisibility(View.INVISIBLE);
        }
        btn_delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getIndentity().equals("管理员")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(ShowPictureDetailActivity.this);
                    builder.setMessage("是否删除该照片");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wait_dialog.show();
                            PhotoAndComment photoAndComment=new PhotoAndComment();
                            photoAndComment.setObjectId(getInternetImgUrlID.get(selectPhoto));
                            photoAndComment.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        urlList.remove(selectPhoto);
                                        adapter.updateData(urlList);
                                        wait_dialog.hide();
                                        Toast.makeText(ShowPictureDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                       *//* startActivity(new Intent(ShowPictureDetailActivity.this,ShowPictureDetailActivity.class));
                                        finish();*//*
                                    }else {
                                        wait_dialog.hide();
                                    }
                                }
                            });

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

            }
        });*/
    }

    private void initDataAndView() {
        wait_dialog=new ProgressDialog(ShowPictureDetailActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("删除中...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        photo_layout= (LinearLayout) findViewById(R.id.photo_layout);
        photo_layout.setBackgroundColor(Color.parseColor("#333333"));
        toolbar_photo_detail_show= (Toolbar) findViewById(R.id.toolbar_photo_detail_show);
        setSupportActionBar(toolbar_photo_detail_show);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_photo_detail_show.setBackgroundColor(Color.parseColor("#534f4f"));
            photo_layout.setBackgroundColor(Color.parseColor("#423737"));
        }

        tv_indicator= (TextView) findViewById(R.id.tv_indicator);
        viewpagerPicture= (MyPhotoViewPager) findViewById(R.id.viewpagerPicture);
      //  btn_delete_photo= (Button) findViewById(R.id.btn_delete_photo);
        final PhotoImgUrl imgUrl= (PhotoImgUrl) getIntent().getSerializableExtra("url");
        urlList=imgUrl.getImgUrl();
        selectPhoto=imgUrl.getPosition();
        getInternetImgUrlID =imgUrl.getGetInternetImgUrlID();
        Log.e("url",""+urlList.size());

        String [] urls= new String [urlList.size()];
        for(int i=0;i<urlList.size();i++){
            urls[i]=urlList.get(i);
            Log.e("url",urls[i]);
        }
        Collections.addAll(list,urls);
        adapter= new PictureSlidePagerAdapter(getSupportFragmentManager(),urlList);
        viewpagerPicture.setAdapter(adapter);
        viewpagerPicture.setCurrentItem(imgUrl.getPosition());
        viewpagerPicture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                tv_indicator.setText("第"+String.valueOf(i+1)+"张"+"/共"+urlList.size()+"张");

            }

            @Override
            public void onPageSelected(int i) {
                selectPhoto=i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private  class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<String> urlList=new ArrayList<>();
        public PictureSlidePagerAdapter(FragmentManager fm,List<String> urlList) {
            super(fm);
            this.urlList=urlList;
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));
        }

        @Override
        public int getCount() {
            return urlList.size();//<span style="white-space: pre;">指定ViewPager的总页数</span>
        }
        public  void updateData(List<String> urlList){
            this.urlList=urlList;
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }

}
