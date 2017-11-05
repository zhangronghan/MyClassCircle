package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MyDividerItemDecoration;
import com.example.jia.classcircle.activity.adapter.StuAttendanceAdapter;
import com.example.jia.classcircle.activity.bean.AttendanceBean;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.Attendance;
import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class StudentAttendanceActivity extends AppCompatActivity {//学生签到界面
    private Toolbar toolbar_check_attendance;
    private TextView text_show_today;
    private TextView text_show_week;
    private RecyclerView recyclerView_show_stu_attendance;
    private APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;
    private ProgressDialog wait_dialog_attendance;
    private List<Attendance> attendanceList=new ArrayList<>();
    private List<ContentAndTime> contentAndTimeList=new ArrayList<>();//里面是管理员发布的考勤
    private List<StuAboutCheck> stuAboutCheckList=new ArrayList<>();//里面记录的是所有学生的list
    private List<StuAboutCheck> otherStuList=new ArrayList<>();//里面存储的是其它学生的数据，等到本用户的数据更完，再加到这里更新上传
    private StuAttendanceAdapter stuAttendanceAdapter;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stundent_attence);
        initView();
        initData();

        onclickEvent();
    }

    private void initData() {
        wait_dialog=new ProgressDialog(StudentAttendanceActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("获取数据中...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        wait_dialog_attendance=new ProgressDialog(StudentAttendanceActivity.this);
        wait_dialog_attendance.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait_dialog_attendance.setMessage("签到中...");
        wait_dialog_attendance.setIndeterminate(false);
        wait_dialog_attendance.setCancelable(false);
        wait_dialog.show();

        BmobQuery<Attendance> query1=new BmobQuery<>();
        query1.addWhereEqualTo("className",user.getClassName());
        query1.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> list, BmobException e) {
                if(e==null){//对于数据库查询尽量采用分页的方式查询,我一口气查询是遇到内存泄露了,contentAndTimeList我重复使用，使得资源无法回收。。。
                    attendanceList=list;
                    id=attendanceList.get(0).getObjectId();
                    contentAndTimeList=attendanceList.get(0).getContentList();//为何是get(0),因为里面该班级永远只有一行,
                    stuAboutCheckList=attendanceList.get(0).getStuAboutCheckList();
                    List<StuAboutCheck> temporaryList=attendanceList.get(0).getStuAboutCheckList();//该班所有学生的数据
                    List<StuAboutCheck> temporaryAdd=new ArrayList<StuAboutCheck>();//临时存筛选出来的数据
                    for(int i=0;i<temporaryList.size();i++){
                        if(temporaryList.get(i).getStuName().equals(user.getUsername())){
                            StuAboutCheck a=temporaryList.get(i);
                            temporaryAdd.add(a);
                        }else {
                            StuAboutCheck other=temporaryList.get(i) ;
                            otherStuList.add(other);
                        }
                    }
                    stuAboutCheckList=temporaryAdd;
                    stuAttendanceAdapter.updateData(contentAndTimeList,stuAboutCheckList);
                    wait_dialog.hide();




                /*    attendanceList=list;这个是内存泄露的不科学写法，留在这当纪念
                    contentAndTimeList=attendanceList.get(0).getContentList();//为何是get(0),因为里面该班级永远只有一行,
                    stuAboutCheckList=attendanceList.get(0).getStuAboutCheckList();
                    for(int i=0;i<attendanceList.get(0).getStuAboutCheckList().size();i++){
                        if(attendanceList.get(0).getStuAboutCheckList().get(i).getStuName().equals(user.getUsername())){//筛出属于该学生的数据
                            stuAboutCheckList.add(attendanceList.get(0).getStuAboutCheckList().get(i));

                        }

                    }
                    stuAttendanceAdapter.updateData(contentAndTimeList,stuAboutCheckList);
                    wait_dialog.hide();
*/
                  //  refreshData(attendanceList);

                }else {
                    Toast.makeText(StudentAttendanceActivity.this,"获取失败，请重试一次",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //监听行数据
        final BmobRealTimeData rtd=new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null){
                    if(rtd.isConnected()){
                        rtd.subRowUpdate("Attendance",id);
                    }
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATEROW.equals(jsonObject.optString("action"))){
                    Gson mGson=new Gson();
                    AttendanceBean bean=mGson.fromJson(jsonObject.toString(),AttendanceBean.class);
                    if(bean.getData().getClassName().equals(user.getClassName())) { //班级名一致
                        List<ContentAndTime> contentList=new ArrayList<ContentAndTime>();
                        List<AttendanceBean.DataBean.ContentListBean> list=bean.getData().getContentList();
                        for(int i=0;i<list.size();i++){
                            ContentAndTime contentAndTime=new ContentAndTime();
                            contentAndTime.setContent(list.get(i).getContent());
                            contentAndTime.setTime(list.get(i).getTime());
                            contentList.add(contentAndTime);
                        }
                        contentAndTimeList=contentList;
                        if(contentAndTimeList!=null){
                            stuAttendanceAdapter.updateData(contentAndTimeList,stuAboutCheckList);
                        }

                    }
                }

            }
        });





    }



    private void onclickEvent() {
        toolbar_check_attendance.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stuAttendanceAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, final int position) {
              //  if(stuAboutCheckList.get(p)){}

                for(int i=0;i<stuAboutCheckList.size();i++){
                    if(contentAndTimeList.get(position).getTime().equals(stuAboutCheckList.get(i).getManagerTime())){

                        if(stuAboutCheckList.get(i).isIfCheck()==true){
                            Toast.makeText(StudentAttendanceActivity.this,"已签到",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                }
                AlertDialog.Builder builder=new AlertDialog.Builder(StudentAttendanceActivity.this);
                builder.setMessage("是否签到？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_dialog_attendance.show();
                        //先上传,上传更新成功后,再更改本表
                        String objectID=attendanceList.get(0).getObjectId();
                        Attendance attendance=new Attendance();


                        StuAboutCheck stuAboutCheck=new StuAboutCheck();
                        stuAboutCheck.setStuName(user.getUsername());
                        stuAboutCheck.setManagerTime(contentAndTimeList.get(position).getTime());
                        stuAboutCheck.setIfCheck(true);
                        stuAboutCheck.setTime(getSystemTime());

                       // otherStuList.add(stuAboutCheck);//其它学生+该学生=所有学生
                        final List<StuAboutCheck> updateStuAboutCheckList=otherStuList;
                        stuAboutCheckList.add(stuAboutCheck);//
                        for(int i=0;i<stuAboutCheckList.size();i++){//将全部点击的签到加到otherList中
                            StuAboutCheck stu=stuAboutCheckList.get(i);
                            updateStuAboutCheckList.add(stu);
                        }

                        attendance.setStuAboutCheckList(updateStuAboutCheckList);
                        attendance.update(objectID, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    stuAttendanceAdapter.updateData(contentAndTimeList,stuAboutCheckList);
                                    Toast.makeText(StudentAttendanceActivity.this,"签到成功",Toast.LENGTH_SHORT).show();
                                    wait_dialog_attendance.hide();
                                }else {
                                    Toast.makeText(StudentAttendanceActivity.this,"签到失败，请重试一次",Toast.LENGTH_SHORT).show();
                                    wait_dialog_attendance.hide();
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

        toolbar_check_attendance= (Toolbar) findViewById(R.id.toolbar_check_attendance);
        setSupportActionBar(toolbar_check_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            toolbar_check_attendance.setBackgroundColor(Color.parseColor("#534f4f"));
        }
        text_show_today= (TextView) findViewById(R.id.text_show_today);
        text_show_week= (TextView) findViewById(R.id.text_show_week);
        recyclerView_show_stu_attendance= (RecyclerView) findViewById(R.id.recyclerView_show_stu_attendance);


        text_show_today.setText(getSystemTime());
        text_show_week.setText(getWeekDay());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView_show_stu_attendance.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView_show_stu_attendance.setLayoutManager(manager);
        stuAttendanceAdapter=new StuAttendanceAdapter(contentAndTimeList,stuAboutCheckList);
        recyclerView_show_stu_attendance.setAdapter(stuAttendanceAdapter);

    }
    private String getSystemTime(){//获取系统时间，年月日
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
        wait_dialog_attendance.dismiss();
    }
}
