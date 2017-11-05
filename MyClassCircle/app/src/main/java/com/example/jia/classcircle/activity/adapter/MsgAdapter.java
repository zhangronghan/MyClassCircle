package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.Msg;
import com.example.jia.classcircle.activity.util.ImageViewPlus;

import java.util.List;

/**
 * Created by jia on 2017/10/25.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;
    public MsgAdapter(List<Msg> msgList){
        this.msgList=msgList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        RelativeLayout rightLayout;
        ImageViewPlus  youFriend_imageViewPlus;
        ImageViewPlus youSelf_imageViewPlus;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout= (LinearLayout) itemView.findViewById(R.id.left_layout);
            rightLayout= (RelativeLayout) itemView.findViewById(R.id.right_layout);
            youFriend_imageViewPlus= (ImageViewPlus) itemView.findViewById(R.id.youFriend_imageViewPlus);
            youSelf_imageViewPlus= (ImageViewPlus) itemView.findViewById(R.id.youSelf_imageViewPlus);

            leftMsg= (TextView) itemView.findViewById(R.id.left_msg);
            rightMsg= (TextView) itemView.findViewById(R.id.right_msg);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg=msgList.get(position);
        if(msg.getType()==Msg.TYPE_RECEIVED){//收到消息，显示左边
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.youSelf_imageViewPlus.setVisibility(View.GONE);
            holder.youFriend_imageViewPlus.setImageBitmap(msg.getFriednImg());//显示朋友的头像
            holder.leftMsg.setText(msg.getContent());//显示朋友的话


        }else if(msg.getType()==Msg.TYPE_SENT){//发出消息，将左边隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.youFriend_imageViewPlus.setVisibility(View.GONE);
            holder.youSelf_imageViewPlus.setImageBitmap(msg.getMineImg());
            holder.rightMsg.setText(msg.getContent());


        }
    }

    @Override
    public int getItemCount() {
        if(msgList!=null){
            return msgList.size();
        }
        return 0;
    }

    public void updateData(List<Msg> msgList){
        this.msgList=msgList;
        notifyDataSetChanged();
    }
}
