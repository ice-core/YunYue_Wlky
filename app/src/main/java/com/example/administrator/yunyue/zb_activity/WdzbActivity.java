package com.example.administrator.yunyue.zb_activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

public class WdzbActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_wdzb_back;
    /**
     * 关注
     */
    private LinearLayout ll_wdzb_gz;

    /**
     * 我看过的
     */
    private LinearLayout ll_wdzb_wkgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_wdzb);
        ll_wdzb_back = findViewById(R.id.ll_wdzb_back);
        ll_wdzb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_wdzb_gz = findViewById(R.id.ll_wdzb_gz);
        ll_wdzb_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WdzbActivity.this, Zb_GzActivity.class);
                startActivity(intent);
            }
        });
        ll_wdzb_wkgd = findViewById(R.id.ll_wdzb_wkgd);
        ll_wdzb_wkgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
