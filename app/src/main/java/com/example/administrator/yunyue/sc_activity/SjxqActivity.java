package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class SjxqActivity extends AppCompatActivity {
    private LinearLayout ll_sjxq_back;
    private static final String TAG = SjxqActivity.class.getSimpleName();
    public static ArrayList<HashMap<String, String>> arrlist;
    private TextView tv_sjxq_name;//联系人
    private TextView tv_sjxq_phone;//联系电话
    private TextView tv_sjxq_storename;//店铺名称
    private TextView tv_sjxq_address;//所在地区
    private TextView tv_sjxq_detailed;//详细地址
    private TextView tv_sjxq_status;//店铺类型
    private TextView tv_sjxq_cname;//所售类目
    private ImageView iv_sjxq_image;//经营许可证
    private String position;
    private EditText et_sjxq_beizhu;
    private TextView tv_sjxq_number;
    private ImageView iv_sjxq_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_sjxq);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        ll_sjxq_back = (LinearLayout) findViewById(R.id.ll_sjxq_back);
        tv_sjxq_name = (TextView) findViewById(R.id.tv_sjxq_name);
        tv_sjxq_phone = (TextView) findViewById(R.id.tv_sjxq_phone);
        tv_sjxq_storename = (TextView) findViewById(R.id.tv_sjxq_storename);
        tv_sjxq_address = (TextView) findViewById(R.id.tv_sjxq_address);
        tv_sjxq_detailed = (TextView) findViewById(R.id.tv_sjxq_detailed);
        tv_sjxq_status = (TextView) findViewById(R.id.tv_sjxq_status);
        tv_sjxq_cname = (TextView) findViewById(R.id.tv_sjxq_cname);
        iv_sjxq_image = (ImageView) findViewById(R.id.iv_sjxq_image);
        et_sjxq_beizhu = (EditText) findViewById(R.id.et_sjxq_beizhu);
        tv_sjxq_number = (TextView) findViewById(R.id.tv_sjxq_number);
        iv_sjxq_back = (ImageView) findViewById(R.id.iv_sjxq_back);
        iv_sjxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_sjxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        tv_sjxq_name.setText(arrlist.get(Integer.valueOf(position)).get("ItemName"));
        tv_sjxq_phone.setText(arrlist.get(Integer.valueOf(position)).get("ItemPhone"));
        tv_sjxq_storename.setText(arrlist.get(Integer.valueOf(position)).get("ItemStorename"));
        tv_sjxq_address.setText(arrlist.get(Integer.valueOf(position)).get("ItemAddress"));
        tv_sjxq_detailed.setText(arrlist.get(Integer.valueOf(position)).get("ItemDetailed"));
        if (arrlist.get(Integer.valueOf(position)).get("ItemType").equals("1")) {
            tv_sjxq_status.setText("周边");
        } else if (arrlist.get(Integer.valueOf(position)).get("ItemType").equals("0")) {
            tv_sjxq_status.setText("全国");
        }
        tv_sjxq_cname.setText(arrlist.get(Integer.valueOf(position)).get("ItemCname"));

        Glide.with(SjxqActivity.this)
                .load( Api.sUrl+ arrlist.get(Integer.valueOf(position)).get("ItemImage"))
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_sjxq_image);
        et_sjxq_beizhu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_sjxq_number.setText(et_sjxq_beizhu.getText().length() + "/500");
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
