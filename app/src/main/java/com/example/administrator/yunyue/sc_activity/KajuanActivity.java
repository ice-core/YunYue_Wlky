package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class KajuanActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private ImageView iv_kajuan_back;

    /**
     * 列表
     */
    private GridView gv_kajuan;
    /**
     * 未使用
     */
    private LinearLayout ll_kaj_wsy;
    private TextView tv_kaj_wsy;
    private TextView tv_kaj_wsy_xhx;

    /**
     * 已失效
     */
    private LinearLayout ll_kaj_ysx;
    private TextView tv_kaj_ysx;
    private TextView tv_kaj_ysx_xhx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_kajuan);
        iv_kajuan_back = findViewById(R.id.iv_kajuan_back);
        gv_kajuan = findViewById(R.id.gv_kajuan);
        iv_kajuan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_kaj_wsy = findViewById(R.id.ll_kaj_wsy);
        tv_kaj_wsy = findViewById(R.id.tv_kaj_wsy);
        tv_kaj_wsy_xhx = findViewById(R.id.tv_kaj_wsy_xhx);
        ll_kaj_ysx = findViewById(R.id.ll_kaj_ysx);
        tv_kaj_ysx = findViewById(R.id.tv_kaj_ysx);
        tv_kaj_ysx_xhx = findViewById(R.id.tv_kaj_ysx_xhx);
        ll_kaj_wsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_kaj_wsy.setTextColor(tv_kaj_wsy.getResources().getColor(R.color.theme));
                tv_kaj_wsy_xhx.setBackgroundResource(R.color.theme);

                tv_kaj_ysx.setTextColor(tv_kaj_wsy.getResources().getColor(R.color.hei333333));
                tv_kaj_ysx_xhx.setBackgroundResource(R.color.touming);
            }
        });
        ll_kaj_ysx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_kaj_wsy.setTextColor(tv_kaj_wsy.getResources().getColor(R.color.hei333333));
                tv_kaj_wsy_xhx.setBackgroundResource(R.color.touming);

                tv_kaj_ysx.setTextColor(tv_kaj_wsy.getResources().getColor(R.color.theme));
                tv_kaj_ysx_xhx.setBackgroundResource(R.color.theme);
            }
        });
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        MyAdapter myAdapter = new MyAdapter(KajuanActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemId", "");
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_kajuan.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.kajuan_item, null);
            }

            return view;
        }
    }
}
