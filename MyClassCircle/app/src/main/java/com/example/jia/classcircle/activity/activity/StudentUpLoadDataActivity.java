package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.ClassHomeWork;
import com.example.jia.classcircle.activity.util.MyPopupWindow;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.io.File;
import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class StudentUpLoadDataActivity extends AppCompatActivity {//学生上传资料
    private Toolbar toolbar_up_load_data;
    private TextView tv_stu_up_data_url;
    private Button img_up_load_data;
    private Button img_file_url_select;
    private String data_url = "";
    private APPUser appUser = BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;
    private TextView tvConfirm;
    private TextView tvCancel;
    private MyPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_up_load_data);
        initView();
        onClickEvent();
    }

    private void onClickEvent() {
        toolbar_up_load_data.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_up_load_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);

            }
        });

        img_file_url_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 2);
            }
        });


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    popupWindow.dismiss();
                    wait_dialog.show();
                    //判断路径是否为空
                    String data_url = tv_stu_up_data_url.getText().toString();
                    if (data_url.length() == 0) {
                        Toast.makeText(StudentUpLoadDataActivity.this, "未选择上传文件，请重新选择", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //先上传文件路径，上传成功后再把路径存入homework表
                    final BmobFile bmobFile = new BmobFile(new File(data_url));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ClassHomeWork classHomeWork = new ClassHomeWork();
                                classHomeWork.setClassName(appUser.getClassName());
                                classHomeWork.setUserName(appUser.getUsername());
                                classHomeWork.setHomeworlUrl(bmobFile.getUrl());
                                classHomeWork.setTime(getSystemTime());

                                classHomeWork.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            tv_stu_up_data_url.setText("");
                                            Toast.makeText(StudentUpLoadDataActivity.this, "文件上传成功", Toast.LENGTH_SHORT).show();
                                            wait_dialog.hide();
                                        } else {
                                            Toast.makeText(StudentUpLoadDataActivity.this, "文件保存失败，请重试一次", Toast.LENGTH_SHORT).show();
                                            wait_dialog.hide();
                                        }

                                    }
                                });

                            } else {
                                Toast.makeText(StudentUpLoadDataActivity.this, "文件上传失败，请重试一次", Toast.LENGTH_SHORT).show();
                                wait_dialog.hide();
                            }
                        }

                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                            wait_dialog.setMessage("文件上传进度：" + value + "%");
                        }
                    });


                    break;

                case R.id.tv_cancel:
                    popupWindow.dismiss();
                    break;

                default:
                    break;

            }


        }
    };


    public void showPopFormBottom(View view) {
        popupWindow = new MyPopupWindow(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        popupWindow.showAtLocation(findViewById(R.id.MyLinearLayout), Gravity.BOTTOM, 0, 0);

    }


    private void initView() {
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        wait_dialog = new ProgressDialog(StudentUpLoadDataActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("文件上传进度：" + "0%");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(true);// 设置ProgressDialog 是否可以按退回按键取消
        toolbar_up_load_data = (Toolbar) findViewById(R.id.toolbar_up_load_data);
        tv_stu_up_data_url = (TextView) findViewById(R.id.tv_stu_up_data_url);
        img_up_load_data = (Button) findViewById(R.id.img_up_load_data);
        img_file_url_select = (Button) findViewById(R.id.img_file_url_select);
        setSupportActionBar(toolbar_up_load_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_up_load_data.setBackgroundColor(Color.parseColor("#534f4f"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {//是否选择，没选择就不会继续
            if (data != null) {//防止万一什么都没选就返回
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);

                File file = new File(img_path);
                data_url = file.toString();
                tv_stu_up_data_url.setText(data_url);
            } else {
                tv_stu_up_data_url.setText("");
            }


        }

    }

    private String getSystemTime() {//获取系统时间，年月日时分
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份,从0开始计
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        String detailTime = String.valueOf(mYear) + "/" + String.valueOf(mMonth) + "/" + String.valueOf(mDay);
        return detailTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }



}
