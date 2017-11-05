package com.example.jia.classcircle.activity.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.activity.ActivityDetailActivity;
import com.example.jia.classcircle.activity.activity.AppraiseActivity;
import com.example.jia.classcircle.activity.activity.FriendCircleActivity;
import com.example.jia.classcircle.activity.activity.ManagerCheckAttendanceActivity;
import com.example.jia.classcircle.activity.activity.ManagerNoticeActivity;
import com.example.jia.classcircle.activity.activity.ManagerReceiveDataActivity;
import com.example.jia.classcircle.activity.activity.QRcodeActivity;
import com.example.jia.classcircle.activity.activity.SharePhotoActivity;
import com.example.jia.classcircle.activity.activity.StudentAttendanceActivity;
import com.example.jia.classcircle.activity.activity.StudentReceiveNoticeActivity;
import com.example.jia.classcircle.activity.activity.StudentUpLoadDataActivity;
import com.example.jia.classcircle.activity.activity.VoteActivity;
import com.example.jia.classcircle.activity.adapter.FirstPageAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.util.BaseFunction;
import com.example.jia.classcircle.activity.util.DelayUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jia on 2017/9/18.
 */

public class FirstPageFragment extends Fragment {
    private GridView firstPage_grid_view;//实现第一个界面的九宫格功能键按钮
    private int[] imgs;
    private String[] functionNames;

    public FirstPageFragment() {
    }

    private APPUser currentUser;//取得当前用户的信息
    private String userIdentity = "";
    private String ClassName = "";
    private boolean ifJointheClass;
    private FirstPageAdapter adapter;
    private Banner banner;
    //实现轮播图
    int[] playClassCirclePicture = new int[]{
            R.drawable.class1, R.drawable.class2, R.drawable.class3, R.drawable.class4
    };
    /*String titles[] = new String[]{
            "方便作业", "相册共享", "发表意见", "疑问解答"
    };*/
    List list = new ArrayList();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //   scanJoinToClass();//扫描加入班级
        //这里还要实现生成二维码,要判断学生是否已经加入班级，加入了，则允许该学生创建二维码，否则，只能由管理员生成
        initPictureResource();
        getCurrentAPPUserMsg();
        View v = inflater.inflate(R.layout.fragment_first_page, container, false);
        firstPage_grid_view = (GridView) v.findViewById(R.id.gv_show_function);
        banner = (Banner) v.findViewById(R.id.id_banner);
        setBanner();

        adapter = new FirstPageAdapter(getContext(), imgs, functionNames);
        firstPage_grid_view.setAdapter(adapter);
        firstPage_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCurrentAPPUserMsg();//点击时，及时更新状态

