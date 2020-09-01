package com.example.administrator.yunyue.Im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.sq_activity.GrtzActivity;
import com.example.administrator.yunyue.sq_activity.QunxiangqingActivity;


import java.util.Locale;

import io.rong.imlib.model.Conversation;

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = ConversationActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_conversation_back;
    /**
     * name
     */
    private TextView tv_conversation_name;
    /**
     * 詳情
     */
    private ImageView iv_conversation_xq;
    public static String sType = "";

    private Conversation.ConversationType mConversationType; //会话类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        ll_conversation_back = findViewById(R.id.ll_conversation_back);
        tv_conversation_name = findViewById(R.id.tv_conversation_name);
        ll_conversation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.US));
        String sName = getIntent().getData().getQueryParameter("title");//获取昵称
        final String targetId = getIntent().getData().getQueryParameter("targetId");//获取昵称
        setTitle("与" + sName + "聊天中");
        tv_conversation_name.setText(sName);
        iv_conversation_xq = findViewById(R.id.iv_conversation_xq);
        iv_conversation_xq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 判断会话类型
                 * */
                if (mConversationType.getName().equals("group")) {
                    Intent intent = new Intent(ConversationActivity.this, QunxiangqingActivity.class);
                    QunxiangqingActivity.sGroup_Id = targetId;
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ConversationActivity.this, GrtzActivity.class);
                    //HyxqActivity.sId = targetId;
                    GrtzActivity.sFriend_id = targetId;
                    startActivity(intent);
                }
            }
        });
    }

    /**
     *  * 方法名：onKeyDown(int keyCode, KeyEvent event)
     *  * 功    能：菜单栏点击事件
     *  * 参    数：int keyCode, KeyEvent event
     *  * 返回值：无
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                finish();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}