package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.AttendanceAdapter;
import com.example.jia.classcircle.activity.adapter.DetailAboutStuAttendanceAdapter;
import com.example.jia.classcircle.activity.adapter.MyDividerItemDecoration;
import com.example.jia.classcircle.activity.bean.AttendanceBean;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.Attendance;
import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;
import com.example.jia.classcircle.activity.listener.MyOnItemLongClickListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class ManagerCheckAttendanceActivity extends AppCompatActivity {//管理员查看考勤界面
    private Toolbar toolbar_manager_check_attendance;
    private RecyclerView recyclerView_show_attendance;
    private List<Attendance> attendanceList=new ArrayList<>();
    private List<ContentAndTime> contentAndTimeList=new ArrayList<>();
    private List<StuAboutCheck> stuAboutCheckList=new ArrayList<>();
    private AttendanceAdapter attendanceAdapter;
    private APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private String id="";//获取attendance表该班级数据ID
    private ProgressDialog wait_dialog;
    private List<ContentAndTime> deleteList=new ArrayList<>();//用来存删除后的数据，如果成功，则更改contentAndTimeList数据
    private int getTheNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_check_attendance);
        initData();
        initView();
        onclickEvent();
        attendanceSuccess();//考勤成功后,传回本界面并且刷新数据，监听貌似有问题
    }

    private void attendanceSuccess() {
        Intent intent=getIntent();
        boolean ifSuccess = Boolean.parseBoolean(intent.getStringExtra("freshSuccess"));
        if(ifSuccess){
            refreshData();
        }
    }

    private void refreshData() {
        BmobQuery<Attendance>query=new BmobQuery<>();//获取其表数据
        query.addWhereEqualTo("className",user.getClassName());
        query.setLimit(500);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> list, BmobException e) {
                if(e==null){
                    id=list.get(0).getObjectId();
                    attendanceList=list;
                    deleteList=attendanceList.get(0).getContentList();
                    contentAndTimeList=attendanceList.get(0).getContentList();
                    stuAboutCheckList=attendanceList.get(0).getStuAboutCheckList();
                    attendanceAdapter.updateDate(contentAndTimeList);
                    Toast.makeText(ManagerCheckAttendanceActivity.this,"考勤表数据获取成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        //监听attendance表（行监听，试试能不能拿到）和获取其表数据
        BmobQuery<Attendance>query=new BmobQuery<>();//获取其表数据
        query.addWhereEqualTo("className",user.getClassName());
        query.setLimit(500);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> list, BmobException e) {
                if(e==null){
                    id=list.get(0).getObjectId();
                    attendanceList=list;
                    deleteList=attendanceList.get(0).getContentList();
                    contentAndTimeList=attendanceList.get(0).getContentList();
                    stuAboutCheckList=attendanceList.get(0).getStuAboutCheckList();
                    attendanceAdapter.updateDate(contentAndTimeList);


                    Toast.makeText(ManagerCheckAttendanceActivity.this,"考勤表数据获取成功",Toast.LENGTH_SHORT).show();
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
                        Log.e("郭加盛日志","连接状态"+rtd.isConnected());
                    }else {
                        Log.e("郭加盛日志","连接状态"+rtd.isConnected());
                    }
                }else {
                    Log.e("郭加盛日志",""+e.getMessage());
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                //Log.e("郭加盛","jsonObject:"+jsonObject.toString());//由于更新后，传过来的是json数据，要解析它，建个bean类,还得判断是同个班级的
                if(BmobRealTimeData.ACTION_UPDATEROW.equals(jsonObject.optString("action"))){

                    Gson mGson=new Gson();
                    AttendanceBean bean=mGson.fromJson(jsonObject.toString(),AttendanceBean.class);
                    if(bean.getData().getClassName().equals(user.getClassName())){ //班级名一致

                        List<ContentAndTime> contentList=new ArrayList<ContentAndTime>();
                        List<AttendanceBean.DataBean.ContentListBean> list=bean.getData().getContentList();
                        for(int i=0;i<list.size();i++){
                            ContentAndTime contentAndTime=new ContentAndTime();
                            contentAndTime.setContent(list.get(i).getContent());
                            contentAndTime.setTime(list.get(i).getTime());
                            contentList.add(contentAndTime);
                        }
                        contentAndTimeList=contentList;
                        deleteList=contentList;
                        if(contentList!=null){
                            attendanceAdapter.updateDate(contentAndTimeList);

                        }else {
                            Log.e("郭加盛","取不到数据");
                        }
                    }


                }else {
                    Log.e("郭加盛","action数据错咯");
                }
            }
        });

    }



    private void onclickEvent() {
        toolbar_manager_check_attendance.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        attendanceAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, final int position) {
                //弹出自定义对话框，查看该班学生该次考勤情况
                int a=position;
                String managerTime= contentAndTimeList.get(position).getTime();
                List<StuAboutCheck> temporaryList=new ArrayList<StuAboutCheck>();//用于临时存储筛选出啦的数据
               // stuAboutCheckList
                for(int i=0;i<stuAboutCheckList.size();i++){
                    if(stuAboutCheckList.get(i).getManagerTime().equals(managerTime)){
                        temporaryList.add(stuAboutCheckList.get(i));
                    }
                }


                showResultDialog(temporaryList);




            }
        });
        attendanceAdapter.setOnItemLongClickListener(new MyOnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(View view, final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ManagerCheckAttendanceActivity.this);
                builder.setMessage("是否删除该次考勤？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(deleteList.size()==0){//当前0个，且要删除该数据，则直接new一个新的出来吧

                            Toast.makeText(ManagerCheckAttendanceActivity.this,"没有数据，无法删除",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wait_dialog.show();
                        deleteList.remove(position);//删除后，及时更新数据
                        Attendance attendance=new Attendance();
                        attendance.setContentList(contentAndTimeList);
                        attendance.update(id, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    contentAndTimeList=deleteList;
                                    attendanceAdapter.updateDate(contentAndTimeList);
                                    Toast.makeText(ManagerCheckAttendanceActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    wait_dialog.hide();
                                }else if(e!=null){
                                    Toast.makeText(ManagerCheckAttendanceActivity.this,"删除失败，请重试一次",Toast.LENGTH_SHORT).show();
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
    }

    private void showResultDialog(List<StuAboutCheck> temporaryList) {//展现自定义布局，里面是一个recyclerView和返回按钮
        if(temporaryList.size()==0){
            Toast.makeText(ManagerCheckAttendanceActivity.this,"没人签到",Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view=View.inflate(this,R.layout.dailog_show_stu_attendance,null);
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题
        DetailAboutStuAttendanceAdapter aboutStuAttendanceAdapter=new DetailAboutStuAttendanceAdapter(temporaryList);
        TextView tv_show_result= (TextView) view.findViewById(R.id.tv_show_result);
        RecyclerView recyclerView_show_stu_attendance_result= (RecyclerView) view.findViewById(R.id.recyclerView_show_stu_attendance_result);
        recyclerView_show_stu_attendance_result.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager  mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView_show_stu_attendance_result.setLayoutManager(mLayoutManager);
        recyclerView_show_stu_attendance_result.setAdapter(aboutStuAttendanceAdapter);

        tv_show_result.setText("签到人数:"+temporaryList.size());
        Button btn_manager_check_allStu_attendance= (Button) view.findViewById(R.id.btn_manager_check_allStu_attendance);
        btn_manager_check_allStu_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//取消显示
            }
        });

        dialog.show();
    }

    private void initView() {
        toolbar_manager_check_attendance= (Toolbar) findViewById(R.id.toolbar_manager_check_attendance);
        setSupportActionBar(toolbar_manager_check_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView_show_attendance= (RecyclerView) findViewById(R.id.recyclerView_show_attendance);
        recyclerView_show_attendance.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView_show_attendance.setLayoutManager(manager);
        attendanceAdapter=new AttendanceAdapter(contentAndTimeList);
        recyclerView_show_attendance.setAdapter(attendanceAdapter);

        wait_dialog=new ProgressDialog(ManagerCheckAttendanceActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait_dialog.setMessage("删除中，请稍等...");
        wait_dialog.setIndeterminate(false);
        wait_dialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.toolbar_check_stu_attendance,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.check_stu:
                //发起考勤
                startActivity(new Intent(ManagerCheckAttendanceActivity.this,ManagerDetailAboutAttendanceActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }


}
