package com.example.jia.classcircle.activity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.FriendCircleListViewAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.FriendCircle;
import com.example.jia.classcircle.activity.listener.OnListViewClickListener;
import com.example.jia.classcircle.activity.util.BaseFunction;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;

import org.json.JSONException;
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

/**
 * Created by Administrator on 2017/11/3.
 */

public class FriendCircleActivity extends BaseActivity implements OnListViewClickListener {
    private Toolbar mToolbar;
    private FriendCircleListViewAdapter mAdapter;
    private ListView mListView;
    private LinearLayout mLinearLayoutSend;
    private EditText mEdtComment;
    private Button mBtnSend;
    private List<FriendCircle> mFriendCircleList;
    private Dialog mDialog;
    private int index;
    private boolean isDisplayEditText = false;
    private APPUser appUser;
    private List<String> mStringList;
    private BmobRealTimeData bmobRealTimeData;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_friend_cricle;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ListView) findViewById(R.id.listView);
        mLinearLayoutSend = (LinearLayout) findViewById(R.id.linearLayout_send);
        mEdtComment = (EditText) findViewById(R.id.edt_comment);
        mBtnSend = (Button) findViewById(R.id.btn_send);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEdtComment.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    String MyComment = appUser.getUsername() + "#" + comment;

                    final FriendCircle friendCircle = mFriendCircleList.get(index);
                    mStringList = friendCircle.getStringList();

                    if (mStringList == null) {
                        mStringList = new ArrayList<>();
                    }
                    mStringList.add(MyComment);

                    friendCircle.setStringList(mStringList);

                    friendCircle.update(friendCircle.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                showToast("评论成功");
//                                mStringList = friendCircle.getStringList();

                                mEdtComment.setText("");
                                mLinearLayoutSend.setVisibility(View.GONE);
                                mEdtComment.setFocusable(false);
                                isDisplayEditText = !isDisplayEditText;

                            } else {
                                showToast("评论失败");
                            }

                        }
                    });


                } else {
                    showToast("内容不能为空");
                }
            }
        });

    }

    @Override
    protected void initData() {
        appUser = BmobUser.getCurrentUser(APPUser.class);

        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            mToolbar.setBackgroundColor(Color.parseColor("#534f4f"));
        }


        mDialog = BaseFunction.showProgressDialog(FriendCircleActivity.this, "");

        bmobRealTimeData=new BmobRealTimeData();
        syncData();


        mAdapter = new FriendCircleListViewAdapter(FriendCircleActivity.this, mFriendCircleList, this);

        View headerView = View.inflate(this, R.layout.item_header, null);
        ImageView ivHeader= (ImageView) headerView.findViewById(R.id.iv_header);
        TextView tvName= (TextView) headerView.findViewById(R.id.tv_name);
        tvName.setText(appUser.getUsername());

        mListView.addHeaderView(headerView);
        mListView.setAdapter(mAdapter);

        //读取数据
        getDataFromBmob();

        byte[] bytes=appUser.getImg_msg();
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        ivHeader.setImageBitmap(bitmap);
    }

    private void syncData() {
        bmobRealTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if (e == null && bmobRealTimeData.isConnected()) {
                    bmobRealTimeData.subTableUpdate("FriendCircle");
                    Log.e("AAA", "数据实时同步连接成功");
                } else {
                    Log.e("AAA", "数据实时同步连接失败");
                }

            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    try {
                        String content=data.getString("mStringList");
                        String str=content.substring(1,content.length()-1);
                        String[] arr=str.split(",");

                        List<String> strings=new ArrayList<String>();
                        for(int i=0;i<arr.length ;i++){
                            String str1=arr[i].substring(1,arr[i].length()-1);
                            strings.add(str1);
                        }
                        mAdapter.refreshChild(strings,index);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

    }

    /**
     * 从bmob中获取数据
     * */
    private void getDataFromBmob() {
        mFriendCircleList=new ArrayList<>();

        mDialog.show();

        BmobQuery<FriendCircle> bmobQuery = new BmobQuery<>();

        bmobQuery.addWhereEqualTo("className", appUser.getClassName());
        bmobQuery.setLimit(50);

        bmobQuery.findObjects(new FindListener<FriendCircle>() {
            @Override
            public void done(List<FriendCircle> list, BmobException e) {
                if (e == null) {
                    mDialog.hide();
//                    mFriendCircleList = list;
                    for(int i=list.size()-1;i>=0 ;i--){
                        mFriendCircleList.add(list.get(i));
                    }

                    mAdapter.refresh(mFriendCircleList);
                } else {
                    mDialog.hide();
                    Log.e("AAA", "读取数据出错" + e.getMessage() + "   " + e.getErrorCode());
                    //showToast("读取数据出错");
                }
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDataFromBmob();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_friendcricle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_publish) {
            startActivity(new Intent(FriendCircleActivity.this, PublishActivity.class));
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();

        bmobRealTimeData.unsubTableUpdate("FriendCircle");
    }

    @Override
    public void onCommentClick(int position) {
        if (isDisplayEditText) {
            mLinearLayoutSend.setVisibility(View.GONE);
            mEdtComment.setFocusable(false);
            isDisplayEditText = !isDisplayEditText;
        } else {
            mLinearLayoutSend.setVisibility(View.VISIBLE);
            isDisplayEditText = !isDisplayEditText;
            mEdtComment.setFocusable(true);
            mEdtComment.setFocusableInTouchMode(true);
            mEdtComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            index = position;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDisplayEditText) {
                mLinearLayoutSend.setVisibility(View.GONE);
                mEdtComment.setFocusable(false);
                isDisplayEditText = !isDisplayEditText;
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
