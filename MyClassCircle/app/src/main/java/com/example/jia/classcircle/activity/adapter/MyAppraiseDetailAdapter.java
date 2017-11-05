package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.Appraise;
import com.example.jia.classcircle.activity.listener.OnAgreeClickListener;
import com.example.jia.classcircle.activity.util.BaseFunction;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MyAppraiseDetailAdapter extends RecyclerView.Adapter<MyAppraiseDetailViewHolder> {
    private static Context mContext;
    private List<Appraise> mAppraisesList;
    private OnAgreeClickListener mAgreeClickListener;
    private int num;
    private int total;
//    private AppraiseGroup mAppraiseGroup;

    public MyAppraiseDetailAdapter(Context context, List<Appraise> appraiseList, OnAgreeClickListener mAgreeClickListener, int total) {
        this.mContext = context;
        this.mAppraisesList = appraiseList;
        this.mAgreeClickListener = mAgreeClickListener;
        this.total=total;
    }

    @Override
    public MyAppraiseDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_appraise_detail, parent, false);
        MyAppraiseDetailViewHolder holder = new MyAppraiseDetailViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyAppraiseDetailViewHolder holder, final int position) {
        Appraise appraise = mAppraisesList.get(position);
        holder.mTvName.setText(appraise.getAppraisedName());
        holder.mTvIntroduce.setText(appraise.getIntroduce());

        num = appraise.getAgreeNum();
        holder.mTvAgreeNum.setText(num + " 票");

//        total = mAppraiseGroup.getVoteNum();
        if(total==0){
            holder.tv_totalPercent.setText("0%");
            holder.mProgressBar.setProgress(0);
        } else {
            float i = (num / (float) total);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            double percent = (Double.valueOf(decimalFormat.format(i))) * 100;

            Log.e("AAANUM", percent + "    " + "num " + num + "  total  " + total);

            holder.tv_totalPercent.setText(String.valueOf(percent) + "%");
            holder.mProgressBar.setProgress((int) percent);
        }

        getImageFromBmob(appraise, holder.mIvHeader);

        holder.mIvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAgreeClickListener.OnItemAgree(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mAppraisesList == null ? 0 : mAppraisesList.size();
    }


    public void refresh(List<Appraise> list) {
        mAppraisesList = list;
        notifyDataSetChanged();
    }


    //从Bmob中下载远程图片
    public static void getImageFromBmob(final Appraise appraise, final ImageView ivHeader) {
        final String imageName = BaseFunction.getImageNameToDate(appraise.getImageHeader());

        BmobFile bmobFile = new BmobFile(imageName, "", appraise.getImageHeader());
        File file = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());

        bmobFile.download(file, new DownloadFileListener() {
            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                    ivHeader.setImageBitmap(bitmap);

                   /* Glide.with(mContext).load(savePath)
                            .transform(new GlideCircleTransform(mContext))
                            .into(ivHeader);*/

                    Log.e("AAA", "下载成功" + savePath);
                } else {
                    Log.e("AAA", "下载失败" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });

    }


    public void refresh(List<Appraise> list, int i) {
        mAppraisesList = list;
        total = i;
        notifyDataSetChanged();
    }
}
