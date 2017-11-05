package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MyDividerItemDecoration;
import com.example.jia.classcircle.activity.adapter.ShowClassmatesAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.bmobTable.Classmates;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;
import com.example.jia.classcircle.activity.util.ChatWidthFriend;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.newim.core.BmobIMClient.getContext;

public class ShowClassmatesActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_show_classmate;
    private List<Classmates> classmatesList = new ArrayList<>();
    private ShowClassmatesAdapter classmatesAdapter = new ShowClassmatesAdapter(classmatesList);
    private List<AllTheClass> classList = new ArrayList<>();
    private APPUser user = BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;//自动旋转对话框，用于提示等待
    private Toolbar toolbar_showStuResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_classmates);
        initView();
        onClickEvent();
    }

    private void onClickEvent() {
        toolbar_showStuResult.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFreshData();
            }
        });
        classmatesAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {

                Intent intent = new Intent();
                ChatWidthFriend friend = new ChatWidthFriend();//序列化传值
                friend.setName(classmatesList.get(position).getUserName());
                friend.setImgByte(Bitmap2Bytes(classmatesList.get(position).getHeadImg()));
                intent.putExtra("friend", friend);

                /*intent.putExtra("name",classmatesList.get(position).getUserName());
                intent.putExtra("imgByte",  imgByteList.get(position))*/
                ;


                intent.setClass(ShowClassmatesActivity.this, ChatWidthFriendActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        wait_dialog = new ProgressDialog(ShowClassmatesActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("刷新数据中，请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        toolbar_showStuResult = (Toolbar) findViewById(R.id.toolbar_showStuResult);
        setSupportActionBar(toolbar_showStuResult);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_showStuResult.setBackgroundColor(Color.parseColor("#534f4f"));
        }
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView_show_classmate = (RecyclerView) findViewById(R.id.recyclerView_show_classmate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowClassmatesActivity.this);
        recyclerView_show_classmate.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView_show_classmate.setLayoutManager(layoutManager);
        recyclerView_show_classmate.setAdapter(classmatesAdapter);
        initData();

    }

    private void initData() {
        wait_dialog.show();
        final List<APPUser> temporrary = new ArrayList<>();
        BmobQuery<AllTheClass> query = new BmobQuery<>();
        query.addWhereEqualTo("className", user.getClassName());
        query.findObjects(new FindListener<AllTheClass>() {
            @Override
            public void done(List<AllTheClass> list, BmobException e) {
                if (e == null) {
                    classList = list;
                    List<APPUser> appUserList = list.get(0).getStu();
                    for (int i = 0; i < appUserList.size(); i++) { //把除自己外的本班学生筛选
                        APPUser a = appUserList.get(i);
                        if (user.getUsername().equals(a.getUsername())) {
                            continue;
                        } else {
                            temporrary.add(a);
                            Classmates classmates = new Classmates();
                            classmates.setUserName(a.getUsername());
                            classmates.setHeadImg(getBitmapFromByte(a.getImg_msg()));
                            classmates.setIdentity(a.getIndentity());
                            classmatesList.add(classmates);
                        }


                    }
                    classmatesAdapter.updateData(classmatesList);
                    // classmatesList.clear();
                    wait_dialog.hide();
                    Log.e("查询日志：", "AllTheClass表的size" + list.size());

                } else {
                    wait_dialog.hide();
                    Log.e("查询日志：", "msg:" + e.getMessage().toString() + "   code" + e.getErrorCode());
                    Toast.makeText(getContext(), "班级数据获取失败，请刷新一次", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    private void reFreshData() {

        final List<APPUser> temporrary = new ArrayList<>();
        BmobQuery<AllTheClass> query = new BmobQuery<>();
        query.addWhereEqualTo("className", user.getClassName());
        query.findObjects(new FindListener<AllTheClass>() {
            @Override
            public void done(List<AllTheClass> list, BmobException e) {
                if (e == null) {
                    classmatesList.clear();
                    classList = list;
                    List<APPUser> appUserList = list.get(0).getStu();
                    for (int i = 0; i < appUserList.size(); i++) { //把除自己外的本班学生筛选
                        APPUser a = appUserList.get(i);
                        if (user.getUsername().equals(a.getUsername())) {
                            continue;
                        } else {
                            temporrary.add(a);
                            Classmates classmates = new Classmates();
                            classmates.setUserName(a.getUsername());
                            classmates.setHeadImg(getBitmapFromByte(a.getImg_msg()));
                            classmates.setIdentity(a.getIndentity());
                            classmatesList.add(classmates);

                        }


                    }
                    List<Classmates> list1 = classmatesList;
                    classmatesAdapter.updateData(list1);
                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();

                    swipe_refresh.setRefreshing(false);
                } else {

                    Log.e("查询日志：", "msg:" + e.getMessage().toString() + "   code" + e.getErrorCode());
                    Toast.makeText(getContext(), "班级数据获取失败，请刷新一次", Toast.LENGTH_SHORT).show();
                    swipe_refresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {//将图片转化为2进制数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}