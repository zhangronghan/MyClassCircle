package com.example.jia.classcircle.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.dataClass.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jia on 2017/9/19.
 */

public class FirstPageAdapter extends BaseAdapter {//自定义适配器,用于九宫格展示
    private Context context;
    private int[] imgs;//这个用于声明图片
    private String functionName[];//这个用于展示图片下面的功能名称
    private List<Picture> pictureList = new ArrayList<>();
    private LayoutInflater inflater;

    public FirstPageAdapter(Context context, int[] imgs, String functionName[]) {
        this.context = context;
        this.imgs = imgs;
        this.functionName = functionName;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < imgs.length; i++) {
            Picture picture = new Picture(functionName[i], imgs[i]);
            pictureList.add(picture);
        }

    }

    @Override
    public int getCount() {
        if (pictureList != null) {
            return pictureList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return pictureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.picture_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_functionName);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_image);
            //   convertView.setBackgroundColor((0xFFFFFFFF));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(pictureList.get(position).getTitle());
        viewHolder.image.setImageResource(pictureList.get(position).getImageId());
        return convertView;
    }
}

class ViewHolder {
    public TextView title;
    public ImageView image;


}

