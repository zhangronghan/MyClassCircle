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
import com.example.jia.classcircle.activity.util.ImgUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class StudentMsgActivity extends AppCompatActivity {//如果身份是学生，则跳转到此页面
    private ImageView ivBack;
    private TextView tv_showStudentUserName;
    private ImageView StudentHeadImageView;
    private TextView tv_chooseStudentImg;
    private Spinner sp_getStudentYear;
    private Spinner sp_getStudentProfession;
    private Spinner sp_getStudentClassNum;
    private TextView tv_registerStu_date;
    private Button btn_commitStudentMsg;
    private String RegisterStudentName="",RegisterStudentPassWord="",RegisterStudentIdentity="",RegisterStudentPhoneNo="";
    private String getYear="",profession="",classNum="";//三个下拉框就为实现：2015，软件，2班
    private static final int CHOOSE_PHOTO=0;
    private Bitmap imgMap;
    private ProgressDialog wait_to_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_msg);
        getIntentValue();
        initView();
        initSpinner();
        onClickEvent();
    }

    private void onClickEvent() {

        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        tv_registerStu_date.setText(String.valueOf(mYear)+"   年   "+(mMonth+1)+"  月    "+mDay+"    号");
        tv_showStudentUserName.setText("尊敬的"+RegisterStudentName+",请继续完善您的信息");
        tv_chooseStudentImg.setOnClickListener(new View.OnClickListener() {
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


        btn_commitStudentMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(StudentMsgActivity.this);
                builder.setMessage("是否注册");
                builder.setTitle("提示");
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_to_register.show();//显示旋转
                        String classMsg=getYear+profession+classNum;
                        APPUser user=new APPUser();
                        byte[] imgMsg=Bitmap2Bytes(imgMap);//将图片转为二进制存储
                        user.setUsername(RegisterStudentName);
                        user.setPassword(RegisterStudentPassWord);
                        user.setPhoneNumber(RegisterStudentPhoneNo);
                        user.setClassName(classMsg);
                        user.setImg_msg(imgMsg);
                        user.setIndentity(RegisterStudentIdentity);
                        user.setIfJoinClass(false);//刚注册时，未加入任何班级

                        user.signUp(new SaveListener<APPUser>() {

                            @Override
                            public void done(APPUser appUser, BmobException e) {
                                if(e==null){
                                    Toast.makeText(StudentMsgActivity.this,"注册成功,将回到登录界面",Toast.LENGTH_SHORT).show();
                                    //环信账号密码注册,注册用户名会自动转为小写字母，所以建议用户名均以小写注册
                                    new  Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().createAccount(RegisterStudentName, RegisterStudentPassWord);//同步方法
                                                Log.e("环信","注册成功");
                                            } catch (HyphenateException e1) {
                                                e1.printStackTrace();

                                                Log.e("环信","注册失败"+ e1.getErrorCode()+ e1.getMessage());
                                            }
                                        }
                                    }).start();



                                    wait_to_register.hide();
                                    startActivity(new Intent(StudentMsgActivity.this,LoginActivity.class));
                                    finish();
                                }else {
                                    wait_to_register.hide();
                                    Toast.makeText(StudentMsgActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initSpinner() {
        final List<String> year_list=new ArrayList<>();//获取年
        final List<String> profession_list=new ArrayList<>();//专业
        final List<String> classNum_list=new ArrayList<>();//班级
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); //获取当前年份,为了让下拉框年自动更新年份，获得今年，上年，和上上年
        for(int i=0;i<3;i++){
            String getThreeYears=String.valueOf(mYear);
            year_list.add(getThreeYears);
            mYear--;
        }
        ArrayAdapter<String> arr_year_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year_list);
        arr_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getStudentYear.setAdapter(arr_year_adapter);
        sp_getStudentYear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getYear=year_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profession_list.add("软件");profession_list.add("网络");profession_list.add("图形图像");
        profession_list.add("测量");profession_list.add("智能交通");profession_list.add("公路测量");profession_list.add("轨道");
        ArrayAdapter<String> arr_profession_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,profession_list);
        arr_profession_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getStudentProfession.setAdapter(arr_profession_adapter);
        sp_getStudentProfession.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profession=profession_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for(int i=1;i<10;i++){
            classNum_list.add(String.valueOf(i));
        }
        ArrayAdapter<String> arr_classNum_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classNum_list);
        arr_classNum_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_getStudentClassNum.setAdapter(arr_classNum_adapter);
        sp_getStudentClassNum.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classNum=classNum_list.get(position);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        RegisterStudentName=intent.getStringExtra("getRegisterStudentName");
        RegisterStudentPassWord=intent.getStringExtra("getRegisterStudentPassWord");
        RegisterStudentIdentity=intent.getStringExtra("getIdentityStudent");
        RegisterStudentPhoneNo=intent.getStringExtra("getRegisterStudentPhoneNo");
    }

    private void initView() {
        ivBack= (ImageView) findViewById(R.id.iv_back);
        tv_showStudentUserName= (TextView) findViewById(R.id.tv_showStudentUserName);
        StudentHeadImageView= (ImageView) findViewById(R.id.StudentHeadImageView);
        tv_chooseStudentImg= (TextView) findViewById(R.id.tv_chooseStudentImg);
        sp_getStudentYear= (Spinner) findViewById(R.id.sp_getStudentYear);
        sp_getStudentProfession= (Spinner) findViewById(R.id.sp_getStudentProfession);
        sp_getStudentClassNum= (Spinner) findViewById(R.id.sp_getStudentClassNum);
        tv_registerStu_date= (TextView) findViewById(R.id.tv_registerStu_date);
        btn_commitStudentMsg= (Button) findViewById(R.id.btn_commitStudentMsg);
        StudentHeadImageView.setImageResource(R.drawable.default_head_image);
        imgMap=convertViewToBitmap(StudentHeadImageView);//如果不选择相片，则默认为这张图片);


        wait_to_register=new ProgressDialog(StudentMsgActivity.this);
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
                        imgMap=bitmap;//用来传图片


                    } else {
                        //4.4以下系统使用这个方法处理图片
                        bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        imgMap=bitmap;

                        Uri originalUri3=data.getData();//获取图片uri
                        String []imgs13={MediaStore.Images.Media.DATA};//将图片URI转换成存储
                        Cursor cursor3=this.managedQuery(originalUri3, imgs13, null, null, null);
                        int index3=cursor3.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor3.moveToFirst();
                        String img_url3=cursor3.getString(index3);
                       // Toast.makeText(StudentMsgActivity.this,"图片路径为2"+img_url3,Toast.LENGTH_SHORT).show();
                    }
                    StudentHeadImageView.setImageBitmap(bitmap);

                }

                break;
            default:
                break;
        }
    }
    public  Bitmap convertViewToBitmap(View view) {//将View转化为Bitmap图片的方法
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }
    private byte[] Bitmap2Bytes(Bitmap bm){//将图片转化为2进制数据
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
