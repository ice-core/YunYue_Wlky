package com.example.administrator.yunyue.hyzx_activity;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Hyzx_ZdActivity extends AppCompatActivity {
    /**
     * tab
     */
    private GridView gv_hyzx_zd;
    private String[] tab = {"支出", "收入"};
    private MyAdapter myAdapter;
    private int iPosition = 0;

    /**
     * 返回
     */
    private LinearLayout ll_hyzx_zd_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.hui2a2a2a);
        }
        setContentView(R.layout.activity_hyzx__zd);
        gv_hyzx_zd = findViewById(R.id.gv_hyzx_zd);
        ll_hyzx_zd_back = findViewById(R.id.ll_hyzx_zd_back);
        ll_hyzx_zd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setGridView();
    }

    private void setGridView() {
        myAdapter = new MyAdapter(Hyzx_ZdActivity.this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_hyzx_zd.setAdapter(myAdapter);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
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
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPosition == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Hyzx_ZdActivity.this, R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(Hyzx_ZdActivity.this, R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPosition = position;
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }
}
