package com.example.jia.classcircle.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.bmobTable.GetClientsTalk;

import java.util.List;


/**
 * Created by jia on 2017/10/13.
 */

public class CommentPictureMsgAdapter  extends RecyclerView.Adapter<CommentPictureMsgAdapter.ViewHolder> {
    private List<GetClientsTalk> list;
    public CommentPictureMsgAdapter(List<GetClientsTalk> list){
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_stu_talk_about_picture_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetClientsTalk comment=list.get(position);
        holder.tv_client_userName.setText(comment.getUserName());
        holder.tv_show_client_content.setText(comment.getSpeakContent());
        holder.tv_show_client_speak_time.setText(comment.getSpeakTime());
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_client_userName;
        TextView tv_show_client_content;
        TextView tv_show_client_speak_time;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_client_userName= (TextView) itemView.findViewById(R.id.tv_client_userName);
            tv_show_client_content= (TextView) itemView.findViewById(R.id.tv_show_client_content);
            tv_show_client_speak_time= (TextView) itemView.findViewById(R.id.tv_show_client_speak_time);

        }
    }
    public void updateData(List<GetClientsTalk> list){
        this.list=list;
        notifyDataSetChanged();

    }
}
