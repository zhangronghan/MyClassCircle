package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.ClassHomeWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jia on 2017/10/14.
 */

public class ShowStuDataAndReceiveAdapter extends RecyclerView.Adapter<ShowStuDataAndReceiveAdapter.ViewHolder> {
    private List<ClassHomeWork> classHomeWorkList;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();//存布尔值的集合
    private boolean mIsSelectable = false;//默认全部未选
    public ShowStuDataAndReceiveAdapter(List<ClassHomeWork> classHomeWorkList){
        this.classHomeWorkList=classHomeWorkList;
    }

     static class ViewHolder extends RecyclerView.ViewHolder{
         TextView tv_stu_userName;
         TextView tv_show_up_load_file_from_stu;
         CheckBox select_checkbox;
         TextView tv_show_up_load_file_system_time;
         public ViewHolder(View itemView) {
             super(itemView);
             tv_stu_userName= (TextView) itemView.findViewById(R.id.tv_stu_userName);
             tv_show_up_load_file_from_stu= (TextView) itemView.findViewById(R.id.tv_show_up_load_file_from_stu);
             tv_show_up_load_file_system_time= (TextView) itemView.findViewById(R.id.tv_show_up_load_file_system_time);
             select_checkbox= (CheckBox) itemView.findViewById(R.id.select_checkbox);
         }
     }

    @Override
    public ShowStuDataAndReceiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_up_load_stu_file_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShowStuDataAndReceiveAdapter.ViewHolder holder, final int position) {

        ClassHomeWork classHomeWork=classHomeWorkList.get(position);
        holder.select_checkbox.setChecked(isItemChecked(position));
        holder.tv_stu_userName.setText(classHomeWork.getUserName());
        String fileName=classHomeWork.getHomeworlUrl();
        String [] strArry=fileName.split("\\/");
        int last=strArry.length;//获取分隔符最后一个，取得文件名
        holder.tv_show_up_load_file_from_stu.setText(strArry[last-1]);
        holder.tv_show_up_load_file_system_time.setText(classHomeWork.getTime());
        holder.select_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItemChecked(position)){
                    setItemChecked(position,false);
                }else {
                    setItemChecked(position,true);
                }
            }
        });
        //条目view的监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItemChecked(position)){
                    setItemChecked(position, false);
                }else {
                    setItemChecked(position, true);
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(classHomeWorkList!=null){
            return classHomeWorkList.size();
        }
        return 0;
    }
    public void updateDate(List<ClassHomeWork> classHomeWorkList){
        this.classHomeWorkList=classHomeWorkList;
        notifyDataSetChanged();
    }
    //设置给定位置条目的选择状态
    private void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //根据位置判断条目是否可选
    private boolean isSelectable() {
        return mIsSelectable;
    }
    //设置给定位置条目的可选与否的状态
    private void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }
    //获得选中条目的结果
    public List<ClassHomeWork> getSelectedItem() {
        List<ClassHomeWork> selectList = new ArrayList<>();
        for (int i = 0; i < classHomeWorkList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(classHomeWorkList.get(i));
            }
        }
        return selectList;
    }

}
