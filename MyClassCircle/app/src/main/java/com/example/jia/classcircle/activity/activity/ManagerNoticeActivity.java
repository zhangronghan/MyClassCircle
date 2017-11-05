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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeFile;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeMsg;
import com.example.jia.classcircle.activity.bmobTable.Notice;
import com.example.jia.classcircle.activity.util.MyPopupWindow;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.io.File;
import java.util.Calendar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class ManagerNoticeActivity extends AppCompatActivity {
    private Toolbar toolbar_notice;
    private EditText edt_notice_str;
    private TextView tv_data_url;
    private Button img_notice;
    private Button img_file_url;
    private String data_url = "";
    private String notice_content = "";
    private APPUser currentUser;
    private ProgressDialog wait_dialog;
    private MyPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //管理员发布通知
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_notice);
        initView();
        onClickEvent();
        getManagerUserMessage();

    }

    private void getManagerUserMessage() {
        currentUser = BmobUser.getCurrentUser(APPUser.class);
    }


    private void onClickEvent() {
        toolbar_notice.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回箭头一按，实现关闭本界面
            }
        });
        img_file_url.setOnClickListener(new View.OnClickListener() { //上传文件的路径
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });

        img_notice.setOnClickListener(new View.OnClickListener() { //实现通知发送
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);
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

                    BmobQuery<Notice> query = new BmobQuery<Notice>();//先把该班级查出来，再添加，最后更新
                    query.addWhereEqualTo("className", currentUser.getClassName());
                    if (currentUser.isIfJoinClass() == false) { //先判断是否已创建班级
                        Toast.makeText(ManagerNoticeActivity.this, "该班级不存在，请先创建班级", Toast.LENGTH_SHORT).show();
                        wait_dialog.hide();
                        return;
                    }
                    notice_content = edt_notice_str.getText().toString().trim();
                    if (TextUtils.isEmpty(notice_content) == true && data_url.length() == 0) {//如果没发文本消息也没传文件，则提醒
                        Toast.makeText(ManagerNoticeActivity.this, "文本无消息平且无选择上传的文件", Toast.LENGTH_SHORT).show();
                        wait_dialog.hide();
                        return;
                    }
                    if (TextUtils.isEmpty(notice_content) == false && data_url.length() == 0) { //有发文本，没选择上传的文件
                        //这里save到AllNoticeMsg
                        AllNoticeMsg allNoticeMsg = new AllNoticeMsg();
                        allNoticeMsg.setClassName(currentUser.getClassName());
                        allNoticeMsg.setManagerUser(currentUser);
                        allNoticeMsg.setNoticeContent(notice_content);
                        allNoticeMsg.setNoticeTime(getSystemTime());
                        allNoticeMsg.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    edt_notice_str.setText("");
                                    tv_data_url.setText("");
                                    Toast.makeText(ManagerNoticeActivity.this, "公告发布成功", Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                } else {
                                    Toast.makeText(ManagerNoticeActivity.this, "公告发布失败，请重新试一次", Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                }
                            }
                        });

                    }


                    if (TextUtils.isEmpty(notice_content) == true && data_url.length() > 0) { //没发文本消息但是有传文件
                        //先将文件上传成功后，再把路径存到AllNoticeFile里面去
                        final BmobFile bmobFile = new BmobFile(new File(data_url));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    AllNoticeFile noticeFile = new AllNoticeFile();
                                    noticeFile.setClassName(currentUser.getClassName());
                                    //    noticeFile.setManagerUser(currentUser);
                                    noticeFile.setNoticeFileUrl(bmobFile.getFileUrl());//返回的上传文件的完整地址
                                    noticeFile.setNoticeTime(getSystemTime());
                                    noticeFile.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                tv_data_url.setText("");
                                                Toast.makeText(ManagerNoticeActivity.this, "文件发布成功", Toast.LENGTH_SHORT).show();
                                                wait_dialog.hide();
                                            } else {
                                                Toast.makeText(ManagerNoticeActivity.this, "该班级无法接收到文件，请重新上传一次", Toast.LENGTH_SHORT).show();
                                                wait_dialog.hide();
                                            }
                                        }
                                    });

                                } else {
                                    wait_dialog.hide();
                                    Toast.makeText(ManagerNoticeActivity.this, "文件上传失败，请重试一次", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }


                    if (TextUtils.isEmpty(notice_content) == false && data_url.length() > 0) { //有发文本且上传了文件
                        wait_dialog.show();
                        AllNoticeMsg allNoticeMsg = new AllNoticeMsg();
                        allNoticeMsg.setClassName(currentUser.getClassName());
                        allNoticeMsg.setManagerUser(currentUser);
                        allNoticeMsg.setNoticeContent(notice_content);
                        allNoticeMsg.setNoticeTime(getSystemTime());
                        allNoticeMsg.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    edt_notice_str.setText("");

                                    Toast.makeText(ManagerNoticeActivity.this, "公告发布成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ManagerNoticeActivity.this, "公告发布失败，请重新试一次", Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                }
                            }
                        });

                        final BmobFile bmobFile = new BmobFile(new File(data_url));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    AllNoticeFile noticeFile = new AllNoticeFile();
                                    noticeFile.setClassName(currentUser.getClassName());
                                    // noticeFile.setManagerUser(currentUser);
                                    noticeFile.setNoticeFileUrl(bmobFile.getFileUrl());//返回的上传文件的完整地址
                                    noticeFile.setNoticeTime(getSystemTime());
                                    noticeFile.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                tv_data_url.setText("");
                                                Toast.makeText(ManagerNoticeActivity.this, "文件发布成功", Toast.LENGTH_SHORT).show();
                                                wait_dialog.hide();
                                            } else {
                                                Toast.makeText(ManagerNoticeActivity.this, "该班级无法接收到文件，请重新上传一次", Toast.LENGTH_SHORT).show();
                                                wait_dialog.hide();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(ManagerNoticeActivity.this, "文件上传失败，请重试一次", Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                }
                            }
                        });
//                        wait_dialog.hide();
                    }


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
        wait_dialog = new ProgressDialog(ManagerNoticeActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("发布中，请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(true);// 设置ProgressDialog 是否可以按退回按键取消

        currentUser = APPUser.getCurrentUser(APPUser.class);
        toolbar_notice = (Toolbar) findViewById(R.id.toolbar_notice);
        edt_notice_str = (EditText) findViewById(R.id.edt_notice_str);
        tv_data_url = (TextView) findViewById(R.id.tv_data_url);
        img_notice = (Button) findViewById(R.id.img_notice);
        img_file_url = (Button) findViewById(R.id.img_file_url);
        setSupportActionBar(toolbar_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_notice.setBackgroundColor(Color.parseColor("#534f4f"));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {//是否选择，没选择就不会继续
            if (data != null) {//防止万一什么都没选就返回
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);

                File file = new File(img_path);
                data_url = file.toString();
                tv_data_url.setText(data_url);
            } else {
                tv_data_url.setText("");
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


}
