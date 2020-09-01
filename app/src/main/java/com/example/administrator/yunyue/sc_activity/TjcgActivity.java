package com.example.administrator.yunyue.sc_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;


public class TjcgActivity extends AppCompatActivity {
    private static final String TAG = TjcgActivity.class.getSimpleName();
    private ImageView iv_tjcg_back;
    private Button bt_tjcg_zf;
    private String sResidence_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_tjcg);
        Intent intent = getIntent();
        sResidence_id = intent.getStringExtra("residence_id");
        iv_tjcg_back = (ImageView) findViewById(R.id.iv_tjcg_back);
        bt_tjcg_zf = (Button) findViewById(R.id.bt_tjcg_zf);
        iv_tjcg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        bt_tjcg_zf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TjcgActivity.this, ZhifuActivity.class);
                intent.putExtra("residence_id", sResidence_id);
                startActivity(intent);
                finish();
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
