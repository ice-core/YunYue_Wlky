package com.example.administrator.yunyue.zssbqx;

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

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

public class ZssbqxFjscXqActivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    /**
     * 店铺评分
     */
    private int iDianpuFen = 0;
    /**
     * 评价
     */
    private GridView gv_zssbqxfjscxq_pj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zssbqxfjsc_xq);
        gv_zssbqxfjscxq_pj = findViewById(R.id.gv_zssbqxfjscxq_pj);
        setGridView();
    }

    private void setGridView() {
        myAdapter = new MyAdapter(ZssbqxFjscXqActivity.this);
        gv_zssbqxfjscxq_pj.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 5;
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
                view = inflater1.inflate(R.layout.fjsc_dppj_pf_item, null);

            }
            ImageView iv_fjsc_dppj_dianpufen = view.findViewById(R.id.iv_fjsc_dppj_dianpufen);
            ViewGroup.LayoutParams layoutParams = iv_fjsc_dppj_dianpufen.getLayoutParams();
            layoutParams.height = 12;
            layoutParams.width = 12;
            iv_fjsc_dppj_dianpufen.setLayoutParams(layoutParams); //设置参数
   /*         iv_fjsc_dppj_dianpufen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iDianpuFen = position + 1;
                    notifyDataSetChanged();
                }
            });*/
            if (iDianpuFen > position) {
                iv_fjsc_dppj_dianpufen.setImageResource(R.drawable.sc_dj_3x);
            } else {
                iv_fjsc_dppj_dianpufen.setImageResource(R.drawable.shouchang);
            }
            return view;
        }
    }
}
