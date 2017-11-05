package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jia.classcircle.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class FriendCircleCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mStringList;

    public FriendCircleCommentAdapter(Context context, List<String> friendCircleCommentList) {
        mContext = context;
        mStringList = friendCircleCommentList;
    }

    @Override
    public int getCount() {
        return mStringList == null ? 0 : mStringList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder=null;
        if(convertView == null){
            holder=new MyHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_friendcircle_commtent,parent,false);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvComment= (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        } else {
            holder= (MyHolder) convertView.getTag();
        }
//        Log.e("AAA","position:"+position+"   "+mStringList.get(position));
        String comment=mStringList.get(position).toString();
        String[] arr=comment.split("#");
        holder.tvName.setText(arr[0]+":");
        holder.tvComment.setText(arr[1]);

        return convertView;
    }

    public void refresh(List<String> stringList) {
        mStringList=stringList;
        notifyDataSetChanged();

    }


    class MyHolder{
        TextView tvName;
        TextView tvComment;

    }


}
