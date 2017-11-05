package com.example.jia.classcircle.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.jia.classcircle.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jia on 2017/11/3.
 */

public class PictureSlideFragment extends Fragment{//实现图片滑动
    private  String url;
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;
    public static PictureSlideFragment  newInstance(String url){
        PictureSlideFragment f = new PictureSlideFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_picture_slide,container,false);
        imageView= (ImageView) v.findViewById(R.id.iv_main_pic);
        mAttacher = new PhotoViewAttacher(imageView);//使用PhotoViewAttacher为图片添加支持缩放、平移的属性
        Glide.with(getActivity()).load(url).crossFade().into(new GlideDrawableImageViewTarget(imageView){
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mAttacher.update();///调用PhotoViewAttacher的update()方法，使图片重新适配布局
            }
        });

        return v;
    }
}
