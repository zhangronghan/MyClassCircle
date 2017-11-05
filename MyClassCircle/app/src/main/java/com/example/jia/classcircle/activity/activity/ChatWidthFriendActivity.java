package com.example.jia.classcircle.activity.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.adapter.MsgAdapter;
import com.example.jia.classcircle.activity.bmobTable.APPUser;
import com.example.jia.classcircle.activity.bmobTable.Msg;
import com.example.jia.classcircle.activity.util.ChatWidthFriend;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class ChatWidthFriendActivity extends AppCompatActivity implements EMMessageListener{
    private Toolbar toolbar_chat;
    private RecyclerView msg_recyclerView;
    private EditText input_text;
    private Button btn_chatSend;
    private String friendName;
    private String mineName;
    private Bitmap friendImg;
    private Bitmap mineImg;
    private List<Msg> msgList=new ArrayList<>();
    private MsgAdapter msgAdapter;
    private APPUser user= BmobUser.getCurrentUser(APPUser.class);
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_width_friend);
        initView();
        onClickEvent();

    }



    private void onClickEvent() {
        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(input_text.length()!=0){
                    // 判断输入不为空，按钮可点击
                    btn_chatSend.setEnabled(true);
                    btn_chatSend.setBackgroundResource(R.drawable.login_button_selector);
                }else {
                    btn_chatSend.setEnabled(false);
                    btn_chatSend.setBackgroundColor(Color.parseColor("#999999"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar_chat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=input_text.getText().toString();
                if(content.length()==0){
                    Toast.makeText(ChatWidthFriendActivity.this,"输入为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(content.length()!=0){
                    final Msg msg=new Msg(content,Msg.TYPE_SENT);
                    msg.setMineName(mineName);
                    msg.setMineImg(mineImg);
                    EMMessage message = EMMessage.createTxtSendMessage(content, friendName);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    msgList.add(msg);
                    msgAdapter.notifyItemInserted(msgList.size()-1);
                    msg_recyclerView.scrollToPosition(msgList.size()-1);//将消息定位到最后一行
                    input_text.setText("");
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.e("EMM","消息发送成功");

                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.e("EMM","消息发送失败:"+s+"   i="+i);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });

                }
            }
        });

    }



    private void initView() {
        ChatWidthFriend friend= (ChatWidthFriend) getIntent().getSerializableExtra("friend");
        friendName=friend.getName();
        mineName=user.getUsername();
        Log.e("friend:",""+friendName);
        byte[] friendByte=friend.getImgByte();
        Log.e("friendIMG:",""+friendByte.length);
        byte[] mineByte=user.getImg_msg();
        friendImg= getBitmapFromByte(friendByte);
        mineImg= getBitmapFromByte(mineByte);


        msg_recyclerView= (RecyclerView) findViewById(R.id.msg_recyclerView);
        input_text= (EditText) findViewById(R.id.input_text);
        btn_chatSend= (Button) findViewById(R.id.btn_chatSend);
        toolbar_chat= (Toolbar) findViewById(R.id.toolbar_chat);
        mLinearLayout= (LinearLayout) findViewById(R.id.linearLayout);

        toolbar_chat.setTitle(friendName);

        //根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)) {
            toolbar_chat.setBackgroundColor(Color.parseColor("#534f4f"));
            mLinearLayout.setBackgroundColor(Color.parseColor("#423737"));
            input_text.setTextColor(Color.BLACK);
        }

        setSupportActionBar(toolbar_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msg_recyclerView.setLayoutManager(layoutManager);
        msgAdapter=new MsgAdapter(msgList);
        msg_recyclerView.setAdapter(msgAdapter);
     //   refreshData();获取聊天记录，但是分不清内容哪个是朋友发的，哪个是自己发的

    }

    private void refreshData() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friendName);
        List<EMMessage> messages = conversation.getAllMessages();
        for(int i=0;i<messages.size();i++){
            Msg msg=new Msg( messages.get(i).getBody().toString(),Msg.TYPE_RECEIVED);
            msg.setMineName(friendName);
            msg.setMineImg(friendImg);
            msgList.add(msg);
        }
        msgAdapter.updateData(msgList);
    }


    @Override
    public void onMessageReceived(List<EMMessage> list) {

        EMMessage emMessage=list.get(list.size()-1);
        String content=  getContentMessage(emMessage.getBody().toString());//截取双引号中的内容
        Msg msg=new Msg(content,Msg.TYPE_RECEIVED);
        msgList.add(msg);
        msg.setFriednImg(friendImg);
        msgAdapter.notifyItemInserted(msgList.size()-1);
        msg_recyclerView.scrollToPosition(msgList.size()-1);
    }

    private String getContentMessage(String s) {
        String content=s.substring(5,s.length()-1);
        return content;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //通过注册消息监听来接收消息
        EMClient.getInstance().chatManager().addMessageListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息监听
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }
    public Bitmap getBitmapFromByte(byte[] temp){//将二进制转化为bitmap
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }
}
