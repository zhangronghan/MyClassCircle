package com.example.jia.classcircle.activity.bmob_communication;


import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/*
 * Created by jia on 2017/10/10.
 **/


public class DemoMessageHandler extends BmobIMMessageHandler {//没有实现及时通讯,保留下来的，没用到，但是去掉接入的很麻烦，所以丢这不管了
    //TODO 集成：1.6、自定义消息接收器处理在线消息和离线消息

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息

    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用

    }
}
