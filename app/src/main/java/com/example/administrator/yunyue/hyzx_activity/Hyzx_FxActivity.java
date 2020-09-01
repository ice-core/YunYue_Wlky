package com.example.administrator.yunyue.hyzx_activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

public class Hyzx_FxActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_hyzx_fx_back;
    /**
     * 升级礼包
     */
    private LinearLayout ll_hyzx_fx_sjlb;
    /**
     * 提现
     */
    private LinearLayout ll_hyzx_fx_tx;
    /**
     * 账单
     */
    private LinearLayout ll_hyzx_fx_zd;
    /**
     * 邀请记录
     */
    private LinearLayout ll_hyzx_fx_yqjl;
    /**
     * 奖励规则
     */
    private TextView tv_hyzx_fx_jlgz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.hui2a2a2a);
        }
        setContentView(R.layout.activity_hyzx__fx);
        ll_hyzx_fx_back = findViewById(R.id.ll_hyzx_fx_back);
        ll_hyzx_fx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_hyzx_fx_sjlb = findViewById(R.id.ll_hyzx_fx_sjlb);
        ll_hyzx_fx_tx = findViewById(R.id.ll_hyzx_fx_tx);
        ll_hyzx_fx_zd = findViewById(R.id.ll_hyzx_fx_zd);
        ll_hyzx_fx_yqjl = findViewById(R.id.ll_hyzx_fx_yqjl);
        tv_hyzx_fx_jlgz = findViewById(R.id.tv_hyzx_fx_jlgz);
        tv_hyzx_fx_jlgz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Hyzx_FxActivity.this, Hyzx_JlgzActivity.class);
                startActivity(intent);
            }
        });
    }
}
