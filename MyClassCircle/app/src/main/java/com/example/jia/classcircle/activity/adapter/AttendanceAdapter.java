package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jia.classcircle.R;

import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;
import com.example.jia.classcircle.activity.listener.MyOnItemLongClickListener;

import java.util.List;

/**
 * Created by jia on 2017/10/17.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {//管理员考勤
    private List<ContentAndTime> attendanceList;
    private MyOnItemClickListener itemClickListener;
    private MyOnItemLongClickListener itemLongClickListener;
    public AttendanceAdapter(List<ContentAndTime> attendanceList){
        this.attendanceList=attendanceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_notice_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
           String  content=attendanceList.get(position).getContent();
           String  time=attendanceList.get(position).getTime();
           holder.tv_show_attendance_content.setText(content);
           holder.tv_show_attendance_system_time.setText(time);

           if(itemClickListener!=null){
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       itemClickListener.OnItemClickListener(holder.itemView,holder.getLayoutPosition());
                   }
               });
           }
           if(itemLongClickListener!=null){
               holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       itemLongClickListener.OnItemLongClickListener(holder.itemView,holder.getLayoutPosition());
                       return true;
                   }
               });
           }
    }

    @Override
    public int getItemCount() {
        if(attendanceList!=null){
            return attendanceList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_show_attendance_content;
        TextView tv_show_attendance_system_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_show_attendance_content= (TextView) itemView.findViewById(R.id.tv_show_attendance_content);
            tv_show_attendance_system_time= (TextView) itemView.findViewById(R.id.tv_show_attendance_system_time);
        }
    }
    public void updateDate(List<ContentAndTime> attendanceList){
        this.attendanceList=attendanceList;
        notifyDataSetChanged();

    }
    public void setOnItemClickListener(MyOnItemClickListener itemClickListener) {//点击
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(MyOnItemLongClickListener itemLongClickListener) {//长按点击
        this.itemLongClickListener = itemLongClickListener;
    }

}
