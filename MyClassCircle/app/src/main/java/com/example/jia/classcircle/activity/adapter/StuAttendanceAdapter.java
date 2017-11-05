package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.ContentAndTime;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;

import java.util.List;

/**
 * Created by jia on 2017/10/17.
 */

public class StuAttendanceAdapter extends RecyclerView.Adapter<StuAttendanceAdapter.ViewHolder> {//学生签到
    private List<ContentAndTime> attendanceList;
    private List<StuAboutCheck> stuAboutCheckList;//获取它的boolean,如果里面没任何数据，则任何人都还没签到
    private MyOnItemClickListener itemClickListener;
    //private boolean theStuResult;//为了方便查是否签到了
    public StuAttendanceAdapter(List<ContentAndTime> attendanceList,List<StuAboutCheck> stuAboutCheckList){
        this.attendanceList=attendanceList;
        this.stuAboutCheckList=stuAboutCheckList;//在外面，先经过筛选，剩下该人的数据，再丢进来
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_stu_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ContentAndTime contentAndTime=attendanceList.get(position);
        holder.tv_show_manager_attendance_content.setText(contentAndTime.getContent());
        holder.tv_show_manager_attendance_system_time.setText(contentAndTime.getTime());
        if(stuAboutCheckList.size()==0){//里面无人考勤，
            holder.tv_stu_if_attendance.setText("未签到");

        }else {
            for(int i=0;i<stuAboutCheckList.size();i++){
                if(contentAndTime.getTime().equals(stuAboutCheckList.get(i).getManagerTime()) &&
                        stuAboutCheckList.get(i).isIfCheck()==true ){ //在外面
                        holder.tv_stu_if_attendance.setText("已签到");

                }else if( stuAboutCheckList.get(i).isIfCheck()==false) {
                    holder.tv_stu_if_attendance.setText("未签到");
                }
            }


        }
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
        if(attendanceList!=null){
            return attendanceList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_show_manager_attendance_content;
        TextView tv_stu_if_attendance;
        TextView tv_show_manager_attendance_system_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_show_manager_attendance_content= (TextView) itemView.findViewById(R.id.tv_show_manager_attendance_content);
            tv_stu_if_attendance= (TextView) itemView.findViewById(R.id.tv_stu_if_attendance);
            tv_show_manager_attendance_system_time= (TextView) itemView.findViewById(R.id.tv_show_manager_attendance_system_time);

        }
    }

    public void updateData(List<ContentAndTime> attendanceList,List<StuAboutCheck> stuAboutCheckList){
        this.attendanceList=attendanceList;
        this.stuAboutCheckList=stuAboutCheckList;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(MyOnItemClickListener itemClickListener) {//点击
        this.itemClickListener = itemClickListener;
    }


}
