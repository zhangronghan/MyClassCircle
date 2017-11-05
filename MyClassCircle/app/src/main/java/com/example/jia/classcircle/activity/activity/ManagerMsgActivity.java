package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.Attendance;
import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.bmobTable.FilePathAndTime;
import com.example.jia.classcircle.activity.bmobTable.GetClientsTalk;
import com.example.jia.classcircle.activity.bmobTable.Notice;
import com.example.jia.classcircle.activity.bmobTable.PhotoAndComment;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;
import com.example.jia.classcircle.activity.util.ImgUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ManagerMsgActivity extends AppCompatActivity {//如果身份是管理者，则跳转到此页面
    private ImageView ivBack;
    private TextView tv_showUserName;
    private ImageView headImageView;
    private TextView tv_chooseImg;
    private Spinner sp_getYear;
    private Spinner sp_getProfession;
    private Spinner sp_getClassNum;
    private TextView tv_register_date;
    private Button btn_commit_msg;
    private String getYear = "", profession = "", classNum = "";//三个下拉框就为实现：2015，软件，2班
    private String RegisterName = "", RegisterPassWord = "", RegisterIdentity = "", RegisterPhoneNo = "";
    private Bitmap imgMap;
    private static final int CHOOSE_PHOTO = 0;
    private ProgressDialog wait_to_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mamager_msg);
        GetLastInterfaceData();//获取上个界面的值
        initViews();
        initSpinner();
        clickEvent();

    }

    private void GetLastInterfaceData() {
        Intent intent = getIntent();
        RegisterName = intent.getStringExtra("getRegisterName");
        RegisterPassWord = intent.getStringExtra("getRegisterPassWord");
        RegisterIdentity = intent.getStringExtra("getIdentity");
        RegisterPhoneNo = intent.getStringExtra("getRegisterPhoneNo");
    }

    private void clickEvent() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); //获取当前年份
        int mMonth = c.get(Calendar.MONTH);//获取当前月份,此方法月份从零开始，所以要+1 ;
        int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
        tv_register_date.setText(String.valueOf(mYear) + "   年   " + (mMonth + 1) + "  月    " + mDay + "    号");
        tv_showUserName.setText("尊敬的" + RegisterName + ",请继续完善您的信息");
        tv_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_commit_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerMsgActivity.this);
                builder.setMessage("是否注册");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String classMsg = getYear + profession + classNum;

                        final APPUser user = new APPUser();

                        byte[] img = Bitmap2Bytes(imgMap);
                        wait_to_register.show();//显示旋转
                        user.setUsername(RegisterName);
                        user.setPassword(RegisterPassWord);
                        user.setPhoneNumber(RegisterPhoneNo);
                        user.setClassName(classMsg);
                        user.setIndentity(RegisterIdentity);
                        user.setImg_msg(img);//传的是二进制图片,不合理（下次改为上传BmobFile）

                        user.signUp(new SaveListener<APPUser>() {

                            @Override
                            public void done(APPUser appUser, BmobException e) {
                                if (e == null) {


                                    //注册时，也把管理员的通知表添加上去,也把相册和评论表添加上去
                                    if (appUser.getIndentity().equals("管理员")) {
                                        Notice notice = new Notice();
                                        notice.setClassName(appUser.getClassName());
                                        notice.setUser(appUser);
                                        notice.setNoticeContentList(new ArrayList<ContentAndTime>());
                                        notice.setFileList(new ArrayList<FilePathAndTime>());
                                        notice.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(ManagerMsgActivity.this, "通知表创建成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ManagerMsgActivity.this, "通知表创建失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        PhotoAndComment photoAndComment = new PhotoAndComment();
                                        photoAndComment.setClassName(appUser.getClassName());
                                        photoAndComment.setClientName(appUser.getUsername());
                                        photoAndComment.setCommentContent("");
                                        photoAndComment.setCommentTime("");
                                        photoAndComment.setPhoto("");
                                        photoAndComment.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(ManagerMsgActivity.this, "相册评论表创建成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ManagerMsgActivity.this, "相册评论表失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        GetClientsTalk getClientsSpeakContent = new GetClientsTalk();
                                        getClientsSpeakContent.setClassName(appUser.getClassName());
                                        getClientsSpeakContent.setUserName(appUser.getUsername());
                                        getClientsSpeakContent.setSpeakContent("欢迎大家来欣赏班级相册");
                                        getClientsSpeakContent.setSpeakTime(getSystemTime());
                                        getClientsSpeakContent.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(ManagerMsgActivity.this, "班级相册交谈表建立成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ManagerMsgActivity.this, "班级相册交谈表建立失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Attendance attendance = new Attendance();
                                        attendance.setClassName(user.getClassName());
                                        attendance.setUserName(user.getUsername());
                                        attendance.setIdentity(user.getIndentity());


                                        List<ContentAndTime> contentAndTimeList = new ArrayList<ContentAndTime>();
                                        List<StuAboutCheck> stuAboutCheckList = new ArrayList<StuAboutCheck>();
                                        // StuAboutCheck stuAboutCheck=new StuAboutCheck();
                                        //  ContentAndTime contentAndTime=new ContentAndTime();
                                        //  contentAndTimeList.add(contentAndTime);
                                        //   stuAboutCheckList.add(stuAboutCheck);
                                        attendance.setContentList(contentAndTimeList);
                                        attendance.setStuAboutCheckList(stuAboutCheckList);
                                        attendance.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(ManagerMsgActivity.this, "考勤表建立成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ManagerMsgActivity.this, "考勤表建立失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                        //环信账号密码注册,注册用户名会自动转为小写字母，所以建议用户名均以小写注册
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    EMClient.getInstance().createAccount(RegisterName, RegisterPassWord);//同步方法
                                                    Log.e("环信", "注册成功");
                                                } catch (HyphenateException e1) {
                                                    e1.printStackTrace();
                                                    Log.e("环信", "注册失败" + e1.getErrorCode() + e1.getMessage());
                                                }
                                            }
                                        }).start();


                                        Toast.makeText(ManagerMsgActivity.this, "管理员注册成功,将回到登录界面", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ManagerMsgActivity.this, LoginActivity.class));
                                        finish();
                                        wait_to_register.hide();

                                    } else {
                                        Toast.makeText(ManagerMsgActivity.this, "管理员表创建失败，但注册成功,将回到登录界面", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ManagerMsgActivity.this, LoginActivity.class));
                                        finish();
                                    }


                                } else {
                                    wait_to_register.hide();
                                    Toast.makeText(ManagerMsgActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
    }


    private String getSystemTime() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份,从0开始计
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        String detailTime = String.valueOf(mYear) + "/" + String.valueOf(mMonth) + "/" + String.valueOf(mDay) + "    " + String.valueOf(mHour) + ":" + String.valueOf(mMinute);
        return detailTime;
    }

    private void initSpinner() { //实现3个下拉框的值
        final List<String> year_list = new ArrayList<>();//获取年
        final List<String> profession_list = new ArrayList<>();//专业
        final List<String> classNum_list = new ArrayList<>();//班级
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); //获取当前年份,为了让下拉框年自动更新年份，获得今年，上年，和上上年
        for (int i = 0; i < 3; i++) {
            String getThreeYears = String.valueOf(mYear);
            year_list.add(getThreeYears);
            mYear--;
        }
        ArrayAdapter<String> arr_year_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year_list);
        arr_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getYear.setAdapter(arr_year_adapter);
        sp_getYear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getYear = year_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //获取专业
        profession_list.add("软件");
        profession_list.add("网络");
        profession_list.add("图形图像");
        profession_list.add("测量");
        profession_list.add("智能交通");
        profession_list.add("公路测量");
        profession_list.add("轨道");
        ArrayAdapter<String> arr_profession_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, profession_list);
        arr_profession_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getProfession.setAdapter(arr_profession_adapter);
        sp_getProfession.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profession = profession_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 1; i < 10; i++) {
            classNum_list.add(String.valueOf(i));
        }
        ArrayAdapter<String> arr_classNum_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classNum_list);
        arr_classNum_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getClassNum.setAdapter(arr_classNum_adapter);
        sp_getClassNum.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classNum = classNum_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tv_showUserName = (TextView) findViewById(R.id.tv_showUserName);
        headImageView = (ImageView) findViewById(R.id.headImageView);
        tv_chooseImg = (TextView) findViewById(R.id.tv_chooseImg);
        sp_getYear = (Spinner) findViewById(R.id.sp_getYear);
        sp_getProfession = (Spinner) findViewById(R.id.sp_getProfession);
        sp_getClassNum = (Spinner) findViewById(R.id.sp_getClassNum);
        tv_register_date = (TextView) findViewById(R.id.tv_register_date);
        btn_commit_msg = (Button) findViewById(R.id.btn_commit_msg);
        headImageView.setImageResource(R.drawable.default_head_image);
        imgMap = convertViewToBitmap(headImageView);//如果不选择相片，则默认为这张图片);
        wait_to_register = new ProgressDialog(ManagerMsgActivity.this);
        wait_to_register.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait_to_register.setMessage("注册中，请稍等...");
        wait_to_register.setIndeterminate(false);
        wait_to_register.setCancelable(false);

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
                        imgMap = bitmap;//用来传图片


                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        imgMap = bitmap;

                        Uri originalUri3 = data.getData();//获取图片uri
                        String[] imgs13 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储
                        Cursor cursor3 = this.managedQuery(originalUri3, imgs13, null, null, null);
                        int index3 = cursor3.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor3.moveToFirst();
                        String img_url3 = cursor3.getString(index3);


                        // Toast.makeText(ManagerMsgActivity.this,"图片路径为2"+img_url3,Toast.LENGTH_SHORT).show();
                    }
                    headImageView.setImageBitmap(bitmap);
                }

                break;
            default:
                break;
        }
    }

    public Bitmap convertViewToBitmap(View view) {//将View转化为Bitmap图片的方法
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {//将图片转化为2进制数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_to_register.dismiss();
    }
}
