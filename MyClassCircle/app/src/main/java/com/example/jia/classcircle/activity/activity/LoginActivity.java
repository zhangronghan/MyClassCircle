package com.example.jia.classcircle.activity.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmob_communication.DemoMessageHandler;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.newim.BmobIM.appContext;


public class LoginActivity extends AppCompatActivity {
    private EditText edt_account_number;
    private EditText edt_account_password;
    private TextView tv_register;
    private Button btn_login;
    private String accountNumber = "";
    private String accountPassWord = "";
    private ProgressDialog wait_dialog;//自动旋转对话框，用于提示等待

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initBmob();//初始化Bmob数据库,初始化BmobNewIM SDK
        initHuanXin();//初始化环信SDK，记住只能初始化一次，不然会出现自己T自己的情况
        initView();
        onClickEvent();

    }

    private void initHuanXin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e("Tag", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }


        //初始化
        EMClient.getInstance().init(LoginActivity.this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }


    private void initBmob() {
        Bmob.initialize(this, "9db39777bc7ec0846df5c87480543a31");
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler());


    }

    private void onClickEvent() {

        edt_account_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    accountNumber = edt_account_number.getText().toString().trim();
                    if (accountNumber.length() > 0) {
                        btn_login.setBackgroundColor(Color.parseColor("#0066FF"));
                        btn_login.getBackground().setAlpha(200);
                        btn_login.setEnabled(true);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn_login.setBackgroundColor(Color.parseColor("#0066FF"));
                    btn_login.getBackground().setAlpha(200);
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setBackgroundColor(Color.parseColor("#9f9f9f"));
                    btn_login.getBackground().setAlpha(150);
                    btn_login.setEnabled(false);
                }

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountNumber = edt_account_number.getText().toString().trim();//判断是否有输入和该是否有改账号
                accountPassWord = edt_account_password.getText().toString().trim();

                    /*if(accountNumber.length()==0){
                        account_number.setError("用户名不能为空");
                        //这里接着判断是否有这账号
                        return;
                    }else if(accountNumber.length()>0){
                        account_number.setError("");
                    }
                    if(accountPassWord.length()==0){
                        account_password.setError("用户密码不能为空");
                        return;
                    }else if(accountPassWord.length()>0){
                        account_password.setError("");
                    }*/

                if (accountNumber.length() == 0) {
                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (accountPassWord.length() == 0) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                wait_dialog.show();//显示进度旋转
                BmobUser user = new BmobUser();
                user.setUsername(accountNumber);
                user.setPassword(accountPassWord);

                user.login(new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            //环信聊天登录
                            EMClient.getInstance().login(accountNumber, accountPassWord, new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    Log.d("main", "登录聊天服务器成功！");
                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }

                                @Override
                                public void onError(int code, String message) {
                                    Log.d("main", "登录聊天服务器失败！");
                                }
                            });
                            edt_account_number.setText("");
                            edt_account_password.setText("");
                            connectIM();  //登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作


                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();//如果失败也要取消
                        }
                    }
                });
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }


    private void initView() {
        APPUser bmobUser = BmobUser.getCurrentUser(APPUser.class);
        if (bmobUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }

        edt_account_number = (EditText) findViewById(R.id.edt_account_number);
        edt_account_password = (EditText) findViewById(R.id.edt_account_password);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        wait_dialog = new ProgressDialog(LoginActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("登录中，请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
    }

    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }

    private void connectIM() {

        APPUser user = BmobUser.getCurrentUser(APPUser.class);
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //连接成功
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        // Toast.makeText(LoginActivity.this,"IM连接成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        wait_dialog.hide();//如果成功取消
                        finish();
                    } else {
                        //连接失败
                        // toast(e.getMessage());
                        wait_dialog.hide();
                        Toast.makeText(LoginActivity.this, "请重试一次", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
