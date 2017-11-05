package com.example.jia.classcircle.activity.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jia.classcircle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jia on 2017/11/3.
 */

public class GridViewAboutPhotoAdapter extends BaseAdapter {
    private String  photo_url;//展示的图片
    private String num;//展示第几张
    private Context context;
    private List<String> list=new ArrayList<>();
    private LayoutInflater inflater;

    //   Glide.with(getApplicationContext()).load(getInternetImgUrl.get(position-1)).into(img_delete);
    public GridViewAboutPhotoAdapter( Context context,List<String> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 viewHolder2;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_about_photo,null);
            viewHolder2=new ViewHolder2();
            viewHolder2.tv_get_num= (TextView) convertView.findViewById(R.id.tv_get_num);
            viewHolder2.photo= (ImageView) convertView.findViewById(R.id.photo_img);
            convertView.setTag(viewHolder2);
        }else {
            viewHolder2= (ViewHolder2) convertView.getTag();
        }

        Glide.with(context).load(list.get(position)).into( viewHolder2.photo);
        int num=position;

        viewHolder2.tv_get_num.setText(("NO."+(num+1)));
        return convertView;
    }
    public void updateData(List<String> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
class ViewHolder2 {
    public TextView tv_get_num;
    public ImageView photo;
}