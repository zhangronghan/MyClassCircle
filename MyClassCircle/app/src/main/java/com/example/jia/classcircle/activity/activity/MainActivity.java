package com.example.jia.classcircle.activity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.fragments.FirstPageFragment;
import com.example.jia.classcircle.activity.fragments.FriendPageFragment;
import com.example.jia.classcircle.activity.fragments.MyPageFragment;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp_mainViewPager;
    private FirstPageFragment firstPageFragment;
    private FriendPageFragment friendPageFragment;
    private MyPageFragment myPageFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mFragmentAdapter;
    private Toolbar toolbar;
    private String studentFunctionName[], managerFunctionName[];
    private int studentFunctionImg[], managerFunctionImg[];
    private APPUser currentUser;//取得当前用户的信息
    private String userIdentity = "";
    private String ClassName = "";
    private boolean ifJointheClass;
    private ProgressDialog wait_dialog;//自动旋转对话框，用于建班提示等待
    private ProgressDialog wait_dialog_to_join;//自动旋转对话框，用于扫码提示等待
    private String ScanResult;//扫描的结果
    //将管理员或已加入班级的学生生成的字符串转化为图片，展示图片
    private List<APPUser> list = new ArrayList<>();//这个是管理员创建班级时，将自己归入到班级表里的学生（复数）中
    private SeekBar seekbar;//亮度条
    private TextView light;//显示百分比数字
    private Button btn_alert_light;
    private int alert_light;
    private int now_light = 0;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //进入首页判断是否为夜间模式(当前状态)
        boolean isNight = SharePreferenceUtil.getNightMode(this);
        AppCompatDelegate.setDefaultNightMode(isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        showFunctionInterface();//第一页展示的是功能页，记得声明2种数组，2个图片数组和2个名称数组，由于管理员与学生拥有不同功能，所以功能名要相应改
        getCurrentAPPUserMsg();
        initView();
        initFragment();
        text_changePage();//点击文本实现颜色变和滑动到下一帧


        int currentIndex=getIntent().getIntExtra("currentIndex",0);
        if(currentIndex == 2){
            vp_mainViewPager.setCurrentItem(currentIndex);
        }


    }


    private void getCurrentAPPUserMsg() {
        currentUser = APPUser.getCurrentUser(APPUser.class);
        if (currentUser != null) {
            userIdentity = (String) APPUser.getObjectByKey("indentity");//获取身份
            ClassName = (String) APPUser.getObjectByKey("ClassName");//获取班级名称
            ifJointheClass = (boolean) APPUser.getObjectByKey("ifJoinClass");//获取是否建立班级
        }
    }

    private void showFunctionInterface() {
        //记得判断身份后，调用正确的展示数组
        studentFunctionName = new String[]{"接收通知", "上传资料", "参与投票", "班级相册", "参与考勤", "班级二维码","朋友圈"};
        managerFunctionName = new String[]{"发布通知", "收集资料", "推选评优", "设计投票", "班级相册", "查看考勤", "班级二维码","朋友圈"};
        studentFunctionImg = new int[]{R.drawable.ic_notice, R.drawable.ic_upload, R.drawable.ic_mainvote,
                R.drawable.ic_photo, R.drawable.ic_check, R.drawable.ic_code,R.drawable.ic_friendcricle};
        managerFunctionImg = new int[]{
                R.drawable.ic_notice, R.drawable.ic_takepart, R.drawable.ic_appraise,
                R.drawable.ic_activity, R.drawable.ic_photo, R.drawable.ic_check, R.drawable.ic_code,R.drawable.ic_friendcricle
        };
    }

    private void text_changePage() {



        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main:
                        vp_mainViewPager.setCurrentItem(0);
                        break;

                    case R.id.rb_friend:
                        vp_mainViewPager.setCurrentItem(1);
                        break;

                    case R.id.rb_me:
                        vp_mainViewPager.setCurrentItem(2);
                        break;

                    default:
                        break;

                }
            }
        });


    }

    private void initFragment() {
        firstPageFragment = new FirstPageFragment();
        //用if语句判断身份，决定展示的网格布局的不同
        if (userIdentity.equals("管理员")) {
            firstPageFragment.setImgs(managerFunctionImg);
            firstPageFragment.setFunctionNames(managerFunctionName);
        } else if (userIdentity.equals("学生")) {
            firstPageFragment.setImgs(studentFunctionImg);
            firstPageFragment.setFunctionNames(studentFunctionName);
        }


        friendPageFragment = new FriendPageFragment();
        myPageFragment = new MyPageFragment();
        mFragmentList.add(firstPageFragment);
        mFragmentList.add(friendPageFragment);
        mFragmentList.add(myPageFragment);
        mFragmentAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        vp_mainViewPager.setOffscreenPageLimit(3);//ViewPager的缓存为4帧
        vp_mainViewPager.setAdapter(mFragmentAdapter);
        vp_mainViewPager.setCurrentItem(0);//初始设置ViewPager选中第一帧

        vp_mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.rb_main);
                        break;

                    case 1:
                        mRadioGroup.check(R.id.rb_friend);
                        break;

                    case 2:
                        mRadioGroup.check(R.id.rb_me);
                        break;

                    default:
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initView() {
        vp_mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_radioGroup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar.setBackgroundColor(Color.parseColor("#423737"));
        } else {
            toolbar.setBackgroundColor(Color.parseColor("#3399FF"));
        }


        wait_dialog = new ProgressDialog(MainActivity.this);
        wait_dialog_to_join = new ProgressDialog(MainActivity.this);
        wait_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        wait_dialog.setMessage("创建中，请稍等...");
        wait_dialog.setIndeterminate(false);// 设置ProgressDialog 的进度条是否不明确
        wait_dialog.setCancelable(false);// 设置ProgressDialog 是否可以按退回按键取消

        wait_dialog_to_join.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait_dialog_to_join.setMessage("加入，请稍等...");
        wait_dialog_to_join.setIndeterminate(false);
        wait_dialog_to_join.setCancelable(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//学生没有创建班级的权限，记得禁掉
        switch (item.getItemId()) {
            case R.id.search:

                startActivity(new Intent(MainActivity.this, SearchActivity.class));


                break;
            case R.id.scan:
                new IntentIntegrator(MainActivity.this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(ScanActivity.class)
                        .initiateScan();
                break;
            case R.id.light_mode:
                //实现调整系统亮度
                AlertDialog.Builder builderLight = new AlertDialog.Builder(this);
                final AlertDialog dialog = builderLight.create();
                View view = View.inflate(this, R.layout.dailog_alert_light, null);
                dialog.setView(view, 0, 0, 0, 0);
                seekbar = (SeekBar) view.findViewById(R.id.seekbar);
                light = (TextView) view.findViewById(R.id.light);
                btn_alert_light = (Button) view.findViewById(R.id.btn_alert_light);
                seekbar.setMax(100);
                seekbar.setProgress(now_light);
                setLight(MainActivity.this, now_light * (255 / 100));
                light.setText("设置屏幕亮度:" + now_light);

                seekbar.setOnSeekBarChangeListener(new SeekBarChangeListenerImp());
                btn_alert_light.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLight(MainActivity.this, alert_light);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.create:
                if (userIdentity.equals("管理员")) {
                    //还得判断该班级是否已经创建
                    getCurrentAPPUserMsg();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("确认创建" + ClassName + "吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wait_dialog.show();
                            //直接判断管理员是否建班。建了则返回，没建则创建
                            if (ifJointheClass) {
                                Toast.makeText(MainActivity.this, "该班级已存在", Toast.LENGTH_SHORT).show();

                                wait_dialog.hide();
                                return;
                            } else {
                                theMethodCreateClass();


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
                } else if (userIdentity.equals("学生")) {
                    Toast.makeText(MainActivity.this, "学生无此权限,请升级为管理员", Toast.LENGTH_SHORT).show();

                }

                break;

            default:
                break;
        }

        return true;
    }

    private void theMethodCreateClass() {
        final AllTheClass createClass = new AllTheClass();
        createClass.setClassName(ClassName);


        //更新管理员状态,记得创建后，要 再调用getCurrentAPPUserMsg()使得管理员状态更改
        APPUser newUser = new APPUser();
        newUser.setIfJoinClass(true);
        BmobUser user = BmobUser.getCurrentUser();
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {//用户信息更新成功才创建这个班级
                    list.add(currentUser);
                    createClass.setStu(list);
                    createClass.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                                wait_dialog.hide();
                            } else {
                                Toast.makeText(MainActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                                wait_dialog.hide();
                            }
                        }
                    });


                } else {
                    Toast.makeText(MainActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    wait_dialog.hide();
                }
            }
        });

    }


    @Override//将图片和文字一起显示在菜单中
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override//扫描二维码功能,扫码加入
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
                return;
            } else {

                ScanResult = intentResult.getContents();//此时获得的是AllTheClass表中的一个班级的id,将其班级名显现

                BmobQuery<AllTheClass> query = new BmobQuery<>();
                query.getObject(ScanResult, new QueryListener<AllTheClass>() {
                    @Override
                    public void done(final AllTheClass allTheClass, BmobException e) {
                        if (e == null) {
                            final String theClassName = allTheClass.getClassName();
                            AlertDialog.Builder if_join_class_dialog = new AlertDialog.Builder(MainActivity.this);
                            if_join_class_dialog.setMessage("是否加入" + theClassName + "班?");
                            ScanResult = theClassName;
                            if_join_class_dialog.setTitle("提示");
                            if_join_class_dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    wait_dialog_to_join.show();
                                    getCurrentAPPUserMsg();
                                    if (ScanResult.equals(currentUser.getClassName())) {


                                        if (currentUser.isIfJoinClass() == false) { //这里判断是否重复加入，应该判断createClass,加入后应该更改其状态
                                            allTheClass.getStu().add(currentUser);//这里应该改为更新，而非save
                                            allTheClass.update(allTheClass.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(MainActivity.this, "扫描成功加入", Toast.LENGTH_LONG).show();//加入后应该更改其状态
                                                        APPUser updateUser = new APPUser();
                                                        updateUser.setIfJoinClass(true);
                                                        BmobUser user = BmobUser.getCurrentUser();
                                                        updateUser.update(user.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(MainActivity.this, "状态更改成功", Toast.LENGTH_LONG).show();
                                                                    wait_dialog_to_join.hide();
                                                                } else {
                                                                    Toast.makeText(MainActivity.this, "状态更改失败", Toast.LENGTH_LONG).show();
                                                                    wait_dialog_to_join.hide();
                                                                }

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(MainActivity.this, "您已经加入过班级", Toast.LENGTH_LONG).show();
                                            wait_dialog_to_join.hide();
                                            return;
                                        }

                                    } else {

                                        Toast.makeText(MainActivity.this, "您不属于该班级，请更改班级或更改您的注册信息", Toast.LENGTH_LONG).show();
                                        wait_dialog_to_join.hide();
                                    }

                                }
                            });
                            if_join_class_dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            if_join_class_dialog.create().show();


                        } else {
                            Toast.makeText(MainActivity.this, "扫描不到，请重新试一次", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wait_dialog.dismiss();
        wait_dialog_to_join.dismiss();
        BmobIM.getInstance().disConnect();//这是即时通讯的，但是我没实现


    }

    private void setLight(Activity context, int brightness) {//设置该APP亮度0-255
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    public void saveBrightness(Activity activity, int brightness) {//设置系统亮度0-255，需要权限

        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        activity.getContentResolver().notifyChange(uri, null);
    }


    public class SeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int cur = seekBar.getProgress();
            float num = 255 / 100;
            alert_light = (int) (cur * num);
            now_light = cur;
            setLight(MainActivity.this, alert_light);
            light.setText("设置屏幕亮度:" + cur);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {


        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}
