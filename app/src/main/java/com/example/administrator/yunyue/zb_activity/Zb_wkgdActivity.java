package com.example.administrator.yunyue.zb_activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Zb_wkgdActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_zb_wkgd_back;
    /**
     * 我看过的列表
     */
    private GridView gv_zb_wkgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_zb_wkgd);
        ll_zb_wkgd_back = findViewById(R.id.ll_zb_wkgd_back);
        ll_zb_wkgd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv_zb_wkgd = findViewById(R.id.gv_zb_wkgd);
        setGridView();
    }

    /**
     * 设置GirdView参数，绑定数据
     */
    private void setGridView() {
        MyAdapter myAdapter = new MyAdapter(Zb_wkgdActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");

            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_zb_wkgd.setAdapter(myAdapter);
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
                view = inflater.inflate(R.layout.zhibo_zb_gz_item, null);
            }

            return view;
        }
    }
}
