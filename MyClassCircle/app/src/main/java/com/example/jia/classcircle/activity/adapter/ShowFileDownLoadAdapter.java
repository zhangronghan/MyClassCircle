package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeFile;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jia on 2017/10/11.
 */

public class ShowFileDownLoadAdapter extends RecyclerView.Adapter<ShowFileDownLoadAdapter.ViewHolder> {//展示文件下载
    private List<AllNoticeFile> filePathList;
    private MyOnItemClickListener itemClickListener;

    public  ShowFileDownLoadAdapter(List<AllNoticeFile> filePathList){

        this.filePathList=filePathList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_show_down_load_file_from_manager;//展示文件路径
        ImageView img_show_down_load_file;//点击下载
        TextView tv_show_file_system_time;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_show_down_load_file_from_manager= (TextView) itemView.findViewById(R.id.tv_show_down_load_file_from_manager);
            tv_show_file_system_time= (TextView) itemView.findViewById(R.id.tv_show_file_system_time);
            img_show_down_load_file= (ImageView) itemView.findViewById(R.id.img_show_down_load_file);

        }
    }

    @Override
    public ShowFileDownLoadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_down_load_file_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ShowFileDownLoadAdapter.ViewHolder holder, final int position) {
        //指定下载时
        String fileName=filePathList.get(position).getNoticeFileUrl();
        String [] strArry=fileName.split("\\/");
        int last=strArry.length;//获取分隔符最后一个，取得文件名
        holder.tv_show_down_load_file_from_manager.setText(strArry[last-1]);//展示文件名，先用split照"/"划分，然后取最后一个
        holder.tv_show_file_system_time.setText(filePathList.get(position).getNoticeTime());
        if(itemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClickListener(holder.itemView,holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(filePathList!=null){
            return filePathList.size();
        }
        return 0;
    }
    public void updateDate(List<AllNoticeFile> filePathList){
        this.filePathList=filePathList;
        notifyDataSetChanged();

    }
    public void setOnItemClickListener(MyOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
