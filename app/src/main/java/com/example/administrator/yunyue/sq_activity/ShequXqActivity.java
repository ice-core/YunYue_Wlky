package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ShequXqActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private ImageView iv_shequxq_back;
    /**
     * 列表
     */
    private GridView gv_shequxq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shequ_xq);
        iv_shequxq_back = findViewById(R.id.iv_shequxq_back);
        gv_shequxq = findViewById(R.id.gv_shequxq);
        iv_shequxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv();
    }

    /**
     * 动态
     */
    private void gv() {
        MyAdapter myAdapter = new MyAdapter(this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");

            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        gv_shequxq.setAdapter(myAdapter);

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
                view = inflater.inflate(R.layout.shequxq_item, null);
            }
            LinearLayout ll_shequxq_image_3 = view.findViewById(R.id.ll_shequxq_image_3);
            ImageView iv_shequxq_1 = view.findViewById(R.id.iv_shequxq_1);
            if (position == 2) {
                ll_shequxq_image_3.setVisibility(View.GONE);
                iv_shequxq_1.setVisibility(View.VISIBLE);
            } else {
                ll_shequxq_image_3.setVisibility(View.VISIBLE);
                iv_shequxq_1.setVisibility(View.GONE);
            }
     /*       ll_shequn_shequ_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (iv_shequn_shequ_item.getDrawable().getCurrent().getConstantState().equals(getResources()
                            .getDrawable(R.drawable.public_btn_like_current_3x).getConstantState())) {

                        //当image1的src为R.drawable.A时，设置image1的src为R.drawable.B

                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_moren_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) - 1));
                    } else {
                        //否则设置image1的src为R.drawable.A
                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_current_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) + 1));
                    }

                }
            });
  */


            return view;
        }
    }
}