                if (userIdentity.equals("管理员")) {
                    if (ifJointheClass == false) {
                        Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position == 0) { //第一个功能点（发布通知）
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        startActivity(new Intent(getContext(), ManagerNoticeActivity.class));

                    }
                    if (position == 1) { //收集资料
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), ManagerReceiveDataActivity.class));
                    }
                    if (position == 2) {//推选评优活动
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), AppraiseActivity.class));


                    }
                    if (position == 3) {//设计活动投票
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), VoteActivity.class));

                    }


                    if (position == 4) {
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {//班级相册
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        startActivity(new Intent(getContext(), SharePhotoActivity.class));
                    }
                    if (position == 5) {//查看考勤
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {//班级相册
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), ManagerCheckAttendanceActivity.class));
                    }

                    if (position == 6) {//如果是第七个功能点（生成二维码）

                        if (DelayUtils.isFastClick()) {
                            Toast.makeText(getContext(), "请勿重复点击，请稍等", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (ifJointheClass) { //如果管理员未建班，则提醒先创建班级

                            String ManagerClassName = currentUser.getClassName();//获取管理员管理班级的班级名,由于识别不了中文，所以使用objectID来判断,这时应该传递班级表ID

                            BmobQuery<AllTheClass> bmobQuery = new BmobQuery<AllTheClass>(); //调用查询语句，获取管理员在班级表中该班级的objectID
                            bmobQuery.addWhereEqualTo("className", ManagerClassName);

                            bmobQuery.findObjects(new FindListener<AllTheClass>() {
                                @Override
                                public void done(List<AllTheClass> list, BmobException e) {
                                    if (e == null) {
                                        String tableClassID = list.get(0).getObjectId();
                                        Intent intent = new Intent();//传递到生成二维码的新activity
                                        intent.putExtra("theClassNameID", tableClassID);
                                        intent.setClass(getContext(), QRcodeActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(getContext(), "创建失败，请再重试一次", Toast.LENGTH_SHORT).show();
                                        // return;

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "未创建班级，请先创建班级", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    }
                    if (position == 7) {
                        startActivity(new Intent(getContext(), FriendCircleActivity.class));
                    }


                } else if (userIdentity.equals("学生")) {
                    if (position == 0) { //接收通知，判断是否加入班级了，未加入则无法调用该功能
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), StudentReceiveNoticeActivity.class));


                    }
                    if (position == 1) { //上传资料
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), StudentUpLoadDataActivity.class));
                    }
                    if (position == 2) { //参与投票
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), ActivityDetailActivity.class));
                    }
                    if (position == 3) {//班级相册，先判断是否加入班级
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), SharePhotoActivity.class));
                    }
                    if (position == 4) {
                        if (DelayUtils.isFastClick()) {//防止控件在一秒内重复点击生效
                            return;
                        }
                        if (ifJointheClass == false) {
                            Toast.makeText(getContext(), "未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getContext(), StudentAttendanceActivity.class));


                    }
                    if (position == 5) {
                        //继续判断该学生是否加入了班级，已加入才能生成二维码
                        if (DelayUtils.isFastClick()) {
                            Toast.makeText(getContext(), "请勿重复点击，请稍等", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentUser.isIfJoinClass() == true) {

                            final Dialog dialog = BaseFunction.showProgressDialog(getContext(), "");
                            dialog.show();

                            String stringToQRcode = currentUser.getClassName();//获取学生的班级名,用查询获取班级表的id
                            BmobQuery<AllTheClass> bmobQuery = new BmobQuery<AllTheClass>(); //调用查询语句，获取管理员在班级表中该班级的objectID
                            bmobQuery.addWhereEqualTo("className", stringToQRcode);

                            bmobQuery.findObjects(new FindListener<AllTheClass>() {
                                @Override
                                public void done(List<AllTheClass> list, BmobException e) {

                                    if (e == null) {
                                        Intent intent = new Intent();//传递到生成二维码的新activity
                                        intent.putExtra("theClassNameID", list.get(0).getObjectId());//将objectID传递过去
                                        intent.setClass(getContext(), QRcodeActivity.class);
                                        dialog.hide();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), "创建失败", Toast.LENGTH_SHORT).show();
                                        dialog.hide();
                                    }

                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "还未加入班级，请先加入班级", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    if (position == 6) {
                        startActivity(new Intent(getContext(), FriendCircleActivity.class));
                    }

                }
            }
        });

        return v;

    }

    private void setBanner() {
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);
        banner.setIndicatorGravity(Banner.CENTER);//Banner.CENTER 指示器居中
        //banner.setBannerTitle(titles); //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        banner.isAutoPlay(true);//设置是否自动轮播（不设置则默认自动）
        banner.setDelayTime(3000);
        banner.setImages(list, new Banner.OnLoadImageListener() {

            @Override
            public void OnLoadImage(ImageView view, Object url) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getContext()).load(url).into(view);
            }
        });
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {

            @Override
            public void OnBannerClick(View view, int position) {//这是点击事件
                if(currentUser.isIfJoinClass()){
                    startActivity(new Intent(getContext(),SharePhotoActivity.class));
                }else {
                    Toast.makeText(getContext(),"未加入班级,获取不到班级相册，请先加入班级",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initPictureResource() {
        for (int i = 0; i < playClassCirclePicture.length; i++) {
            list.add(playClassCirclePicture[i]);
        }

    }


    public int[] getImgs() {
        return imgs;
    }

    public void setImgs(int[] imgs) {
        this.imgs = imgs;
    }

    public String[] getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(String[] functionNames) {
        this.functionNames = functionNames;
    }

    private void getCurrentAPPUserMsg() {

        currentUser = APPUser.getCurrentUser(APPUser.class);
        if (currentUser != null) {
            userIdentity = (String) APPUser.getObjectByKey("indentity");//获取身份
            ClassName = (String) APPUser.getObjectByKey("ClassName");//获取班级名称
            ifJointheClass = (boolean) APPUser.getObjectByKey("ifJoinClass");//获取是否建立班级
        }
    }
}
