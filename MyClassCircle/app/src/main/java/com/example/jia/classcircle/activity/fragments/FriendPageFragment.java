package com.example.jia.classcircle.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.activity.ShowClassmatesActivity;

/**
 * Created by jia on 2017/9/18.
 */

public class FriendPageFragment extends Fragment{//展示同个班级的同学（自己除外）
    public FriendPageFragment(){}
    private TextView tv_showStuResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_friend_page,container,false);
        tv_showStuResult= (TextView) v.findViewById(R.id.tv_showStuResult);


      /*点击传送到新界面*/
        tv_showStuResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                传递到展示同班同学界面
                * */
                startActivity(new Intent(getContext(), ShowClassmatesActivity.class));
            }
        });



        return v;
    }

}
