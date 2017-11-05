package com.example.jia.classcircle.activity.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/10/7.
 */

public class BaseFunction {

    /**
     * EditText获取焦点并显示软键盘
     * */
    public static void showSoftInputFromWindow(final Activity activity,EditText edtSpec) {
        edtSpec.setFocusable(true);
        edtSpec.setFocusableInTouchMode(true);
        edtSpec.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }


    public static ProgressDialog showProgressDialog(Context context,String str) {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(str);

        return progressDialog;
    }

    //截取字符串，获取图片名
    public static String getImageNameToDate(String imageHeaderUri) {
        String str=imageHeaderUri.substring(imageHeaderUri.lastIndexOf("/")+1,imageHeaderUri.length());
        return str;
    }

  /*  //从Bmob中下载远程图片
    public static void getImageFromBmob(final ClassGroup mGroup, final ImageView ivHeader) {
        final String imageName= BaseFunction.getImageNameToDate(mGroup.getImageHeaderUri());

        BmobQuery<ClassGroup> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<ClassGroup>() {
            @Override
            public void done(List<ClassGroup> list, BmobException e) {
                if(e==null){
                    BmobFile bmobFile=new BmobFile(imageName,"",mGroup.getImageHeaderUri());
                    File file=new File(Environment.getExternalStorageDirectory(),bmobFile.getFilename());

                    bmobFile.download(file,new DownloadFileListener() {
                        @Override
                        public void done(String savePath, BmobException e) {
                            if(e==null){
                                Bitmap bitmap= BitmapFactory.decodeFile(savePath);
                                ivHeader.setImageBitmap(bitmap);
                                Log.e("AAA","下载成功"+savePath);
                            } else {
                                Log.e("AAA","下载失败"+e.getMessage());
                            }
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });

                } else {
                    Log.e("AAA","find:"+e.getMessage());
                }

            }
        });
    }*/

    /**
     * 从这5种颜色中按顺序拿到一种
     *
     * @param position*/
    public static int getFirstWordColor(int position) {
        int colorNum=0;
        int[] arr={Color.argb(200,255,0,0),Color.argb(120,255,0,0),
                Color.argb(200,0,0,255),Color.argb(150,0,255,0),Color.argb(100,50,50,50)};

        int num=position % arr.length;
        for(int i=0;i<arr.length ;i++){
            if(num==i){
                colorNum=arr[i];
            }
        }
        return colorNum;
    }




}
