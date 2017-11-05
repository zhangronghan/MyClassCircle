package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.Attendance;
import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class ManagerDetailAboutAttendanceActivity extends AppCompatActivity {//管理员发起考勤的的详细内容
    private Toolbar toolbar_create_attendance;
    private TextView text_manager_show_today;
    private TextView text_manager_show_week;
    private EditText edt__manager_attendance_content;
    private Button btn_manager_sure_attendance;
    private String Time="";
    private APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;
    private String id="";//获取attendance表该班级数据ID

    private List<ContentAndTime> contentAndTimeList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_detial_about_attendance);
        initData();//获取attendance表的该班级ID
        initView();
        onClickEvent();
    }

    private void initData() {
        BmobQuery<Attendance> query = new BmobQuery<>();
        query.addWhereEqualTo("className", user.getClassName());
        query.setLimit(50);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> list, BmobException e) {
                if(e==null){
                  //  Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"考勤表数据获取成功",Toast.LENGTH_SHORT).show();
                    id=list.get(0).getObjectId();
                    contentAndTimeList=list.get(0).getContentList();

                }else {
                    Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"考勤表数据获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private void onClickEvent() {
        toolbar_create_attendance.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_manager_sure_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder=new AlertDialog.Builder(ManagerDetailAboutAttendanceActivity.this);
                builder.setMessage("是否考勤？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String attendanceContent=edt__manager_attendance_content.getText().toString();
                        if(attendanceContent.length()==0){
                            Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"未输入内容",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wait_dialog.show();
                        // List<StuAboutCheck> stuAboutCheckList=new ArrayList<StuAboutCheck>();//将学生考勤的存到里面
                        // List<ContentAndTime> contentList=new ArrayList<ContentAndTime>();
                        ContentAndTime contentAndTime=new ContentAndTime();
                        contentAndTime.setTime(Time);
                        contentAndTime.setContent(attendanceContent);
                        contentAndTimeList.add(contentAndTime);
                        Attendance attendance=new Attendance();
                        attendance.setUserName(user.getUsername());
                        attendance.setIdentity(user.getIndentity());
                        attendance.setClassName(user.getClassName());
                        attendance.setContentList(contentAndTimeList);
                        //   attendance.setStuAboutCheckList(stuAboutCheckList);
                        if(id.length()==0){
                            wait_dialog.hide();
                            Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"班级ID拿不到",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        attendance.update(id, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    wait_dialog.hide();
                                    edt__manager_attendance_content.setText("");
                                    Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"成功",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent();
                                    intent.putExtra("freshSuccess",true);
                                    intent.setClass(ManagerDetailAboutAttendanceActivity.this,ManagerCheckAttendanceActivity.class);
                                    startActivity(intent);
                                    finish();//发送成功后，关闭本界面，回到ManagerCheckAttendanceActivity

                                }else {
                                    wait_dialog.hide();
                                    Toast.makeText(ManagerDetailAboutAttendanceActivity.this,"发布失败，请重试一次",Toast.LENGTH_SHORT).show();
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

    private void initView() {
        wait_dialog=new ProgressDialog(ManagerDetailAboutAttendanceActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait_dialog.setMessage("考勤发布中...");
        wait_dialog.setIndeterminate(false);
        wait_dialog.setCancelable(false);
        toolbar_create_attendance= (Toolbar) findViewById(R.id.toolbar_create_attendance);
        setSupportActionBar(toolbar_create_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_manager_show_today= (TextView) findViewById(R.id.text_manager_show_today);
        text_manager_show_week= (TextView) findViewById(R.id.text_manager_show_week);
        edt__manager_attendance_content= (EditText) findViewById(R.id.edt__manager_attendance_content);
        btn_manager_sure_attendance= (Button) findViewById(R.id.btn_manager_sure_attendance);
        text_manager_show_today.setText(getSystemTime());
        Time=text_manager_show_today.getText().toString();
        text_manager_show_week.setText(getWeekDay());

    }
    private String getSystemTime(){//获取系统时间，年月日时分
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth= c.get(Calendar.MONTH) + 1;// 获取当前月份,从0开始计
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        String detailTime=String.valueOf(mYear)+"/"+String.valueOf(mMonth)+"/"+String.valueOf(mDay)+"    "+String.valueOf(mHour)+":"+String.valueOf(mMinute);
        return detailTime;
    }
    private String getWeekDay(){
        Calendar c = Calendar.getInstance();
        if(c == null){
            return "星期一";
        }

        if(Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期一";
        }
        if(Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期二";
        }
        if(Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期三";
        }
        if(Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期四";
        }
        if(Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期五";
        }
        if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期六";
        }
        if(Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)){
            return "星期日";
        }

        return "星期一";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }
}
