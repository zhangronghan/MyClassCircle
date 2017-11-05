package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.AllNoticeMsg;

import java.util.List;

/**
 * Created by jia on 2017/10/11.
 */

public class ShowNoticeMsgAdapter extends RecyclerView.Adapter<ShowNoticeMsgAdapter.ViewHolder>{
    private List<AllNoticeMsg> noticeList;
    public ShowNoticeMsgAdapter(List<AllNoticeMsg> noticeList){
        this.noticeList=noticeList;

    }
    static class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView img_notice;
        TextView  tv_show_notice_content;
        TextView  tv_show_system_time;
       public ViewHolder(View itemView) {
           super(itemView);
           img_notice= (ImageView) itemView.findViewById(R.id.img_notice_managerHeadView);
           tv_show_notice_content= (TextView) itemView.findViewById(R.id.tv_show_notice_content);
           tv_show_system_time= (TextView) itemView.findViewById(R.id.tv_show_system_time);
       }
   }


    @Override
    public ShowNoticeMsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.show_notice_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShowNoticeMsgAdapter.ViewHolder holder, int position) {
        AllNoticeMsg allNoticeMsg=noticeList.get(position);
        holder.tv_show_notice_content.setText(allNoticeMsg.getNoticeContent());
        holder.tv_show_system_time.setText(allNoticeMsg.getNoticeTime());

    }

    @Override
    public int getItemCount() {
        if(noticeList!=null){
            return noticeList.size();
        }
        return 0;
    }
    public void updateDate(List<AllNoticeMsg> noticeList ){//实现数据更新用,用下拉刷新实现,点击搜索实现
        this.noticeList=noticeList;
        notifyDataSetChanged();

    }
}
