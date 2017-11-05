package com.example.jia.classcircle.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;

import java.util.ArrayList;
import java.util.List;
public class RegisterActivity extends AppCompatActivity {
    private ImageView ivBack;
    private Spinner indentity_sp;
    private EditText edt_registerPassWord;
    private EditText edt_phone;
    private EditText edt_code;
    private TextView tv_sendCode;
    private Button btn_nextTo;
    private EditText edt_registerName;
    private String getIdentity="";
    private String getRegisterName="";
    private String getRegisterPhoneNo="";
    private String getRegisterPassWord="";
    private String getEditCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        onClickEvent();
    }

    private void onClickEvent() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_nextTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegisterName=edt_registerName.getText().toString().trim();//获取注册者姓名
                getRegisterPhoneNo=edt_phone.getText().toString().trim();//获取手机号
                getRegisterPassWord=edt_registerPassWord.getText().toString().trim();//获取设置的密码
                getEditCode=edt_code.getText().toString().trim();//获取注册验证码


                if (getRegisterName.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "未输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (getRegisterPassWord.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (getRegisterPhoneNo.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "未输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (getRegisterPhoneNo.length() > 0) { //判断手机正则
                    if (isMobile(getRegisterPhoneNo) == false) {
                        Toast.makeText(RegisterActivity.this, "该号码不合法，请重新输入手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (getEditCode.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "验证码为空，请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }



                //根据下拉框中选择的身份，下一步按钮点击时传递到管理者信息界面或学生界面,顺便清空本界面输入的数据
                APPUser user=new APPUser();
                user.setUsername(getRegisterName);
                user.setPassword(getRegisterPassWord);
                user.setPhoneNumber(getRegisterPhoneNo);
                user.setIndentity(getIdentity);
                if(getIdentity.equals("管理员")){
                    Intent managerIntent=new Intent();
                    managerIntent.putExtra("getRegisterName",getRegisterName);
                    managerIntent.putExtra("getRegisterPassWord",getRegisterPassWord);
                    managerIntent.putExtra("getRegisterPhoneNo",getRegisterPhoneNo);
                    managerIntent.putExtra("getIdentity",getIdentity);
                    managerIntent.setClass(RegisterActivity.this,ManagerMsgActivity.class);
                    startActivity(managerIntent);

                }else if(getIdentity.equals("学生")){
                    Intent studentIntent=new Intent();
                    studentIntent.putExtra("getRegisterStudentName",getRegisterName);
                    studentIntent.putExtra("getRegisterStudentPassWord",getRegisterPassWord);
                    studentIntent.putExtra("getRegisterStudentPhoneNo",getRegisterPhoneNo);
                    studentIntent.putExtra("getIdentityStudent",getIdentity);
                    studentIntent.setClass(RegisterActivity.this,StudentMsgActivity.class);
                    startActivity(studentIntent);
                }
            }
        });
        tv_sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);//处于不可点击状态
                timer.start();
            }
        });
    }

    private void initView() {
        indentity_sp= (Spinner) findViewById(R.id.sp_identity);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        edt_registerName= (EditText) findViewById(R.id.edt_registerName);
        edt_registerPassWord= (EditText) findViewById(R.id.edt_registerPassWord);
        edt_phone= (EditText) findViewById(R.id.edt_phone);
        edt_code= (EditText) findViewById(R.id.edt_code);
        tv_sendCode= (TextView) findViewById(R.id.tv_sendCode);
        btn_nextTo= (Button) findViewById(R.id.btn_registerToApp);

        initSpinnerData();
    }

    private void initSpinnerData() {
        //这里实现下拉列表选择身份
         final List<String> data_list=new ArrayList<>();
        data_list.add("管理员");
        data_list.add("学生");
        ArrayAdapter<String> arr_adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indentity_sp.setAdapter(arr_adapter);
        indentity_sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener (){


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getIdentity=data_list.get(position);
                parent.setVisibility(View.VISIBLE); //设置显示当前选择的项

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    CountDownTimer timer=new CountDownTimer(60000, 1000) {//用于倒计时, millisInFuture 总时长， countDownInterval 时间间隔
        @Override
        public void onTick(long millisUntilFinished) {
            tv_sendCode.setText(millisUntilFinished/1000+"秒后重发");
        }

        @Override
        public void onFinish() {
            tv_sendCode.setEnabled(true);
            tv_sendCode.setText("发送验证码");
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();//结束时，取消倒计时
    }
}
