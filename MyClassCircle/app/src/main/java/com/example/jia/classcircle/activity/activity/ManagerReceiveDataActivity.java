package com.example.jia.classcircle.activity.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MyDividerItemDecoration;
import com.example.jia.classcircle.activity.adapter.ShowStuDataAndReceiveAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.ClassHomeWork;

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

public class ManagerReceiveDataActivity extends AppCompatActivity {
    private APPUser appUser= BmobUser.getCurrentUser(APPUser.class);
    private Toolbar toolbar_manager_down_load_data;
    private EditText edt_search_stu_receive_homework_date;
    private ImageView img_search_homework_data;
    private RecyclerView recyclerView_receive_homework_data;
    private String homeworkDate="";//获取搜索的作业日期
    private ProgressDialog wait_dialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ClassHomeWork> classHomeWorkList=new ArrayList<>();
    private ShowStuDataAndReceiveAdapter showStuDataAndReceiveAdapter=new ShowStuDataAndReceiveAdapter(classHomeWorkList);
    private APPUser user=BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_down_load;
    private int TOTALDOWNLOAD=0;//显示的总文件数
    private int FINSH=0;//显示下载完成的文件数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_receive_data);
        getData();
        initView();
        onClickEvent();
    }

    private void getData() {
        wait_dialog=new ProgressDialog(ManagerReceiveDataActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("请稍等....");
        wait_dialog.show();
        wait_down_load=new ProgressDialog(ManagerReceiveDataActivity.this);
        wait_down_load.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_down_load.setMessage("当前文件进度："+"0%"+"     总文件："+TOTALDOWNLOAD+"      已完成:"+FINSH);
        wait_down_load.setIndeterminate(false);
        wait_down_load.setCancelable(false);
        BmobQuery<ClassHomeWork> query=new BmobQuery<>();
        query.addWhereEqualTo("className",appUser.getClassName());
        query.setLimit(500);
        query.findObjects(new FindListener<ClassHomeWork>() {
            @Override
            public void done(List<ClassHomeWork> list, BmobException e) {
                if(e==null){
                    classHomeWorkList=list;
                    showStuDataAndReceiveAdapter.updateDate(classHomeWorkList);
                    wait_dialog.hide();


                }else {
                    Toast.makeText(ManagerReceiveDataActivity.this,"读取失败，请重进一次",Toast.LENGTH_SHORT).show();
                    wait_dialog.hide();
                }
            }
        });

        //表监听
        final BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(rtd.isConnected()){
                    rtd.subTableUpdate("ClassHomeWork");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    String className= (String) data.opt("className");//判断获取同个班级的数据先
                    String userName=(String) data.opt("userName");
                    String homeworlUrl=(String) data.opt("homeworlUrl");
                    String time=(String) data.opt("time");
                    if(className.equals(user.getClassName())){
                        ClassHomeWork classHomeWork=new ClassHomeWork();
                        classHomeWork.setClassName(className);
                        classHomeWork.setUserName(userName);
                        classHomeWork.setHomeworlUrl(homeworlUrl);
                        classHomeWork.setTime(time);
                        classHomeWorkList.add(classHomeWork);
                        showStuDataAndReceiveAdapter.updateDate(classHomeWorkList);

                    }
                }
            }
        });

    }

    private void onClickEvent() {
        toolbar_manager_down_load_data.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_search_homework_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeworkDate=edt_search_stu_receive_homework_date.getText().toString();
                if(homeworkDate.length()==0){
                    showStuDataAndReceiveAdapter.updateDate(classHomeWorkList);//搜索未输入，则默认搜索全部
                    return;
                }
                //这里是检索数据的，直接拿取到的数据判断时间，再adapter.update 下就解决了
                showStuDataAndReceiveAdapter.updateDate( getSearchResult(homeworkDate));

            }
        });
    }

    private  List<ClassHomeWork> getSearchResult(String homeworkDate) {//返回用户索引时间的list
        List<ClassHomeWork> list=new ArrayList<ClassHomeWork>();
        for(int i=0;i<classHomeWorkList.size();i++){
            if(classHomeWorkList.get(i).getTime().equals(homeworkDate) ){
                list.add(classHomeWorkList.get(i));
            }
        }

        return list;

    }

    private void initView() {

        toolbar_manager_down_load_data= (Toolbar) findViewById(R.id.toolbar_manager_down_load_data);
        edt_search_stu_receive_homework_date= (EditText) findViewById(R.id.edt_search_stu_receive_homework_date);
        img_search_homework_data= (ImageView) findViewById(R.id.img_search_homework_data);
        recyclerView_receive_homework_data= (RecyclerView) findViewById(R.id.recyclerView_receive_homework_data);
        recyclerView_receive_homework_data.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        setSupportActionBar(toolbar_manager_down_load_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView_receive_homework_data.setLayoutManager(mLayoutManager);
        recyclerView_receive_homework_data.setAdapter(showStuDataAndReceiveAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_home_work_collect,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.img_receive_home_date:
                Toast.makeText(ManagerReceiveDataActivity.this,"管理员回收作业",Toast.LENGTH_SHORT).show();
                final List<ClassHomeWork> classHomeWorkListUpLoadList=   showStuDataAndReceiveAdapter.getSelectedItem();//收集到选中的数据
                AlertDialog.Builder builder=new AlertDialog.Builder(ManagerReceiveDataActivity.this);
                builder.setMessage("确认回收"+classHomeWorkListUpLoadList.size()+"份作业吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //留一大堆空，怕自己看不到，记得补完这里,记得做dialog提示稍等
                      //  showStuDataAndReceiveAdapter.getS
                        if(classHomeWorkListUpLoadList.size()==0){
                            Toast.makeText(ManagerReceiveDataActivity.this,"未选择作业无法下载",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wait_down_load.show();
                        TOTALDOWNLOAD=classHomeWorkListUpLoadList.size();
                        wait_down_load.setMessage("当前文件进度："+"0%"+"       文件数："+TOTALDOWNLOAD+"      已完成:"+FINSH);
                        for(int i=0;i<classHomeWorkList.size();i++){
                            String downloadHomewokrUrl=classHomeWorkList.get(i).getHomeworlUrl();
                            String [] strArry=downloadHomewokrUrl.split("\\/");
                            int last=strArry.length;//获取分隔符最后一个，取得文件名
                            String fileName=strArry[last-1];
                            BmobFile bmobFile=new BmobFile(fileName,"",downloadHomewokrUrl);
                            downloadFile(bmobFile);


                        }


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();





                break;
            default:
                break;
        }
        return true;
    }

    private void downloadFile(BmobFile bmobFile) {
        File saveFile=new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
        bmobFile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    if(FINSH<TOTALDOWNLOAD){
                        FINSH=FINSH+1;
                        wait_down_load.setMessage("当前文件进度："+"0%"+"       文件数："+TOTALDOWNLOAD+"      已完成:"+FINSH);

                    }else {
                        Toast.makeText(ManagerReceiveDataActivity.this,"全部下载完成",Toast.LENGTH_SHORT).show();
                        FINSH=0;//完毕后，归0，下次下载才不会有问题
                        wait_down_load.hide();
                    }

                }else {
                    FINSH=0;
                    Toast.makeText(ManagerReceiveDataActivity.this,"下载失败，请重试一次",Toast.LENGTH_SHORT).show();
                    wait_down_load.hide();
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {
                wait_down_load.setMessage("当前文件进度："+integer+"%"+"     文件数："+TOTALDOWNLOAD+"      已完成:"+FINSH);
            }


        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
        wait_down_load.dismiss();
    }
}
