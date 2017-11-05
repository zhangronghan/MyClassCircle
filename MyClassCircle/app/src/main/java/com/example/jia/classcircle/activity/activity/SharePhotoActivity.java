package com.example.jia.classcircle.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.CommentPictureMsgAdapter;
import com.example.jia.classcircle.activity.adapter.GridViewAboutPhotoAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.GetClientsTalk;
import com.example.jia.classcircle.activity.bmobTable.PhotoAndComment;
import com.example.jia.classcircle.activity.bmobTable.PhotoImgUrl;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.youth.banner.Banner;

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

public class SharePhotoActivity extends AppCompatActivity {//实现班级相册

    private Toolbar toolbar_sharePhoto;
    private Banner banner_showPhoto;
    private RecyclerView recyclerView_talk_about_photo;
    private EditText edt_talk_about_photo;
    private Button btn_send_msg;
    private APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private ProgressDialog wait_dialog;
    private ProgressDialog wait_dialog2;
    private List<String> getInternetImgUrl=new ArrayList<>();
    private List<String> getInternetImgUrlID=new ArrayList<>();//用来存删除的ID
    private List<PhotoAndComment> commentList=new ArrayList<>();
    private List<GetClientsTalk> getClientsSpeakContentsList=new ArrayList<>();
    private CommentPictureMsgAdapter commentPictureMsgAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_photo);
        initData();
        initGetComment();//获取该班级的评论
        initView();
        onClickEvent();
        monitoringData();//监听数据变化

    }


    private void initGetComment() {
        BmobQuery<GetClientsTalk> queryChat=new BmobQuery<>(); //实现获取用户评价图片内容
        queryChat.setLimit(500);
        queryChat.addWhereEqualTo("className",user.getClassName());
        queryChat.findObjects(new FindListener<GetClientsTalk>() {
            @Override
            public void done(List<GetClientsTalk> list, BmobException e) {
                if(e==null){
                    getClientsSpeakContentsList=list;
                    setAdapter(getClientsSpeakContentsList);
                }
            }
        });



    }

    private void monitoringData() {
        final BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(rtd.isConnected()){
                    rtd.subTableUpdate("PhotoAndComment");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    String url= (String) data.opt("photo");
                    String className=(String) data.opt("className");


                    //获取的班级名一致。user
                    if(url!=null){
                        if(url.length()>0 && className.equals(user.getClassName())){
                            getInternetImgUrl.add(url);
                            startBanner(getInternetImgUrl);
                        }
                    }
                }
            }
        });

        final BmobRealTimeData rtd2 = new BmobRealTimeData();
        rtd2.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(rtd2.isConnected()){
                    rtd2.subTableUpdate("GetClientsTalk");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    String className=(String) data.opt("className");
                    String name= (String) data.opt("userName");
                    String speakContent=(String) data.opt("speakContent");
                    String  speakTime=(String) data.opt("speakTime");
                    if(className.equals(user.getClassName())){//班级名称一样的才允许存入该言论
                        GetClientsTalk content=new GetClientsTalk();
                        content.setClassName(className);
                        content.setUserName(name);
                        content.setSpeakContent(speakContent);
                        content.setSpeakTime(speakTime);
                        getClientsSpeakContentsList.add(content);
                        commentPictureMsgAdapter.updateData(getClientsSpeakContentsList);
                        recyclerView_talk_about_photo.scrollToPosition(getClientsSpeakContentsList.size()-1);//将消息定位到最后一行

                    }

                }
            }
        });
        final BmobRealTimeData rtd3 = new BmobRealTimeData();//监听照片表删除,没实现阿
        rtd3.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(rtd3.isConnected()){
                    rtd3.subTableDelete("PhotoAndComment");
                    Log.e("imgurl","删除监听");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if(BmobRealTimeData.ACTION_DELETETABLE.equals(jsonObject.optString("action"))){
                    JSONObject data = jsonObject.optJSONObject("data");
                    String url= (String) data.opt("photo");
                    String className=(String) data.opt("className");
                    //获取的班级名一致。user
                    if(url!=null){
                        if(url.length()>0 && className.equals(user.getClassName())){
                            Log.e("imgurl",url);
                           // getInternetImgUrl.remove(url);

                            startBanner(getInternetImgUrl);
                        }
                    }
                }
            }
        });

    }


    private void initData() {
        wait_dialog2=new ProgressDialog(SharePhotoActivity.this);
        wait_dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog2.setMessage("获取数据中...");
        wait_dialog2.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog2.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        wait_dialog2.show();

        BmobQuery<PhotoAndComment> query=new BmobQuery<>();//实现
        query.setLimit(500);//bug:超过500就GG,由于里面存了评论和用户二进制头像，导致取数据国语庞大，无法实现展现数据，所以用户头像不传了
        query.addWhereEqualTo("className",user.getClassName());
        query.findObjects(new FindListener<PhotoAndComment>() {
            @Override
            public void done(List<PhotoAndComment> list, BmobException e) {
                if(e==null){
                    commentList=list;
                    for(int i=0;i<list.size();i++){//这里获取的是所有网络图片的路径
                        if(list.get(i).getPhoto().length()>0){
                            String url=list.get(i).getPhoto();
                            getInternetImgUrl.add(url);
                            getInternetImgUrlID.add(commentList.get(i).getObjectId());
                        }
                    }
                    startBanner(getInternetImgUrl);


                    wait_dialog2.hide();
                }else {
                    wait_dialog2.hide();
                    Toast.makeText(SharePhotoActivity.this,"获取不到班级相册的数据，请重新进入"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void setAdapter(List<GetClientsTalk> getClientsSpeakContentsList) {
        commentPictureMsgAdapter=new CommentPictureMsgAdapter(getClientsSpeakContentsList);
        recyclerView_talk_about_photo.setAdapter(commentPictureMsgAdapter);
    }

    private void startBanner(final List<String> getInternetImgUrl) {
        this.getInternetImgUrl=getInternetImgUrl;
        banner_showPhoto.setImages(getInternetImgUrl,new Banner.OnLoadImageListener(){

            @Override
            public void OnLoadImage(ImageView view, Object url) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getApplicationContext()).load(url).into(view);
            }
        });

            banner_showPhoto.setOnBannerClickListener(new Banner.OnBannerClickListener(){

                @Override
                public void OnBannerClick(View view, int position) { //这是点击事件

                    showPicture(position);




                }
            });
      //  }
    }

    private void showPicture(final int position) {
        //自定义布局,弹出删除
        AlertDialog.Builder builder=new AlertDialog.Builder(SharePhotoActivity.this);
        final AlertDialog dialog=builder.create();
        View getDeleteView=View.inflate(SharePhotoActivity.this,R.layout.dialog_delete_photo,null);
        dialog.setView(getDeleteView ,0, 0, 0, 0);
        GridView show_detail_gridView_photo= (GridView) getDeleteView.findViewById(R.id.show_detail_gridView_photo);
        GridViewAboutPhotoAdapter aboutPhotoAdapter=new GridViewAboutPhotoAdapter(SharePhotoActivity.this,getInternetImgUrl);
        show_detail_gridView_photo.setAdapter(aboutPhotoAdapter);
        show_detail_gridView_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //九宫格图片显示，将其放大，手势滑动达到一张张显示
                Intent intent=new Intent(SharePhotoActivity.this,ShowPictureDetailActivity.class);
                PhotoImgUrl url=new PhotoImgUrl();
                url.setImgUrl(getInternetImgUrl);
                url.setGetInternetImgUrlID(getInternetImgUrlID);
                url.setPosition(position);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        CardView card_delete= (CardView) getDeleteView.findViewById(R.id.card_delete);
        ImageView img_delete= (ImageView) getDeleteView.findViewById(R.id.img_delete);
        ImageView img_close=(ImageView) getDeleteView.findViewById(R.id.img_close);
        Button btn_delete_sure= (Button) getDeleteView.findViewById(R.id.btn_delete_sure);

        if(user.getIndentity().equals("学生")){
            card_delete.setVisibility(View.INVISIBLE);
            img_delete.setVisibility(View.INVISIBLE);
            btn_delete_sure.setVisibility(View.INVISIBLE);
        }

        Glide.with(getApplicationContext()).load(getInternetImgUrl.get(position-1)).into(img_delete);//将取到的的图片展示
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_delete_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getIndentity().equals("管理员")){
                    Toast.makeText(SharePhotoActivity.this,"管理员删除",Toast.LENGTH_SHORT).show();
                    PhotoAndComment photoAndComment=new PhotoAndComment();
                    photoAndComment.setObjectId(getInternetImgUrlID.get(position-1));


                    photoAndComment.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                getInternetImgUrl.remove(position-1);
                                getInternetImgUrl.clear();
                                initData();
                                Toast.makeText(SharePhotoActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                }
            }
        });


        dialog.show();
    }


    private void onClickEvent() {
        toolbar_sharePhoto.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_send_msg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wait_dialog.show();
                String content=edt_talk_about_photo.getText().toString();
                if(content.length()==0){
                    wait_dialog.hide();
                    Toast.makeText(SharePhotoActivity.this,"未输入评论",Toast.LENGTH_SHORT).show();
                    return;
                }
                GetClientsTalk clientsSpeakContent=new GetClientsTalk();
                clientsSpeakContent.setSpeakContent(content);
                clientsSpeakContent.setUserName(user.getUsername());
                clientsSpeakContent.setClassName(user.getClassName());
                clientsSpeakContent.setSpeakTime(getSystemTime());
                clientsSpeakContent.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            edt_talk_about_photo.setText("");
                            wait_dialog.hide();
                            Toast.makeText(SharePhotoActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                        }else {
                            wait_dialog.hide();
                            Toast.makeText(SharePhotoActivity.this,"评论失败，请重试一次",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }

    private void initView() {
        wait_dialog=new ProgressDialog(SharePhotoActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("评论中");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消
        toolbar_sharePhoto= (Toolbar) findViewById(R.id.toolbar_sharePhoto);


        setSupportActionBar(toolbar_sharePhoto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar的返回箭头

        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            toolbar_sharePhoto.setBackgroundColor(Color.parseColor("#534f4f"));
        }

        banner_showPhoto= (Banner) findViewById(R.id.banner_showPhoto);
        recyclerView_talk_about_photo= (RecyclerView) findViewById(R.id.recyclerView_talk_about_photo);
        edt_talk_about_photo= (EditText) findViewById(R.id.edt_talk_about_photo);
        btn_send_msg= (Button) findViewById(R.id.btn_send_msg);

        banner_showPhoto.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);
        banner_showPhoto.setIndicatorGravity(Banner.CENTER);//Banner.CENTER 指示器居中

        banner_showPhoto.isAutoPlay(true) ;//设置是否自动轮播（不设置则默认自动）
        banner_showPhoto.setDelayTime(3000);

        LinearLayoutManager layoutManager=new LinearLayoutManager(SharePhotoActivity.this);
        recyclerView_talk_about_photo.setLayoutManager(layoutManager);
        commentPictureMsgAdapter=new CommentPictureMsgAdapter(getClientsSpeakContentsList);
        recyclerView_talk_about_photo.setAdapter(commentPictureMsgAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_photo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.up_load_photo:
                //传递到新界面
                startActivity(new Intent(SharePhotoActivity.this,ChoosePhotoUpLoadActivity.class));
                break;
            default:
                break;
        }
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
        wait_dialog2.dismiss();
    }
}
