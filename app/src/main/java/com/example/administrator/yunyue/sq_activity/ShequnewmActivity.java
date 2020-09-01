package com.example.administrator.yunyue.sq_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

public class ShequnewmActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_sqewm_back;
    /**
     * 二维码
     */
    private ImageView iv_sqewm_code;
    private TextView tv_sqewm_hint;
    private TextView tv_sqewm_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shequnewm);
        Intent intent = getIntent();
        String sCode = intent.getStringExtra("code");
        String sType = intent.getStringExtra("type");
        tv_sqewm_hint = findViewById(R.id.tv_sqewm_hint);
        tv_sqewm_code = findViewById(R.id.tv_sqewm_code);
        ll_sqewm_back = findViewById(R.id.ll_sqewm_back);
        ll_sqewm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_sqewm_code = findViewById(R.id.iv_sqewm_code);
        Glide.with(ShequnewmActivity.this)
                .load( Api.sUrl+ sCode)
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_sqewm_code);
        if (sType.equals("shequn")) {
            tv_sqewm_hint.setText("社群二维码");
            tv_sqewm_code.setText("扫一扫上面的二维码图案，加入社群");
        } else {
            tv_sqewm_hint.setText("群聊二维码");
            tv_sqewm_code.setText("扫一扫上面的二维码图案，加入群聊");
        }
    }
}
