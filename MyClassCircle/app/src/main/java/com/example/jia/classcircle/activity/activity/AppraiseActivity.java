package com.example.jia.classcircle.activity.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MyAppraiseRecyclerViewAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AppraiseGroup;
import com.example.jia.classcircle.activity.bmobTable.MyIntentData;
import com.example.jia.classcircle.activity.listener.OnItemClickListener;
import com.example.jia.classcircle.activity.util.BaseFunction;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by Administrator on 2017/10/14.
 */

public class AppraiseActivity extends BaseActivity{
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Button mBtnDel;
    private MyAppraiseRecyclerViewAdapter mAppraiseRecyclerViewAdapter;
    private List<AppraiseGroup> mAppraiseGroupList=new ArrayList<>();
    private boolean firstPressBack = true;
    private String identity="";

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mBtnDel = (Button) findViewById(R.id.btn_del);
        APPUser appUser= BmobUser.getCurrentUser(APPUser.class);
        identity=appUser.getIndentity();

        mAppraiseRecyclerViewAdapter=new MyAppraiseRecyclerViewAdapter(this, mAppraiseGroupList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppraiseRecyclerViewAdapter);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //设置toolbar的返回箭头
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAppraiseRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(AppraiseActivity.this,AppraiseDetailActivity.class);
                intent.putExtra(MyIntentData.APPRAISE_DATA,mAppraiseGroupList.get(position));
                startActivity(intent);
            }
        });


        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(AppraiseActivity.this, "删除中....");
                dialog.show();

                //获得选中的Item
                List<AppraiseGroup> mAppraiseGroup = mAppraiseRecyclerViewAdapter.getSelectedItem();
                if (mAppraiseGroup != null) {
                    List<BmobObject> appraiseList = new ArrayList<BmobObject>();
                    for (int i = 0; i < mAppraiseGroup.size(); i++) {
                        String objectId = mAppraiseGroup.get(i).getObjectId();
                        AppraiseGroup group = new AppraiseGroup();
                        group.setObjectId(objectId);
                        appraiseList.add(group);
                    }

                    List<String> selectPositionList = mAppraiseRecyclerViewAdapter.getSelectTitleArr();
                    for (int i = 0; i < selectPositionList.size(); i++) {
//                        Log.e("SSS", selectPositionList.get(i) + "");
                        for (int k = 0; k < mAppraiseGroupList.size(); k++) {
                            if(mAppraiseGroupList.get(k).getAppraiseTitle().equals(selectPositionList.get(i).toString())){
                                mAppraiseGroupList.remove(k);
                            }

                        }
                    }
                    for(int i=0;i<mAppraiseGroupList.size() ;i++){
                        Log.e("SSS","AAA:"+mAppraiseGroupList.get(i).getAppraiseTitle());
                    }

                    new BmobBatch().deleteBatch(appraiseList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if(e==null){
                                mAppraiseRecyclerViewAdapter.refresh(mAppraiseGroupList);
                                showToast("删除成功");
                                mBtnDel.setVisibility(View.GONE);
                                dialog.dismiss();
                            } else {
                                showToast("删除失败");
                                mBtnDel.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        }
                    });

                } else {
                    showToast("请选择");
                }

            }
        });



    }

    @Override
    protected void initData() {
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            mToolbar.setBackgroundColor(Color.parseColor("#534f4f"));
        }

        getDataFromBmob();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromBmob();
    }

    /**
    * 从bmob中获取评优数据
    */
    private void getDataFromBmob() {
        final APPUser appUser=BmobUser.getCurrentUser(APPUser.class);

        final Dialog dialog= BaseFunction.showProgressDialog(this,"加载中....");
        dialog.show();
        BmobQuery<AppraiseGroup> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<AppraiseGroup>() {
            @Override
            public void done(List<AppraiseGroup> list, BmobException e) {
                if(e==null){
                    List<AppraiseGroup> mList=new ArrayList<AppraiseGroup>();
                    for(int i=0;i< list.size() ;i++){
                        if(appUser.getClassName().equals(list.get(i).getClassName())){
                            mList.add(list.get(i));
                        }
                    }

                    mAppraiseGroupList =mList;
                    mAppraiseRecyclerViewAdapter.refresh(mAppraiseGroupList);
                    dialog.dismiss();
                } else {
                    showToast("加载出错");
                    dialog.dismiss();
                    Log.e("AAA","GET_DATA: "+e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (firstPressBack) {
                return super.onKeyDown(keyCode, event);
            } else {
                mBtnDel.setVisibility(View.GONE);
                mAppraiseRecyclerViewAdapter.setCheckBoxVisible(false);
                firstPressBack = true;
                return true;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(identity.length()==0){
            return super.onCreateOptionsMenu(menu);
        }
        else if(identity.equals("管理员")){
            getMenuInflater().inflate(R.menu.popup_apppreise_menu,menu);
            return true;
        }else if(identity.equals("学生")){
            return super.onCreateOptionsMenu(menu);
        }

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_create_appraise:
                final EditText edtTitle=new EditText(this);
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("输入标题").setView(edtTitle);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title=edtTitle.getText().toString().trim();
                        if(TextUtils.isEmpty(title)){
                            showToast("请输入标题");
                        } else {
                            Intent intent=new Intent(AppraiseActivity.this,AppraiseCreateActivity.class);
                            intent.putExtra(MyIntentData.APPRAISE_TITLE,title);
                            startActivity(intent);
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;

            case R.id.item_delete_appraise:
                firstPressBack = false;
                mBtnDel.setVisibility(View.VISIBLE);
                mAppraiseRecyclerViewAdapter.setCheckBoxVisible(true);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }





}
