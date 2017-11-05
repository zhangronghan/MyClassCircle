package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.util.ImgUtil;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AlertUserMsgActivity extends AppCompatActivity {//修改用户界面
    private Toolbar toolbar_alert_return;
    private ImageView alertUserImageView;
    private EditText alert_user_name;
    private EditText alert_user_password;
    private EditText alert_user_phone;
    private Button btn_alert_user;
    private APPUser currentUser;
    private Bitmap head_bitmap;
    private byte[]img={};
    private static final int CHOOSE_PHOTO=1001;
    private ProgressDialog wait_dialog;//自动旋转对话框，用于建班提示等待
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_user_msg);

        initView();

    }


    private void initView() {

        wait_dialog=new ProgressDialog(AlertUserMsgActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消


        toolbar_alert_return= (Toolbar) findViewById(R.id. toolbar_alert_return);
        setSupportActionBar(toolbar_alert_return);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
        currentUser= BmobUser.getCurrentUser(APPUser.class);
        img=currentUser.getImg_msg();
        head_bitmap= getBitmapFromByte(img);

        //根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_alert_return.setBackgroundColor(Color.parseColor("#534f4f"));
        }


        alertUserImageView= (ImageView) findViewById(R.id.alertUserImageView);
        alert_user_name= (EditText) findViewById(R.id.alert_user_name);
        alert_user_password= (EditText) findViewById(R.id.alert_user_password);
        alert_user_phone= (EditText) findViewById(R.id.alert_user_phone);
        btn_alert_user= (Button) findViewById(R.id.btn_alert_user);

        alertUserImageView.setImageBitmap(head_bitmap);
        alert_user_name.setText(currentUser.getUsername());
        alert_user_phone.setText(currentUser.getPhoneNumber());
        alert_user_password.setText("");
        alertUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传入图片，系统选择相册
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);


            }
        });

        btn_alert_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(AlertUserMsgActivity.this);
                builder.setMessage("是否修改？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_dialog.show();
                        //这里实现确认修改信息,一旦修改，若其已经加入班级，则连班级中的信息也要改
                        String alertUserName=alert_user_name.getText().toString().trim();
                        String alertUserPhoneNum=alert_user_phone.getText().toString().trim();
                        String alertPassWord=alert_user_password.getText().toString().trim();

                        if(alertUserName.length()==0){
                            Toast.makeText(AlertUserMsgActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        } if(alertUserPhoneNum.length()==0){//正则验证
                            Toast.makeText(AlertUserMsgActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        } if(alertUserPhoneNum.length()>0) {
                            if( isMobile(alertUserPhoneNum)==false){
                                Toast.makeText(AlertUserMsgActivity.this,"该号码不合法,请重新输入",Toast.LENGTH_SHORT).show();
                                wait_dialog.hide();
                                return;
                            }
                        }  if(alertPassWord.length()==0){
                            Toast.makeText(AlertUserMsgActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        }
                        final APPUser newUser=new APPUser();//时间过长，需要旋转框提示等待
                        final BmobUser bmobUser = BmobUser.getCurrentUser(APPUser.class);
                        newUser.setUsername(alertUserName);
                        newUser.setPassword(alertPassWord);

                        newUser.setIfJoinClass(currentUser.isIfJoinClass());
                        newUser.setImg_msg(Bitmap2Bytes(head_bitmap));
                        newUser.setPhoneNumber(alertUserPhoneNum);
                        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    //成功后，顺便看看该用户是否加入过班级，如果有，则顺便更新班级中该学生的信息
                                    if(currentUser.isIfJoinClass()==true){
                                        BmobQuery<AllTheClass>query=new BmobQuery<AllTheClass>();
                                        query.addWhereEqualTo("className",currentUser.getClassName());
                                        query.findObjects(new FindListener<AllTheClass>() {
                                            @Override
                                            public void done(List<AllTheClass> list, BmobException e) {
                                                if(e==null){
                                                    List<APPUser> stuList=list.get(0).getStu();
                                                    for (int i=0;i<stuList.size();i++){
                                                        if(stuList.get(i).getObjectId().equals(currentUser.getObjectId())){
                                                            list.get(0).getStu().remove(i);
                                                            list.get(0).getStu().add(newUser);//更改后，还需再上传
                                                            AllTheClass alertClass=new AllTheClass();
                                                            alertClass.setStu(list.get(0).getStu());
                                                            alertClass.update(list.get(0).getObjectId(), new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if(e==null){
                                                                        Toast.makeText(AlertUserMsgActivity.this,"所有信息更新成功",Toast.LENGTH_SHORT).show();
                                                                        wait_dialog.hide();
                                                                        finish();
                                                                    }else {
                                                                        Toast.makeText(AlertUserMsgActivity.this,"所有信息更新失败",Toast.LENGTH_SHORT).show();
                                                                        wait_dialog.hide();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }else {
                                                    Toast.makeText(AlertUserMsgActivity.this,"信息更新成功",Toast.LENGTH_SHORT).show();
                                                    wait_dialog.hide();
                                                    finish();
                                                }

                                            }
                                        });

                                    }else {
                                        //未加入班级，但是修改成功
                                        Toast.makeText(AlertUserMsgActivity.this,"信息更新成功",Toast.LENGTH_SHORT).show();
                                        wait_dialog.hide();
                                    }
                                }else {
                                    Toast.makeText(AlertUserMsgActivity.this,"更新失败，请重试一次",Toast.LENGTH_SHORT).show();
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
        });

        toolbar_alert_return.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public Bitmap getBitmapFromByte(byte[] temp){
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                        head_bitmap=bitmap;
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        head_bitmap=bitmap;
                    }
                    head_bitmap=bitmap;
                    alertUserImageView.setImageBitmap(head_bitmap);
                }
                break;
            default:
                break;
        }
    }
    public static boolean isMobile(String number) {//用于判断手机合法性
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }

    }
    private byte[] Bitmap2Bytes(Bitmap bm){//将图片转化为2进制数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }
}
