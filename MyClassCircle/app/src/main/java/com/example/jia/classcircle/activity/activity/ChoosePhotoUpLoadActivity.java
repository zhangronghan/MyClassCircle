package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.PhotoAndComment;
import com.example.jia.classcircle.activity.util.ImgUtil;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.io.File;
import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChoosePhotoUpLoadActivity extends AppCompatActivity {
    private Toolbar toolbar_choosePhotoUpLoad;
    private ImageView img_choosePhotoUpLoad;
    private Button btn_select_photo_up_load;
    private Button btn_select_photo_from_system;
    private String get_the_system_photo_url="";
    private static final int CHOOSE_PHOTO=1110;
    private Bitmap imgMap=null;
    private ProgressDialog wait_dialog;
    private int upload_progress=0;
    private String PhotoAndCommentID="";
    APPUser appUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_up_load);
        initData();
        initView();
        onClickEvent();

    }

    private void initData() {
         appUser= BmobUser.getCurrentUser(APPUser.class);


    }

    private void onClickEvent() {
        toolbar_choosePhotoUpLoad.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_select_photo_from_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
        btn_select_photo_up_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ChoosePhotoUpLoadActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否上传该相片");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_dialog.show();
                        if(get_the_system_photo_url.length()==0){
                            wait_dialog.hide();
                            Toast.makeText(ChoosePhotoUpLoadActivity.this,"未选择任何相册",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final BmobFile bmobFile=new BmobFile(new File(get_the_system_photo_url));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){//文件上传成功后，再把路径存储到相册评论表中

                                    PhotoAndComment photoAndComment=new PhotoAndComment();
                                    photoAndComment.setClassName(appUser.getClassName());
                                    photoAndComment.setCommentTime(getSystemTime());
                                    photoAndComment.setPhoto(bmobFile.getFileUrl().toString());
                                    photoAndComment.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                                wait_dialog.hide();
                                                Toast.makeText(ChoosePhotoUpLoadActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }else {
                                    Toast.makeText(ChoosePhotoUpLoadActivity.this,"上传失败，请重试一次",Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                }
                            }

                            @Override
                            public void onProgress(Integer value) {
                                super.onProgress(value);//获取上传进度的百分比
                                upload_progress=value;
                                wait_dialog.setMessage("上传进度： "+upload_progress+"%");
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
        });
    }

    private void initView() {
        toolbar_choosePhotoUpLoad= (Toolbar) findViewById(R.id.toolbar_choosePhotoUpLoad);
        img_choosePhotoUpLoad= (ImageView) findViewById(R.id.img_choosePhotoUpLoad);
        btn_select_photo_up_load= (Button) findViewById(R.id.btn_select_photo_up_load);
        btn_select_photo_from_system= (Button) findViewById(R.id.btn_select_photo_from_system);
        setSupportActionBar(toolbar_choosePhotoUpLoad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            toolbar_choosePhotoUpLoad.setBackgroundColor(Color.parseColor("#534f4f"));
        }


        wait_dialog=new ProgressDialog(ChoosePhotoUpLoadActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("上传进度： "+upload_progress+"%");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                        Uri originalUri3=data.getData();//获取图片uri
                        String []imgs13={MediaStore.Images.Media.DATA};//将图片URI转换成存储
                        Cursor cursor3=this.managedQuery(originalUri3, imgs13, null, null, null);
                        int index3=cursor3.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor3.moveToFirst();
                        get_the_system_photo_url=cursor3.getString(index3);
                        imgMap=bitmap;

                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        Uri originalUri3=data.getData();//获取图片uri
                        String []imgs13={MediaStore.Images.Media.DATA};//将图片URI转换成存储
                        Cursor cursor3=this.managedQuery(originalUri3, imgs13, null, null, null);
                        int index3=cursor3.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor3.moveToFirst();
                        get_the_system_photo_url=cursor3.getString(index3);
                        imgMap=bitmap;
                    }
                    imgMap=bitmap;
                    if(imgMap==null){
                      //  Toast.makeText(ChoosePhotoUpLoadActivity.this,"未选择任何相片",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        img_choosePhotoUpLoad.setImageBitmap(imgMap);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }
    private String getSystemTime(){//获取系统时间，年月日时分
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth= c.get(Calendar.MONTH) + 1;// 获取当前月份,从0开始计
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        String detailTime=String.valueOf(mYear)+"/"+String.valueOf(mMonth)+"/"+String.valueOf(mDay);
        return detailTime;
    }
}
