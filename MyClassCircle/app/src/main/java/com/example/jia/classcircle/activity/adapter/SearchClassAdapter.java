package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.AllTheClass;
import com.example.jia.classcircle.activity.listener.MyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jia on 2017/10/5.
 */

public class SearchClassAdapter extends RecyclerView.Adapter<SearchClassAdapter.ViewHolder> {//需要的数据就只有班级表的所有班级
    private ArrayList<AllTheClass> allClassList;
    private MyOnItemClickListener itemClickListener;
    public SearchClassAdapter(ArrayList<AllTheClass> allClassList){
        for(int i=0;i<allClassList.size();i++){
            if(allClassList.get(i).getClassName()==null){
                allClassList.remove(i);
            }
        }

        this.allClassList=allClassList;
    }
    public void updateDate(ArrayList<AllTheClass> allClassList){//实现数据更新用,用下拉刷新实现,点击搜索实现
        this.allClassList=allClassList;
        notifyDataSetChanged();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_class_item,parent,false);
        RecyclerView.ViewHolder viewHolder=new ViewHolder(v);
        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
            if(itemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.OnItemClickListener(holder.itemView,holder.getLayoutPosition());
                    }
                });
            }
            holder.showClassTv.setText(allClassList.get(position).getClassName()+"班");
    }

    @Override
    public int getItemCount() {
        if(allClassList==null){
            return 0;
        }
        return allClassList.size();

    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView showClassTv;
        public ViewHolder(View itemView) {
            super(itemView);
            showClassTv= (TextView) itemView.findViewById(R.id.tv_show_class);

        }
    }
    public void setOnItemClickListener(MyOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
