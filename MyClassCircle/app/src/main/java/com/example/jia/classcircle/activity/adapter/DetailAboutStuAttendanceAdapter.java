package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.StuAboutCheck;

import java.util.List;

/**
 * Created by jia on 2017/10/18.
 */

public class DetailAboutStuAttendanceAdapter extends RecyclerView.Adapter<DetailAboutStuAttendanceAdapter.ViewHolder> {
    private List<StuAboutCheck> stuAboutCheckList;//在外面晒出与这次考勤有关的，再丢进来


    public DetailAboutStuAttendanceAdapter(List<StuAboutCheck> stuAboutCheckList){
        this.stuAboutCheckList=stuAboutCheckList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_stu_show_for_manager_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StuAboutCheck stuAboutCheck=stuAboutCheckList.get(position);
        holder.tv_show_stuName_attendance.setText("学生名:"+stuAboutCheck.getStuName());
        if(stuAboutCheck.isIfCheck()==true){
            holder.tv_stu_if_attendance_result.setText("已签到");
        }else if(stuAboutCheck==null){
            holder.tv_stu_if_attendance_result.setText("未签到");
        }
        else {
            holder.tv_stu_if_attendance_result.setText("未签到");
        }
        holder.tv_show_stu_attendance_system_time.setText("签到时间:"+stuAboutCheck.getTime());


    }

    @Override
    public int getItemCount() {
        if(stuAboutCheckList!=null){
           return stuAboutCheckList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_show_stuName_attendance;
        TextView tv_stu_if_attendance_result;
        TextView tv_show_stu_attendance_system_time;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_show_stuName_attendance= (TextView) itemView.findViewById(R.id.tv_show_stuName_attendance);
            tv_stu_if_attendance_result= (TextView) itemView.findViewById(R.id.tv_stu_if_attendance_result);
            tv_show_stu_attendance_system_time= (TextView) itemView.findViewById(R.id.tv_show_stu_attendance_system_time);
        }
    }
    public void updateData(List<StuAboutCheck> stuAboutCheckList){
        this.stuAboutCheckList=stuAboutCheckList;
        notifyDataSetChanged();
    }
}
