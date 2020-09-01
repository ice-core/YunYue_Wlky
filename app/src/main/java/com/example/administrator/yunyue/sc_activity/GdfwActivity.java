package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;

public class GdfwActivity extends AppCompatActivity {

    private LinearLayout ll_gdfw_yygh, ll_gdfw_hfcz, ll_gdfw_gxdc,
            ll_gdfw_yx, ll_gdfw_dyp, ll_gdfw_px, ll_gdfw_sf,
            ll_gdfw_df, ll_gdfw_rqf, ll_gdfw_jyz, ll_gdfw_jdfw;
    private ImageView iv_gdfw_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_gdfw);
        ll_gdfw_yygh = findViewById(R.id.ll_gdfw_yygh);
        ll_gdfw_hfcz = findViewById(R.id.ll_gdfw_hfcz);
        ll_gdfw_gxdc = findViewById(R.id.ll_gdfw_gxdc);
        ll_gdfw_yx = findViewById(R.id.ll_gdfw_yx);
        ll_gdfw_dyp = findViewById(R.id.ll_gdfw_dyp);
        ll_gdfw_px = findViewById(R.id.ll_gdfw_px);
        ll_gdfw_sf = findViewById(R.id.ll_gdfw_sf);
        ll_gdfw_df = findViewById(R.id.ll_gdfw_df);
        ll_gdfw_rqf = findViewById(R.id.ll_gdfw_rqf);
        ll_gdfw_jyz = findViewById(R.id.ll_gdfw_jyz);
        ll_gdfw_jdfw = findViewById(R.id.ll_gdfw_jdfw);
        iv_gdfw_back = findViewById(R.id.iv_gdfw_back);
        ll_gdfw_yygh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_hfcz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GdfwActivity.this, SjczActivity.class);
                startActivity(intent);
            }
        });
        ll_gdfw_gxdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_yx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_dyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_px.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_df.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_rqf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_jyz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        ll_gdfw_jdfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hint("敬待开放", HintDialog.WARM);
            }
        });
        iv_gdfw_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 消息提示
     */

    protected void Hint(String sHint, int type) {
        new HintDialog(GdfwActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
