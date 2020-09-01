package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class QunliaoActivity extends AppCompatActivity {
    private TextView tv_qunliao_name;

    private ImageView iv_qunliao_back;
    private GridView gv_qunliao;
    private MyAdapter myAdapter;
    private ImageView iv_qunlian_fs;
    private EditText et_liantian_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_qunliao);
        tv_qunliao_name = findViewById(R.id.tv_qunliao_name);
        iv_qunliao_back = findViewById(R.id.iv_qunliao_back);
        gv_qunliao = findViewById(R.id.gv_qunliao);
        iv_qunlian_fs = findViewById(R.id.iv_qunlian_fs);
        et_liantian_context = findViewById(R.id.et_liantian_context);
        iv_qunliao_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myAdapter = new MyAdapter(this);
        final ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        myAdapter.arrlist = mylist;
        gv_qunliao.setAdapter(myAdapter);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ItemName", "");
        mylist.add(map);
        myAdapter.notifyDataSetChanged();
        iv_qunlian_fs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hour = "";
                String minute = "";
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                if (cal.get(Calendar.AM_PM) == 0) {
                    hour = String.valueOf(cal.get(Calendar.HOUR));
                } else {
                    hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
                }
                minute = String.valueOf(cal.get(Calendar.MINUTE));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemName", et_liantian_context.getText().toString());
                map.put("ItemShijian", hour + ":" + minute);
                mylist.add(map);
                myAdapter.notifyDataSetChanged();
                et_liantian_context.setText("");
                View view1 = getWindow().peekDecorView();
                if (view1 != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
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
            if (position == 0) {
                if (view == null) {
                    view = inflater.inflate(R.layout.qunliaozuo_item, null);
                }

            } else {
                view = inflater.inflate(R.layout.qunliaoyou_item, null);
                TextView tv_qunliaoyou = view.findViewById(R.id.tv_qunliaoyou);
                tv_qunliaoyou.setText(arrlist.get(position).get("ItemName"));
            }
            return view;
        }
    }
}
