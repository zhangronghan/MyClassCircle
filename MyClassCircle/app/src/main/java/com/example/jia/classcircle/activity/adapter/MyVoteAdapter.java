package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.ActivityVoteGroup;


import com.example.jia.classcircle.activity.listener.OnItemClickListener;
import com.example.jia.classcircle.activity.util.BaseFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MyVoteAdapter extends RecyclerView.Adapter<VoteViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<ActivityVoteGroup> mList;
    private OnItemClickListener mOnItemClickListener = null;
    private boolean mIsSelectable = false;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private Boolean isCheckBoxVisible = false;     //显示多选框，默认不显示出来
    private List<Integer> selectPositionArr = new ArrayList<>();

    public MyVoteAdapter(Context context, List<ActivityVoteGroup> mList) {
        mContext = context;
        this.mList = mList;
    }

    @Override
    public VoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote, parent, false);
        VoteViewHolder viewHolder = new VoteViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteViewHolder holder, final int position) {
        ActivityVoteGroup activityVoteGroup = mList.get(position);
        holder.mTvNum.setText(String.valueOf(position + 1));
        holder.mTvTitle.setText(activityVoteGroup.getActivityTitle());
        holder.itemView.setTag(position);

        if (isCheckBoxVisible) {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mCheckBox.setChecked(isItemChecked(position));
        } else {
            holder.mCheckBox.setVisibility(View.GONE);
        }


        GradientDrawable myGrad = (GradientDrawable) holder.mTvNum.getBackground();
        myGrad.setColor(BaseFunction.getFirstWordColor(position));


        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(position)) {
                    setItemChecked(position, false);
                    Log.e("CheckBox", "position:" + position + "   boolean:false");
                } else {
                    setItemChecked(position, true);
                    Log.e("CheckBox", "position:" + position + "   boolean:true");
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void refresh(List<ActivityVoteGroup> activityVoteGroupList) {
        mList = activityVoteGroupList;
        isCheckBoxVisible=false;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }

    }


    //点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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


    public List<ActivityVoteGroup> getSelectedItem() {
        List<ActivityVoteGroup> activityVoteList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (isItemChecked(i)) {
                selectPositionArr.add(i);
                activityVoteList.add(mList.get(i));
            }
        }
        return activityVoteList;
    }

    //得到选中的标题
    public List<String> getSelectTitleArr() {
        List<String> mStringList = new ArrayList<>();
        for (int i = 0; i < selectPositionArr.size(); i++) {
            mStringList.add(mList.get(selectPositionArr.get(i)).getActivityTitle());
        }
        return mStringList;
    }


    public void setCheckBoxVisible(boolean isVisible) {
        isCheckBoxVisible = isVisible;
        notifyDataSetChanged();
    }


}