package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ShequActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private ImageView iv_shequ_back;
    /**
     * 热门社区列表
     */
    private ListView lv_shequ_rmsq;
    /**
     * 社区推荐
     */
    private ListView lv_shequ_sqtj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_shequ);
        iv_shequ_back = findViewById(R.id.iv_shequ_back);
        lv_shequ_rmsq = findViewById(R.id.lv_shequ_rmsq);
        lv_shequ_sqtj = findViewById(R.id.lv_shequ_sqtj);
        iv_shequ_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv_rmsq();
        gv_sqtj();
    }

    /**
     * 热门社区
     */
    private void gv_rmsq() {
        MyAdapterRmsq myAdapterRmsq = new MyAdapterRmsq(this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");
            mylist.add(map);
        }
        myAdapterRmsq.arrlist = mylist;
        lv_shequ_rmsq.setAdapter(myAdapterRmsq);
        setListViewHeightBasedOnChildren(lv_shequ_rmsq);
    }


    /**
     * 社区推荐
     */
    private void gv_sqtj() {
        MyAdapterSqtj myAdapterSqtj = new MyAdapterSqtj(this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 2; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", "");

            mylist.add(map);
        }
        myAdapterSqtj.arrlist = mylist;
        lv_shequ_sqtj.setAdapter(myAdapterSqtj);
        setListViewHeightBasedOnChildren(lv_shequ_sqtj);
    }

    /**
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth = getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
    }

    private class MyAdapterRmsq extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterRmsq(Context context) {
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
                view = inflater.inflate(R.layout.rmsq_item, null);
            }
            LinearLayout ll_rmsq_item = view.findViewById(R.id.ll_rmsq_item);
            ll_rmsq_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShequActivity.this, ShequXqActivity.class);
                    startActivity(intent);
                }
            });
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


    private class MyAdapterSqtj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterSqtj(Context context) {
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
                view = inflater.inflate(R.layout.sqtj_item, null);
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
