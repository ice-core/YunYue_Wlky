package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ShztActivity extends AppCompatActivity {
    private static final String TAG = ShztActivity.class.getSimpleName();
    private ImageView iv_shzt_bj;
    private ImageView iv_shzt_zt;
    private String state = "";
    private TextView tv_shzt_name;
    private TextView tv_shzt_describe;
    private Button bt_shzt;
    private ImageView iv_shzt_back;
    private String position;
    public static ArrayList<HashMap<String, String>> arrlist;
    private LinearLayout ll_shzt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        position = intent.getStringExtra("position");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (state.equals("tg")) {
                StatusBarUtil.setStatusBarColor(this, R.color.theme);
            } else if (state.equals("wtg")) {
                StatusBarUtil.setStatusBarColor(this, R.color.sh_sb);
            } else if (state.equals("dsh")) {
                StatusBarUtil.setStatusBarColor(this, R.color.sh_dd);
            }
        }

        setContentView(R.layout.activity_shzt);
        iv_shzt_bj = (ImageView) findViewById(R.id.iv_shzt_bj);
        iv_shzt_zt = (ImageView) findViewById(R.id.iv_shzt_zt);
        tv_shzt_name = (TextView) findViewById(R.id.tv_shzt_name);
        tv_shzt_describe = (TextView) findViewById(R.id.tv_shzt_describe);
        bt_shzt = (Button) findViewById(R.id.bt_shzt);
        iv_shzt_back = (ImageView) findViewById(R.id.iv_shzt_back);
        ll_shzt_back = findViewById(R.id.ll_shzt_back);
        ll_shzt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        iv_shzt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        if (state.equals("tg")) {
            Drawable drawableColor = new ColorDrawable(ContextCompat.getColor(this, R.color.theme));//使用colors.xml文件中的颜色【在这里未使用，只是用来说明一种方式】
            iv_shzt_bj.setImageDrawable(drawableColor);
            iv_shzt_zt.setImageResource(R.mipmap.shcg);
            tv_shzt_name.setText("恭喜，您的审核已通过");
            tv_shzt_describe.setText("恭喜，您的资料信息相符，已经完成审核通过。");
            bt_shzt.setText("查看详情");
        } else if (state.equals("wtg")) {
            Drawable drawableColor = new ColorDrawable(ContextCompat.getColor(this, R.color.sh_sb));//使用colors.xml文件中的颜色【在这里未使用，只是用来说明一种方式】
            iv_shzt_bj.setImageDrawable(drawableColor);
            iv_shzt_zt.setImageResource(R.mipmap.shsb);
            tv_shzt_name.setText("抱歉，您的申请未通过");
            tv_shzt_describe.setText("抱歉，您的资料信息存在不符，请重新核实后再提交申请。");
            bt_shzt.setText("重新提交");
        } else if (state.equals("dsh")) {
            Drawable drawableColor = new ColorDrawable(ContextCompat.getColor(this, R.color.sh_dd));//使用colors.xml文件中的颜色【在这里未使用，只是用来说明一种方式】
            iv_shzt_bj.setImageDrawable(drawableColor);
            iv_shzt_zt.setImageResource(R.mipmap.ddsh);
            tv_shzt_name.setText("稍等，您的申请正在路上");
            tv_shzt_describe.setText("稍等，您的资料信息正在审核，请您耐心等待。");
            bt_shzt.setVisibility(View.GONE);
        }
        bt_shzt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("tg")) {
                    Intent intent = new Intent(ShztActivity.this, SjxqActivity.class);
                    SjxqActivity.arrlist = arrlist;
                    intent.putExtra("position", String.valueOf(position));
                    startActivity(intent);
                }
            }
        });

    }

    private void back() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                back();
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
