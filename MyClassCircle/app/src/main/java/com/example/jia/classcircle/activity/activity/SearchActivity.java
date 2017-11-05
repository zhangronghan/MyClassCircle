package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MyDividerItemDecoration;
import com.example.jia.classcircle.activity.adapter.SearchClassAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class SearchActivity extends AppCompatActivity {//这是查询界面，实现查找加入，但是不能删除更改，如果是管理员，则不能加入，只能查看
    private EditText et_inputClassName;
    private ImageView iv_search;
    private RecyclerView recyclerView_showClass;
    private Button btn_class_return;
    private SearchClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AllTheClass> allClassList=new ArrayList<>();
    private ProgressDialog wait_dialog;//自动旋转对话框，用于提示等待
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();//初始化，获取班级表的所有数据
        initView();
        wait_dialog.show();
    }

    private void initData() {
        BmobQuery<AllTheClass> query=new BmobQuery<AllTheClass>();
        query.addQueryKeys("objectID,className");
        query.addWhereExists("className");//询某个列的值存在
        query.setLimit(100);
        query.findObjects(new FindListener<AllTheClass>() {
            @Override
            public void done(List<AllTheClass> list, BmobException e) {
                if(e==null){

                    allClassList= (ArrayList<AllTheClass>) list;
                    Toast.makeText(SearchActivity.this,"共有"+allClassList.size()+"个班级",Toast.LENGTH_SHORT).show();
                    mAdapter.updateDate(allClassList);
                    wait_dialog.hide();
                }else {
                    Toast.makeText(SearchActivity.this,"请刷新重试下",Toast.LENGTH_SHORT).show();
                    wait_dialog.hide();
                }
            }
        });
    }

    private void initView() {
        et_inputClassName= (EditText) findViewById(R.id.et_inputClassName);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        recyclerView_showClass= (RecyclerView) findViewById(R.id.recyclerView_showClass);
        btn_class_return= (Button) findViewById(R.id.btn_class_return);
        recyclerView_showClass.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        wait_dialog=new ProgressDialog(SearchActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消

        mAdapter=new SearchClassAdapter(allClassList);
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView_showClass.setLayoutManager(mLayoutManager);
        recyclerView_showClass.setAdapter(mAdapter);


        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击查询(模糊查询需要付费,所以做了全信息匹配才能查询)
                wait_dialog.show();
                String resultSearch=  et_inputClassName.getText().toString().trim();
                if(resultSearch.length()==0){
                    Toast.makeText(SearchActivity.this,"输入为空，默认查询所有班级",Toast.LENGTH_SHORT).show();
                    initData();


                    wait_dialog.hide();
                    return;
                }
                BmobQuery<AllTheClass> query=new BmobQuery<AllTheClass>();
                query.addWhereEqualTo("className",resultSearch);
                query.findObjects(new FindListener<AllTheClass>() {
                    @Override
                    public void done(List<AllTheClass> list, BmobException e) {
                        if(e==null){
                            if(list.size()==0){
                                Toast.makeText(SearchActivity.this,"查询无此班，请重新输入",Toast.LENGTH_SHORT).show();
                                et_inputClassName.setText("");
                                wait_dialog.hide();
                            }
                            else {
                                allClassList= (ArrayList<AllTheClass>) list;
                                mAdapter.updateDate(allClassList);
                                et_inputClassName.setText("");

                                wait_dialog.hide();
                            }

                        }else {
                            Toast.makeText(SearchActivity.this,"查询失败，请重新查询",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                        }
                    }
                });




            }
        });
        btn_class_return.setOnClickListener(new View.OnClickListener() {//点击返回
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, final int position) {

                //点击出现对话框，如果是管理员则无法加入，判断要加入的班级是否与当前用户的班级一致，,判断是否已经加入，若没加入，则加入并更改状态，已加入则提示
                AlertDialog.Builder builder=new AlertDialog.Builder(SearchActivity.this);
                builder.setMessage("确认加入"+allClassList.get(position).getClassName()+"班吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wait_dialog.show();
                        final APPUser user=  APPUser.getCurrentUser(APPUser.class);
                        if(user.isIfJoinClass()==true){
                            Toast.makeText(SearchActivity.this,"您已经加入过班级了",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        }else if(user.getIndentity().equals("管理员")){
                            Toast.makeText(SearchActivity.this,"您是管理员，无需加入班级",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        }else if(user.getClassName().equals(allClassList.get(position).getClassName())==false){
                            Toast.makeText(SearchActivity.this,"您并非该班级成员",Toast.LENGTH_SHORT).show();
                            wait_dialog.hide();
                            return;
                        }
                        final APPUser updateUser=new APPUser();
                        updateUser.setIfJoinClass(true);

                        updateUser.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(SearchActivity.this,"状态更改成功",Toast.LENGTH_LONG).show();
                                    BmobQuery<AllTheClass> query=new BmobQuery<AllTheClass>();
                                    query.addWhereEqualTo("className",user.getClassName());
                                    query.findObjects(new FindListener<AllTheClass>() {
                                        @Override
                                        public void done(List<AllTheClass> list, BmobException e) {
                                            if(e==null){
                                                list.get(0).getStu().add(user);//将当前用户加入到班级表的stuList,然后再更新
                                                list.get(0).update(list.get(0).getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        Toast.makeText(SearchActivity.this,"成功加入",Toast.LENGTH_LONG).show();
                                                        wait_dialog.hide();
                                                    }
                                                });

                                            }
                                            wait_dialog.hide();
                                        }
                                    });

                                    //改更状态后，再加入班级表
                                    //wait_dialog_to_join.hide();
                                }else {
                                    Toast.makeText(SearchActivity.this,"状态更改失败",Toast.LENGTH_LONG).show();
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
}
