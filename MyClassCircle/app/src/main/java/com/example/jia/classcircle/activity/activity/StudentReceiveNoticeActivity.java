package com.example.jia.classcircle.activity.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.ShowFileDownLoadAdapter;
import com.example.jia.classcircle.activity.adapter.ShowNoticeMsgAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeFile;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeMsg;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;

public class StudentReceiveNoticeActivity extends AppCompatActivity {
    final APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private Toolbar toolbar_receive_notice;
    private RecyclerView recyclerView_showContent;
    private RecyclerView recyclerView_showFile;
    private ProgressDialog wait_dialog;//自动旋转对话框，用于提示等待
    private List<AllNoticeMsg> listNoticeMsg=new ArrayList<>();
    private List<AllNoticeFile> listFileUrlMsg=new ArrayList<>();
    private ShowNoticeMsgAdapter noticeContentAdapter=new ShowNoticeMsgAdapter(listNoticeMsg);
    private ShowFileDownLoadAdapter showFileDownLoadAdapter=new ShowFileDownLoadAdapter(listFileUrlMsg);
    private BmobRealTimeData rtd_notice=new BmobRealTimeData();//监听公告表
    private BmobRealTimeData rtd_file=new BmobRealTimeData();//监听文件表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_receive_notice);
        initData();
        initView();


    }

    private void onClickEvent() {
        showFileDownLoadAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                final String url=  listFileUrlMsg.get(position).getNoticeFileUrl();
                String [] strArry=url.split("\\/");
                int last=strArry.length;//获取分隔符最后一个，取得文件名
                final String fileName=strArry[last-1];

                AlertDialog.Builder builder=new AlertDialog.Builder(StudentReceiveNoticeActivity.this);
                builder.setMessage("确认下载"+fileName+"？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_dialog.show();
                      //  Toast.makeText(StudentReceiveNoticeActivity.this,"确认就下载了",Toast.LENGTH_SHORT).show();
                        BmobFile bmobFile=new BmobFile(fileName,"",url);
                        downloadFile(bmobFile);

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

    private void downloadFile(BmobFile bmobFile) {
        final File saveFile=new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
        bmobFile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(StudentReceiveNoticeActivity.this,"保存路径："+s,Toast.LENGTH_SHORT).show();
                    wait_dialog.hide();
                }else {
                    Toast.makeText(StudentReceiveNoticeActivity.this,"下载失败，请重试一次",Toast.LENGTH_SHORT).show();
                    wait_dialog.hide();
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {
                wait_dialog.setMessage("下载进度："+integer+"%");
            }
        });

    }


    private void initData() { //获取同步的数据,分别查询获取AllNoticeMsg，AllNoticeFile


        BmobQuery<AllNoticeMsg> query = new BmobQuery<AllNoticeMsg>();//这里查询公告的消息
        query.addWhereEqualTo("className",user.getClassName());
        query.setLimit(500);
        query.findObjects(new FindListener<AllNoticeMsg>() {
            @Override
            public void done(List<AllNoticeMsg> list, BmobException e) {
                if(e==null){
                    listNoticeMsg=list;
                    noticeContentAdapter=new ShowNoticeMsgAdapter(listNoticeMsg);
                    recyclerView_showContent.setAdapter(noticeContentAdapter);

                }else {
                    Toast.makeText(StudentReceiveNoticeActivity.this,"公告消息获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        BmobQuery<AllNoticeFile> queryFile=new BmobQuery<>();//查询
        queryFile.addWhereEqualTo("className",user.getClassName());
        queryFile.setLimit(500);
        queryFile.findObjects(new FindListener<AllNoticeFile>() {
            @Override
            public void done(List<AllNoticeFile> list, BmobException e) {
                if(e==null){
                    listFileUrlMsg=list;
                    showFileDownLoadAdapter=new ShowFileDownLoadAdapter(listFileUrlMsg);
                    recyclerView_showFile.setAdapter(showFileDownLoadAdapter);
                    onClickEvent();

                }else {
                    Toast.makeText(StudentReceiveNoticeActivity.this,"文件路径获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        });



        rtd_notice.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null && rtd_notice.isConnected()){
                    rtd_notice.subTableUpdate("AllNoticeMsg");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.opt("action"))){
                    JSONObject data=jsonObject.optJSONObject("data");
                    String className= (String) data.opt("className");
                    String noticeTime= (String) data.opt("noticeTime");
                    String noticeContent= (String) data.opt("noticeContent");
                    if(className.equals(user.getClassName())){//接收的信息是与该用户同个班级则接受
                        AllNoticeMsg noticeMsg=new AllNoticeMsg();
                        noticeMsg.setClassName(className);
                        noticeMsg.setClassName(noticeTime);
                        noticeMsg.setNoticeContent(noticeContent);
                        listNoticeMsg.add(noticeMsg);
                        noticeContentAdapter.updateDate(listNoticeMsg);
                    }
                }

            }
        });
        rtd_file.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null && rtd_file.isConnected()){
                    rtd_file.subTableUpdate("AllNoticeFile");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.opt("action"))){
                    JSONObject data=jsonObject.optJSONObject("data");
                    String className= (String) data.opt("className");
                    String noticeFileUrl= (String) data.opt("noticeFileUrl");
                    String noticeTime= (String) data.opt("noticeTime");
                    if(className.equals(user.getClassName())) {//接收的文件信息是与该用户同个班级则接受
                        AllNoticeFile allNoticeFile=new AllNoticeFile();
                        allNoticeFile.setNoticeFileUrl(noticeFileUrl);
                        allNoticeFile.setClassName(className);
                        allNoticeFile.setNoticeTime(noticeTime);
                        listFileUrlMsg.add(allNoticeFile);
                        showFileDownLoadAdapter.updateDate(listFileUrlMsg);
                        onClickEvent();
                    }
                }
            }
        });



    }

    private void initView() {
        wait_dialog=new ProgressDialog(StudentReceiveNoticeActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("下载进度："+"0%");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        toolbar_receive_notice= (Toolbar) findViewById(R.id.toolbar_receive_notice);
        setSupportActionBar(toolbar_receive_notice);
        getSupportActionBar().setTitle(user.getClassName()+"班公告群");//设置标题栏为该班级公告群
        recyclerView_showContent= (RecyclerView) findViewById(R.id.recyclerView_showContent);
        recyclerView_showFile= (RecyclerView) findViewById(R.id.recyclerView_showFile);
        setSupportActionBar(toolbar_receive_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头

        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            toolbar_receive_notice.setBackgroundColor(Color.parseColor("#534f4f"));
        }


        toolbar_receive_notice.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager layoutManager1=new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this);
        recyclerView_showContent.setLayoutManager(layoutManager1);
        recyclerView_showFile.setLayoutManager(layoutManager2);
        recyclerView_showContent.setAdapter(noticeContentAdapter);
        recyclerView_showFile.setAdapter(showFileDownLoadAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }


}
