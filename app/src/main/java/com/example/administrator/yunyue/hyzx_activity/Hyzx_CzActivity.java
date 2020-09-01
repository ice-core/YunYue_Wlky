package com.example.administrator.yunyue.hyzx_activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

public class Hyzx_CzActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_hyzx_cz_back;
    /**
     * 下一步
     */
    private TextView tv_hyzx_cz_xyb;
    /**
     * 金额
     */
    private EditText et_hyzx_cz_je;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.hui2a2a2a);
        }
        setContentView(R.layout.activity_hyzx__cz);
        ll_hyzx_cz_back = findViewById(R.id.ll_hyzx_cz_back);
        ll_hyzx_cz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_hyzx_cz_xyb = findViewById(R.id.tv_hyzx_cz_xyb);
        et_hyzx_cz_je = findViewById(R.id.et_hyzx_cz_je);
    }
}
