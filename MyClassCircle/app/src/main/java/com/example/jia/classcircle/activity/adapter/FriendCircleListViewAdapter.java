package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.FriendCircle;
import com.example.jia.classcircle.activity.listener.OnListViewClickListener;
import com.example.jia.classcircle.activity.util.ChildListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class FriendCircleListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<FriendCircle> mFriendCircleList;
    private OnListViewClickListener mOnListViewClickListener = null;
    private FriendCircleCommentAdapter commentAdapter;
    private List<String> mStringList = new ArrayList<>();

    public FriendCircleListViewAdapter(Context context, List<FriendCircle> friendCircleList, OnListViewClickListener mOnListViewClickListener) {
        mContext = context;
        mFriendCircleList = friendCircleList;
        this.mOnListViewClickListener = mOnListViewClickListener;
    }

    @Override
    public int getCount() {
        return mFriendCircleList == null ? 0 : mFriendCircleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriendCircleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friendcircle, parent, false);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.listView = (ChildListView) convertView.findViewById(R.id.listView);
            holder.btnComment = (Button) convertView.findViewById(R.id.btn_comment);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnListViewClickListener.onCommentClick(position);
            }
        });

        holder.tvUserName.setText(mFriendCircleList.get(position).getUsername());
        holder.tvContent.setText(mFriendCircleList.get(position).getContent());

        //========================评论==============================
        mStringList = mFriendCircleList.get(position).getStringList();
        commentAdapter = new FriendCircleCommentAdapter(mContext, mStringList);
        holder.listView.setAdapter(commentAdapter);

        return convertView;
    }


    public void refresh(List<FriendCircle> friendCircleList) {
        mFriendCircleList = friendCircleList;
        notifyDataSetChanged();
    }

    public void refreshChild(List<String> stringList, int index) {
        for(int i=0;i<mFriendCircleList.size() ;i++){
            if(i == index){
                mFriendCircleList.get(i).setStringList(stringList);
                continue;
            }
        }
        notifyDataSetChanged();
    }


    class viewHolder {
        TextView tvUserName;
        TextView tvContent;
        ListView listView;
        Button btnComment;
    }


}
