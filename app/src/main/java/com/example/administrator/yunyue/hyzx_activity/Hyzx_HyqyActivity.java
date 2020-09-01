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

public class Hyzx_HyqyActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_hyzx_hyqy_back;
    /**
     * 规则
     */
    private TextView tv_hyzx_hyqy_gz;
    /**
     * 升级礼包
     */
    private LinearLayout ll_hyzx_hyqy_sjlb;
    /**
     * 会员专享
     */
    private LinearLayout ll_hyzx_hyqy_hyzx;
    /**
     * 金币超值
     */
    private LinearLayout ll_hyzx_hyqy_jbcz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.hui2a2a2a);
        }
        setContentView(R.layout.activity_gyzx__hyqy);
        ll_hyzx_hyqy_back = findViewById(R.id.ll_hyzx_hyqy_back);
        ll_hyzx_hyqy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_hyzx_hyqy_gz = findViewById(R.id.tv_hyzx_hyqy_gz);
        tv_hyzx_hyqy_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Hyzx_HyqyActivity.this, Hyzx_JlgzActivity.class);
                startActivity(intent);
            }
        });
        ll_hyzx_hyqy_sjlb = findViewById(R.id.ll_hyzx_hyqy_sjlb);
        ll_hyzx_hyqy_hyzx = findViewById(R.id.ll_hyzx_hyqy_hyzx);
        ll_hyzx_hyqy_jbcz = findViewById(R.id.ll_hyzx_hyqy_jbcz);
    }
}
